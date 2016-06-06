package splib.run;


import splib.util.BenchmarkSuite;
import splib.algo.ThorupZwick;
import java.util.ArrayList;
import java.lang.Math;
import splib.util.Heap;
import splib.util.GraphCreator;
import splib.util.Pair;
import splib.util.Triple;
import splib.util.Quad;
import splib.data.TZSPVertex;
import splib.data.Graph;

public class BenchmarkThorupZwick {


  public static void main(String[] arg) throws InstantiationException, IllegalAccessException {
    //Warm-up
    BenchmarkSuite<TZSPVertex> b = new BenchmarkSuite<TZSPVertex>();
    for (int i = 0; i < 1000; i++) {
      Graph<TZSPVertex> G = GraphCreator.complete(TZSPVertex.class, 50);
      b.addOraclePreprocessBenchmark("", ThorupZwick.class, G, 5, 3);
    }
    b.run(BenchmarkSuite.Output.NONE);
    b = null;
    System.gc();

    // Warm-up complete
    // BenchmarkSuite<TZSPVertex> b = new BenchmarkSuite<TZSPVertex>();
    b = new BenchmarkSuite<TZSPVertex>();

    for (int i = 500; i < 5000; i += 500) {
      for (int a = 2; a < 6; a++) {
        for (int k = 3; k < 10; k++) {
          Pair<Graph<TZSPVertex>, ?> G1 = GraphCreator.<TZSPVertex>euclidian(TZSPVertex.class, 100d, i, 4);
          b.runOraclePreprocessBenchmark(BenchmarkSuite.Output.CSV,
              "Thorup Zwick Distance Oracle", ThorupZwick.class,
              new Triple(G1.getItem1(), k, a));
        }
      }
    }
  }


}
