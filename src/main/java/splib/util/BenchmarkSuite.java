package splib.util;


import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import splib.util.Triple;
import splib.util.Quad;
import splib.data.Graph;
import splib.data.SPVertex;
import splib.algo.Oracle;
import splib.algo.Dijkstra;
import java.lang.reflect.InvocationTargetException;


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
    public ArrayList<W> sssp(Graph<W> G, W s, int heapArity);
  }

  @FunctionalInterface
  public interface SinglePairAlgorithm<W extends SPVertex> {
    public Object spsp(Graph<W> G, W s, W t, int heapArity);
  }

  private ArrayList<Triple<String, SingleSourceAlgorithm, Triple<Graph<V>, V,
          Integer>>> singleSourceBenchmarks;
  private ArrayList<Triple<String, SinglePairAlgorithm, Quad<Graph<V>, V, V,
          Integer>>> singlePairBenchmarks;
  private ArrayList<Quint<String, Class<?>, Graph<V>, Integer, Integer>>
    oraclePreprocessBenchmarks;
  private ArrayList<Triple<String, Oracle, Graph<V>>> oracleQueryBenchmarks;

  public BenchmarkSuite() {
    this.singleSourceBenchmarks = new ArrayList<Triple<String,
      SingleSourceAlgorithm, Triple<Graph<V>, V, Integer>>>();
    this.singlePairBenchmarks = new ArrayList<Triple<String,
      SinglePairAlgorithm, Quad<Graph<V>, V, V,Integer>>>();
    this.oraclePreprocessBenchmarks = new ArrayList<Quint<String, Class<?>, Graph<V>,
      Integer, Integer>>();
    this.oracleQueryBenchmarks = new ArrayList<Triple<String, Oracle, Graph<V>>>();
  }

  public void addSingleSourceBenchmark(String name, SingleSourceAlgorithm<V> algo,
      Graph<V> G, V s, int heapArity) {
    this.singleSourceBenchmarks.add(new Triple(name, algo, new Triple(G, s, heapArity)));
  }

  public void addSinglePairBenchmark(String name, SinglePairAlgorithm<V> algo,
      Graph<V> G, V s, V t, int heapArity) {
    this.singlePairBenchmarks.add(new Triple(name, algo,
          new Quad(G, s, t, heapArity)));
  }

  public <O extends Oracle> void addOraclePreprocessBenchmark(String name, Class<O> oClass, Graph<V> G, int k, int heapArity) {
    this.oraclePreprocessBenchmarks.add(new Quint(name, oClass, G, k, heapArity));
  }

  public void addOracleQueryBenchmark(String name, Oracle o, Graph<V> G) {
    this.oracleQueryBenchmarks.add(new Triple(name, o, G));
  }

  public void runSinglePairBenchmark(Output type, String name,
        SinglePairAlgorithm<V> algo, Quad<Graph<V>, V, V, Integer> args) {
      long ns = System.nanoTime();
      long ms = System.currentTimeMillis();
      algo.<V>spsp(args.getItem1(), args.getItem2(), args.getItem3(), args.getItem4());
      ns = System.nanoTime() - ns;
      ms = System.currentTimeMillis() - ms;
      if (type == Output.REGULAR) {
        System.out.println("Benchmarking " + name);
        System.out.println("heap arity: " + args.getItem4());
        System.out.println(" vertices: " + args.getItem1().getVertexCount());
        System.out.println("    edges: " + args.getItem1().getEdgeCount());
        System.out.printf("       ns: %s\n", ns);
        System.out.printf("       ms: %s\n", ms);
      } else if (type == Output.CSV) {
        System.out.printf("%s\t%d\t%d\t%d\t%d\t%d\t\n", name,
            args.getItem4(), args.getItem1().getVertexCount(),
            args.getItem1().getEdgeCount(), ms, ns);
      }
  }

  public void runSingleSourceBenchmark(Output type, String name,
        SingleSourceAlgorithm<V> algo, Triple<Graph<V>, V, Integer> args) {
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
      Constructor ctor = oClass.getConstructor(Integer.class, Graph.class, Integer.class);

      long ns = System.nanoTime();
      long ms = System.currentTimeMillis();
      Runtime runtime = Runtime.getRuntime();
      runtime.gc();
      long memory = runtime.freeMemory();
      Oracle or = (Oracle)ctor.newInstance(args.getItem2(), args.getItem1(), args.getItem3());
      memory -= runtime.freeMemory();
      ns = System.nanoTime() - ns;
      ms = System.currentTimeMillis() - ms;
      if (type == Output.REGULAR) {
        System.out.println("Benchmarking " + name);
        System.out.println("heap arity: " + args.getItem3());
        System.out.println(" vertices: " + args.getItem1().getVertexCount());
        System.out.println("    edges: " + args.getItem1().getEdgeCount());
        System.out.println("        k: " + args.getItem2());
        System.out.println("  mem (b): " + memory);
        System.out.printf("       ns: %s\n", ns);
        System.out.printf("       ms: %s\n", ms);
      } else if (type == Output.CSV) {
        System.out.printf("%s\t%d\t%d\t%d\t%d\t%d\t%d\t%d\n", name,
            args.getItem3(), args.getItem1().getVertexCount(),
            args.getItem1().getEdgeCount(), args.getItem2(), memory, ms, ns);
      }
    } catch (InvocationTargetException | IllegalAccessException |
        NoSuchMethodException | InstantiationException ex) {
      // Ignore
    }
  }

  public void runOracleQueryBenchmark(Output type, String name,
      Oracle o, Graph<V> G) {
    long avgNs = 0;
    long avgMs = 0;
    double minStretch = Double.MAX_VALUE;
    double avgStretch = 0;
    double maxStretch = 0;

    // Query all pairs of vertices
    for (int i = 0; i < G.getVertices().size(); i++) {
      for (int j = i + 1; j < G.getVertices().size(); j++) {
        V s = G.getVertices().get(i);
        V t = G.getVertices().get(j);
        avgNs -= System.nanoTime();
        avgMs -= System.currentTimeMillis();
        double oracleDistance = o.query(s, t);
        avgNs += System.nanoTime();
        avgMs += System.currentTimeMillis();
        Dijkstra.<V>singleSource(G, s, 4);
        double actualDistance = t.getEstimate();
        double stretch = oracleDistance / actualDistance;
        if (stretch < minStretch)
          minStretch = stretch;
        else if (stretch > maxStretch)
          maxStretch = stretch;
        avgStretch += stretch;
      }
    }

    // compute averages
    int pairs = (G.getVertices().size() * (G.getVertices().size() - 1)) / 2;
    avgNs /= pairs; // N*(N-1)/2
    avgMs /= pairs; // N*(N-1)/2
    avgStretch /= pairs; // N*(N-1)/2

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
    for (Triple<String, SingleSourceAlgorithm, Triple<Graph<V>, V,Integer>> bm :
        singleSourceBenchmarks) {
      this.runSingleSourceBenchmark(type, bm.getItem1(), bm.getItem2(),
          bm.getItem3());
    }

    // Single pairs
    for (Triple<String, SinglePairAlgorithm, Quad<Graph<V>, V, V,
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
