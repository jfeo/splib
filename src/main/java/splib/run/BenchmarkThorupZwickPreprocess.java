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

public class BenchmarkThorupZwickPreprocess {


  public static void main(String[] arg) throws InstantiationException, IllegalAccessException {
    //Warm-up
    BenchmarkSuite<TZSPVertex> b = new BenchmarkSuite<TZSPVertex>(TZSPVertex.class);
    for (int i = 0; i < 100; i++) {
      Graph<TZSPVertex> G = GraphCreator.complete(TZSPVertex.class, 50);
      b.addOraclePreprocessBenchmark("", ThorupZwick.class, G, 5, 3);
    }
    b.run(BenchmarkSuite.Output.NONE);
    b = null;
    System.gc();

    // Warm-up complete
    // BenchmarkSuite<TZSPVertex> b = new BenchmarkSuite<TZSPVertex>(TZSPVertex.class);
    b = new BenchmarkSuite<TZSPVertex>(TZSPVertex.class);

    int maxArity = 10;
    int maxK = 128;

    for (int i = 5000; i > 0; i -= 250) {
      for (int d = 2; d < i / 3; d *= 3) {
        Pair<Graph<TZSPVertex>, ?> G1 = GraphCreator.<TZSPVertex>euclidian(TZSPVertex.class, 100d, i, d);
        for (int a = 2; a <= maxArity; a++) {
          for (int k = 2; k <= maxK; k *= 2) {
            b.runOraclePreprocessBenchmark(BenchmarkSuite.Output.CSV,
                "Thorup Zwick Distance Oracle", ThorupZwick.class,
                new Triple(G1.getItem1(), k, a));
          }
        }
      }
    }
  }


}
