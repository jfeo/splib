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

    BenchmarkSuite<EuclidianSPVertex> b = new BenchmarkSuite<EuclidianSPVertex>();

    int maxHeaps = 10;

    for (int i = 150; i < 5000; i += 100) {
      for (int d = 2; d < i; d += 70) {
        Pair<Graph<EuclidianSPVertex>, ?> G
          = GraphCreator.<EuclidianSPVertex>euclidian(EuclidianSPVertex.class,
              100d, i, d);
        for (int a = 2; a <= maxHeaps; a++) {
          // run astar on a bunch of pairs
          for (int u = 1; u < i; u += i/5) {
            b.runSinglePairBenchmark(BenchmarkSuite.Output.CSV,
              "A* " + a + "-ary Heap", astar, new Quad(G.getItem1(),
                G.getItem1().getVertices().get(0),
                G.getItem1().getVertices().get(u), a));
          }
        }
      }
    }
  }


}
