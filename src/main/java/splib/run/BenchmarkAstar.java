package splib.run;


import splib.util.BenchmarkSuite;
import splib.algo.Dijkstra;
import splib.algo.Astar;
import java.util.ArrayList;
import java.lang.Math;
import splib.util.Heap;
import splib.util.GraphCreator;
import splib.util.Pair;
import splib.util.Quad;
import splib.data.EuclidianSPVertex;
import splib.data.Graph;

public class BenchmarkAstar {


  public static void main(String[] arg) throws InstantiationException,
         IllegalAccessException {

    BenchmarkSuite.SinglePairAlgorithm<EuclidianSPVertex> astar = (Gr, vs, vt, a) -> Astar.<EuclidianSPVertex>singlePair(Gr, vs, vt, Astar::euclidianHeuristic, a);

    BenchmarkSuite<EuclidianSPVertex> b = new BenchmarkSuite<EuclidianSPVertex>(EuclidianSPVertex.class);

    int maxHeaps = 10;

    int numBms = (5000-150)/50 * 10;
    int curBm = 0;
    for (int i = 50; i < 5000; i += 50) {
      for (int d = 2; d < i; d += i/10) {
        curBm++;
        Pair<Graph<EuclidianSPVertex>, ?> G
          = GraphCreator.<EuclidianSPVertex>euclidian(EuclidianSPVertex.class,
              100d, i, d);
        for (int a = 2; a <= maxHeaps; a++) {
          // run astar on a bunch of pairs
          b.runSinglePairBenchmark(BenchmarkSuite.Output.CSV,
            "A* " + a + "-ary Heap", astar, new Quad(G.getItem1(),
              G.getItem1().getVertices().get(0),
              G.getItem1().getVertices().get(1), a));
        }
        System.err.println(curBm + "/" + numBms);
      }
    }
  }


}
