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

public class BenchmarkThorupZwickQuery {


  public static void main(String[] arg) throws InstantiationException, IllegalAccessException {
    //Warm-up
    // BenchmarkSuite<TZSPVertex> b = new BenchmarkSuite<TZSPVertex>();
    // for (int i = 0; i < 1000; i++) {
    //   Graph<TZSPVertex> G = GraphCreator.complete(TZSPVertex.class, 50);
    //   ThorupZwick tz = new ThorupZwick(5, G, 3);
    //   b.addOracleQueryBenchmark("", tz, G);
    // }
    // b.run(BenchmarkSuite.Output.NONE);
    // b = null;
    // System.gc();

    // Warm-up complete
    BenchmarkSuite<TZSPVertex> b = new BenchmarkSuite<TZSPVertex>();
    // b = new BenchmarkSuite<TZSPVertex>();

    for (int i = 500; i < 5000; i += 500) {
      for (int a = 2; a < 6; a++) {
        for (int k = 3; k < 10; k++) {
          Pair<Graph<TZSPVertex>, ?> G = GraphCreator.<TZSPVertex>euclidian(TZSPVertex.class, 100d, i, 4);
          ThorupZwick tz = new ThorupZwick(k, G.getItem1(), a);
          b.runOracleQueryBenchmark(BenchmarkSuite.Output.CSV,
              "Thorup Zwick Query", tz, G.getItem1());
        }
      }
    }
  }


}
