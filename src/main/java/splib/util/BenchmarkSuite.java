package splib.util;


import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import splib.util.Triple;
import splib.util.Quad;
import splib.data.Graph;
import splib.data.SPVertex;
import splib.algo.Oracle;
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

  public BenchmarkSuite() {
    this.singleSourceBenchmarks = new ArrayList<Triple<String,
      SingleSourceAlgorithm, Triple<Graph<V>, V, Integer>>>();
    this.singlePairBenchmarks = new ArrayList<Triple<String,
      SinglePairAlgorithm, Quad<Graph<V>, V, V,Integer>>>();

    // this.oraclePreprocessBenchmarks = ArrayList<Quint<String, Class, Graph<V>, Integer,
    //   Integer>>();
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
      Oracle or = (Oracle)ctor.newInstance(args.getItem2(), args.getItem1(), args.getItem3());
      ns = System.nanoTime() - ns;
      ms = System.currentTimeMillis() - ms;
      if (type == Output.REGULAR) {
        System.out.println("Benchmarking " + name);
        System.out.println("heap arity: " + args.getItem3());
        System.out.println(" vertices: " + args.getItem1().getVertexCount());
        System.out.println("    edges: " + args.getItem1().getEdgeCount());
        System.out.println("        k: " + args.getItem2());
        System.out.printf("       ns: %s\n", ns);
        System.out.printf("       ms: %s\n", ms);
      } else if (type == Output.CSV) {
        System.out.printf("%s\t%d\t%d\t%d\t%d\t%d\t%d\n", name,
            args.getItem3(), args.getItem1().getVertexCount(),
            args.getItem1().getEdgeCount(), args.getItem2(), ms, ns);
      }
    } catch (InvocationTargetException | IllegalAccessException |
        NoSuchMethodException | InstantiationException ex) {
      // Ignore
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

    for (Quint<String, Class<?>, Graph<V>, Integer, Integer> bm :
        oraclePreprocessBenchmarks) {
      this.runOraclePreprocessBenchmark(type, bm.getItem1(), bm.getItem2(),
          new Triple(bm.getItem3(), bm.getItem4(), bm.getItem5()));
    }
  }


}
