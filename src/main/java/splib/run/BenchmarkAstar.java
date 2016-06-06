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

    // Warm-up complete
    BenchmarkSuite<EuclidianSPVertex> b = new BenchmarkSuite<EuclidianSPVertex>();
    // b = new BenchmarkSuite<EuclidianSPVertex>();

    for (int i = 100; i < 5000; i += 100) {
      for (int d = 1; d < i; d *= 3) {
        Pair<Graph<EuclidianSPVertex>, ?> G
          = GraphCreator.<EuclidianSPVertex>euclidian(EuclidianSPVertex.class,
              100d, i, d);
        for (int a = 2; a < 10; a++) {
          b.runSinglePairBenchmark(BenchmarkSuite.Output.CSV,
              "A*", astar, new Quad(G.getItem1(),
                G.getItem1().getVertices().get(10),
                G.getItem1().getVertices().get(5), a));
        }
      }
    }
  }


}
