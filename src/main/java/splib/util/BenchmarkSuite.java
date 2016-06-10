package splib.util;


import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import splib.util.Triple;
import splib.util.Quad;
import splib.data.Graph;
import splib.data.SPVertex;
import splib.data.BDDVertex;
import splib.algo.Oracle;
import splib.algo.Dijkstra;
import java.lang.reflect.InvocationTargetException;
import org.github.jamm.*;


/*
 * A class to perform a benchmark of methods, specifically in regard to their
 * time complexity.
 */
public class BenchmarkSuite <V extends SPVertex> {

  public enum Output {
    CSV,
    REGULAR,
    NONE
  }

  @FunctionalInterface
  public interface SingleSourceAlgorithm<W extends SPVertex> {
    public ArrayList<Integer> sssp(Graph<W> G, int s, int heapArity);
  }

  @FunctionalInterface
  public interface SinglePairAlgorithm<W extends SPVertex> {
    public Object spsp(Graph<W> G, int s, int t, int heapArity);
  }

  private ArrayList<Triple<String, SingleSourceAlgorithm, Triple<Graph<V>, Integer,
          Integer>>> singleSourceBenchmarks;
  private ArrayList<Triple<String, SinglePairAlgorithm, Quad<Graph<V>, Integer, Integer,
          Integer>>> singlePairBenchmarks;
  private ArrayList<Quint<String, Class<?>, Graph<V>, Integer, Integer>>
    oraclePreprocessBenchmarks;
  private ArrayList<Triple<String, Oracle, Graph<V>>> oracleQueryBenchmarks;

  private Class<V> vClass;

  public BenchmarkSuite(Class<V> vClass) {
    this.vClass = vClass;
    this.singleSourceBenchmarks = new ArrayList<Triple<String,
      SingleSourceAlgorithm, Triple<Graph<V>, Integer, Integer>>>();
    this.singlePairBenchmarks = new ArrayList<Triple<String,
      SinglePairAlgorithm, Quad<Graph<V>, Integer, Integer, Integer>>>();
    this.oraclePreprocessBenchmarks = new ArrayList<Quint<String, Class<?>, Graph<V>,
      Integer, Integer>>();
    this.oracleQueryBenchmarks = new ArrayList<Triple<String, Oracle, Graph<V>>>();
  }

  public void addSingleSourceBenchmark(String name, SingleSourceAlgorithm<V>
      algo, Graph<V> G, int s, int heapArity) {
    this.singleSourceBenchmarks.add(new Triple(name, algo, new Triple(G, s,
            heapArity)));
  }

  public void addSinglePairBenchmark(String name, SinglePairAlgorithm<V> algo,
      Graph<V> G, int s, int t, int heapArity) {
    this.singlePairBenchmarks.add(new Triple(name, algo,
          new Quad(G, s, t, heapArity)));
  }

  public <O extends Oracle> void addOraclePreprocessBenchmark(String name,
      Class<O> oClass, Graph<V> G, int k, int heapArity) {
    this.oraclePreprocessBenchmarks.add(new Quint(name, oClass, G, k,
          heapArity)); }

  public void addOracleQueryBenchmark(String name, Oracle o, Graph<V> G) {
    this.oracleQueryBenchmarks.add(new Triple(name, o, G));
  }

  public void runSinglePairBenchmark(Output type, String name,
        SinglePairAlgorithm<V> algo, Quad<Graph<V>, Integer, Integer, Integer> args) {
      long ns = System.nanoTime();
      long ms = System.currentTimeMillis();
      algo.<V>spsp(args.getItem1(), args.getItem2(), args.getItem3(), args.getItem4());
      ns = System.nanoTime() - ns;
      ms = System.currentTimeMillis() - ms;

      int relaxed = 0;
      // count relaxed vertices
      for (V v : args.getItem1().getVertices()) {
        if (BDDVertex.class.isAssignableFrom(vClass)) {
          if (((BDDVertex)v).getSuccessor() != null || v.getPredecessor() != null) {
            relaxed++;
          }
        } else {
          if (v.getPredecessor() != null) {
            relaxed++;
          }
        }
      }

      if (type == Output.REGULAR) {
        System.out.println("Benchmarking " + name);
        System.out.println("heap arity: " + args.getItem4());
        System.out.println(" vertices: " + args.getItem1().getVertexCount());
        System.out.println("    edges: " + args.getItem1().getEdgeCount());
        System.out.println(" r. verts: " + relaxed);
        System.out.printf("       ns: %s\n", ns);
        System.out.printf("       ms: %s\n", ms);
      } else if (type == Output.CSV) {
        System.out.printf("%s\t%d\t%d\t%d\t%d\t%d\t%d\t\n", name,
            args.getItem4(), args.getItem1().getVertexCount(),
            args.getItem1().getEdgeCount(), relaxed, ms, ns);
      }
  }

  public void runSingleSourceBenchmark(Output type, String name,
      SingleSourceAlgorithm<V> algo, Triple<Graph<V>, Integer, Integer> args) {
    long ns = System.nanoTime();
    long ms = System.currentTimeMillis();
    algo.<V>sssp(args.getItem1(), args.getItem2(), args.getItem3());
    ns = System.nanoTime() - ns;
    ms = System.currentTimeMillis() - ms;

    if (type == Output.REGULAR) {
      System.out.println("Benchmarking " + name);
      System.out.println("heap arity: " + args.getItem3());
      System.out.println(" vertices: " + args.getItem1().getVertexCount());
      System.out.println("    edges: " + args.getItem1().getEdgeCount());
      System.out.printf("       ns: %s\n", ns);
      System.out.printf("       ms: %s\n", ms);
    } else if (type == Output.CSV) {
      System.out.printf("%s\t%d\t%d\t%d\t%d\t%d\t\n", name,
          args.getItem3(), args.getItem1().getVertexCount(),
          args.getItem1().getEdgeCount(), ms, ns);
    }
  }

