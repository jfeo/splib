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

    BenchmarkSuite<EuclidianSPVertex> b = new BenchmarkSuite<EuclidianSPVertex>(EuclidianSPVertex.class);

    int maxHeaps = 10;

    for (int i = 5000; i > 0; i -= 500) {
      for (int d = i / 3; d > 0; d /= 3) {
        Pair<Graph<EuclidianSPVertex>, ?> G
          = GraphCreator.<EuclidianSPVertex>euclidian(EuclidianSPVertex.class,
              100d, i, d);
        for (int a = 2; a <= maxHeaps; a++) {
          // run astar on a bunch of pairs
          final EuclidianSPVertex target = G.getItem1().getVertex(1);
          b.runSinglePairBenchmark(BenchmarkSuite.Output.CSV,
            "A* " + a + "-ary Heap", (_G, _s, _t, _a) -> {return Astar.singlePair(_G, _s, _t, Astar.euclidianHeuristic(target), _a);}, new Quad(G.getItem1(), 0, 1, a));
        }
      }
    }
  }


}
