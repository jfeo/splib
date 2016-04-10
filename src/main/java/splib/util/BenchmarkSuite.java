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
    CSV,
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

  public void runSingleSourceBenchmark(Output type, Quint<String, SingleSourceAlgorithm, Heap<SPVertex>, Graph<SPVertex>, SPVertex> ssBm, Boolean comma) {
    this.runSingleSourceBenchmark(type, ssBm.getItem1(), ssBm.getItem2(), ssBm.getItem3(), ssBm.getItem4(), ssBm.getItem5(), comma);
  }

  public void runSingleSourceBenchmark(Output type, String name, SingleSourceAlgorithm algo, Heap<SPVertex> h, Graph<SPVertex> G, SPVertex s, Boolean comma) {
      long ns = System.nanoTime();
      long ms = System.currentTimeMillis();
      algo.sssp(h, G, s);
      ns = System.nanoTime() - ns;
      ms = System.currentTimeMillis() - ms;

      if (type == Output.JSON) {
        if (!comma) {
          comma = true;
        } else {
          System.out.print(",");
        }
        System.out.printf("{\'name\':\'%s\', \'vcount\':%d, \'ecount\':%d, \'ns\':%d, \'ms\':%d}",
            name, G.getVertexCount(), G.getEdgeCount(), ns, ms);
      } else if (type == Output.REGULAR) {
        System.out.println("Benchmarking " + name);
        System.out.println(" vertices: " + G.getVertexCount());
        System.out.println("    edges: " + G.getEdgeCount());
        System.out.printf("       ns: %s\n", ns);
        System.out.printf("       ms: %s\n", ms);
      } else if (type == Output.CSV) {
        System.out.printf("%s\t%d\t%d\t%d\t%d\t\n", name, G.getVertexCount(), G.getEdgeCount(), ns, ms);
      }
  }

  public void run(Output type) {
    if (type == Output.JSON) {
      System.out.printf("[");
    } else if (type == Output.CSV) {
      System.out.println("name\tvertices\tedges\ttimenano\ttimemilli");
    }

    // Single sources
    Boolean comma = false;
    for (Quint<String, SingleSourceAlgorithm, Heap<SPVertex>, Graph<SPVertex>, SPVertex> ssBm : singleSourceBenchmarks) {
      this.runSingleSourceBenchmark(type, ssBm, comma);
    }

    if (type == Output.JSON) {
      System.out.print("]");
    }
  }


}