  public void runOraclePreprocessBenchmark(Output type,
      String name, Class<?> oClass, Triple<Graph<V>, Integer, Integer> args) {
    try {
      Constructor ctor = oClass.getConstructor(Integer.class);

      long ns = System.nanoTime();
      long ms = System.currentTimeMillis();
      Runtime runtime = Runtime.getRuntime();
      int vertices = args.getItem1().getVertexCount();
      int edges = args.getItem1().getEdgeCount();

      MemoryMeter meter = new MemoryMeter();
      Oracle or = (Oracle)ctor.newInstance(args.getItem2());
      or.preprocess(args.getItem1(), args.getItem3());

      long memory = meter.measureDeep(or);

      ns = System.nanoTime() - ns;
      ms = System.currentTimeMillis() - ms;
      long memoryMb = memory / 1024 / 1024;
      if (type == Output.REGULAR) {
        System.out.println("Benchmarking " + name);
        System.out.println("heap arity: " + args.getItem3());
        System.out.println(" vertices: " + args.getItem1().getVertexCount());
        System.out.println("    edges: " + args.getItem1().getEdgeCount());
        System.out.println("        k: " + args.getItem2());
        System.out.println("  mem (b): " + memory);
        System.out.println(" mem (mb): " + memoryMb);
        System.out.printf("       ns: %s\n", ns);
        System.out.printf("       ms: %s\n", ms);
      } else if (type == Output.CSV) {
        System.out.printf("%s\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\n", name,
            args.getItem3(), vertices, edges, args.getItem2(), memoryMb, memory, ms, ns);
      }
    } catch (InvocationTargetException | IllegalAccessException |
        NoSuchMethodException | InstantiationException ex) {
      // Ignore
      System.out.println(ex.toString());
    }
  }

  public void runOracleQueryBenchmark(Output type, String name,
      Oracle o, Graph<V> G) {
    long avgNs = 0;
    long avgMs = 0;
    double minStretch = Double.MAX_VALUE;
    double avgStretch = 0;
    double maxStretch = 0;
    int pairs = (G.getVertexCount() * (G.getVertexCount() - 1)) / 2;
    int connectedPairs = pairs;

    // Query all pairs of vertices
    for (int i = 0; i < G.getVertexCount(); i++) {
      Dijkstra.<V>singleSource(G, i, 4);
      for (int j = i + 1; j < G.getVertexCount(); j++) {
        avgNs -= System.nanoTime();
        avgMs -= System.currentTimeMillis();
        double oracleDistance = o.query(i, j);
        avgNs += System.nanoTime();
        avgMs += System.currentTimeMillis();

        // we do not use infinite distances, since it messes up the stretch
        if (oracleDistance == 1d/0d) {
          connectedPairs--;
        } else {
          double actualDistance = G.getVertex(j).getEstimate();
          double stretch = oracleDistance / actualDistance;
          if (stretch < minStretch)
            minStretch = stretch;
          else if (stretch > maxStretch)
            maxStretch = stretch;
          avgStretch += stretch;
        }
      }
    }

    // compute averages
    avgNs /= pairs; // N*(N-1)/2
    avgMs /= pairs; // N*(N-1)/2
    avgStretch /= connectedPairs; // N*(N-1)/2

    if (type == Output.REGULAR) {
      System.out.println("Benchmarking " + name);
      System.out.println("   vertices: " + G.getVertexCount());
      System.out.println("      edges: " + G.getEdgeCount());
      System.out.println("          k: " + o.getK());
      System.out.println("max stretch: " + maxStretch);
      System.out.println("min stretch: " + minStretch);
      System.out.println("avg stretch: " + avgStretch);
      System.out.printf("    avg. ns: %s\n", avgNs);
      System.out.printf("    avg. ms: %s\n", avgMs);
    } else if (type == Output.CSV) {
      System.out.printf("%s\t%d\t%d\t%d\t%f\t%f\t%f\t%d\t%d\n", name,
          G.getVertexCount(), G.getEdgeCount(), o.getK(), maxStretch,
          minStretch, avgStretch, avgNs, avgMs);
    }
  }

  public void run(Output type) {
    if (type == Output.CSV) {
      System.out.println("name\tarity\tvertices\tedges\ttimemilli\ttimenano");
    }

    // Single sources
    for (Triple<String, SingleSourceAlgorithm, Triple<Graph<V>, Integer, Integer>> bm :
        singleSourceBenchmarks) {
      this.runSingleSourceBenchmark(type, bm.getItem1(), bm.getItem2(),
          bm.getItem3());
    }

    // Single pairs
    for (Triple<String, SinglePairAlgorithm, Quad<Graph<V>, Integer, Integer,
               Integer>> bm : singlePairBenchmarks) {
      this.runSinglePairBenchmark(type, bm.getItem1(), bm.getItem2(),
          bm.getItem3());
    }

    // Oracle preprocessing
    for (Quint<String, Class<?>, Graph<V>, Integer, Integer> bm :
        oraclePreprocessBenchmarks) {
      this.runOraclePreprocessBenchmark(type, bm.getItem1(), bm.getItem2(),
          new Triple(bm.getItem3(), bm.getItem4(), bm.getItem5()));
    }

    // Oracle queries
    for (Triple<String, Oracle, Graph<V>> bm : oracleQueryBenchmarks) {
      this.runOracleQueryBenchmark(type, bm.getItem1(), bm.getItem2(), bm.getItem3());
    }
  }


}
