package splib.util;


import java.lang.reflect.Method;
import java.util.ArrayList;
import splib.util.Quint;
import splib.util.Heap;
import splib.data.Graph;
import splib.data.SPVertex;


/*
 * A class to perform a benchmark of methods, specifically in regard to their
 * time complexity.
 */
public class BenchmarkSuite {

  public enum Output {
    JSON,
    REGULAR,
    NONE
  }

  @FunctionalInterface
  public interface SingleSourceAlgorithm {
    public ArrayList<SPVertex> sssp(Heap<SPVertex> h, Graph<SPVertex> G, SPVertex s);
  }

  private ArrayList<Quint<String, SingleSourceAlgorithm, Heap<SPVertex>, Graph<SPVertex>, SPVertex>> singleSourceBenchmarks;

  public BenchmarkSuite() {
    this.singleSourceBenchmarks = new ArrayList<Quint<String, SingleSourceAlgorithm, Heap<SPVertex>, Graph<SPVertex>, SPVertex>>();
  }

  public void addSingleSourceBenchmark(String name, SingleSourceAlgorithm algo, Heap<SPVertex> h, Graph<SPVertex> G, SPVertex s) {
    this.singleSourceBenchmarks.add(new Quint(name, algo, h, G, s));
  }

  public void run(Output type) {
    long ns, ms;

    if (type == Output.JSON) {
      System.out.printf("[");
    }

    // Single sources
    boolean comma = false;
    for (Quint<String, SingleSourceAlgorithm, Heap<SPVertex>, Graph<SPVertex>, SPVertex> ssBm : singleSourceBenchmarks) {
      ns = System.nanoTime();
      ms = System.currentTimeMillis();
      ssBm.getItem2().sssp(ssBm.getItem3(), ssBm.getItem4(), ssBm.getItem5());
      ns = System.nanoTime() - ns;
      ms = System.currentTimeMillis() - ms;

      if (type == Output.JSON) {
        if (!comma) {
          comma = true;
        } else {
          System.out.print(",");
        }
        System.out.printf("{\'name\':\'%s\', \'vcount\':%d, \'ecount\':%d, \'ns\':%d, \'ms\':%d}",
            ssBm.getItem1(), ssBm.getItem4().getVertexCount(), ssBm.getItem4().getEdgeCount(), ns, ms);
      } else if (type == Output.REGULAR) {
        System.out.println("Benchmarking " + ssBm.getItem1());
        System.out.println(" vertices: " + ssBm.getItem4().getVertexCount());
        System.out.println("    edges: " + ssBm.getItem4().getEdgeCount());
        System.out.printf("       ns: %s\n", ns);
        System.out.printf("       ms: %s\n", ms);
      }



    }

    if (type == Output.JSON) {
      System.out.print("]");
    }

  }

}
