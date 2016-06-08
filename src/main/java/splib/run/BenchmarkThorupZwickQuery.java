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
    BenchmarkSuite<TZSPVertex> b = new BenchmarkSuite<TZSPVertex>(TZSPVertex.class);

    for (int i = 500; i < 5000; i += 500) {
      for (int d = 3; d < 15; d += 3) {
        Pair<Graph<TZSPVertex>, ?> G = GraphCreator.<TZSPVertex>euclidian(TZSPVertex.class, 100d, i, 4);
        for (int k = 3; k < 10; k++) {
          for (int a = 3; a < 10; a++) {
            ThorupZwick tz = new ThorupZwick(k, G.getItem1(), a);
            b.runOracleQueryBenchmark(BenchmarkSuite.Output.CSV,
                "Thorup Zwick Query", tz, G.getItem1());
          }
        }
      }
    }
  }


}
