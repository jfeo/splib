package splib.run;


import splib.util.BenchmarkSuite;
import splib.algo.Dijkstra;
import splib.algo.BidirectionalDijkstra;
import java.util.ArrayList;
import java.lang.Math;
import splib.util.Heap;
import splib.util.GraphCreator;
import splib.util.Pair;
import splib.util.Quad;
import splib.data.BDDVertex;
import splib.data.Graph;

public class BenchmarkBidirectionalDijkstra {


  public static void main(String[] arg) throws InstantiationException, IllegalAccessException {
    BenchmarkSuite<BDDVertex> b = new BenchmarkSuite<BDDVertex>(BDDVertex.class);
    int maxArity = 10;


    for (int i = 100; i < 4600; i += 100) {
      for (float p = 1.0f; p > 2.0f * Math.log((double)i) / (double)i; p -= 0.10f) {
        Graph<BDDVertex> G = GraphCreator.<BDDVertex>erdosrenyi(BDDVertex.class, i, p);
        for (int a = 2; a <= maxArity; a++) {
          b.runSinglePairBenchmark(BenchmarkSuite.Output.CSV,
            "Bidirectional Dijkstra, " + a + "-ary Heap", BidirectionalDijkstra::singlePair,
            new Quad(G, G.getVertices().get(0), G.getVertices().get(1) , a));
        }
      }
    }

    for (int i = 150; i < 5000; i += 150) {
      for (int d = 2; d < i; d += 40) {
        Pair<Graph<BDDVertex>, ?> G = GraphCreator.<BDDVertex>euclidian(BDDVertex.class, 100d, i, d);
        for (int a = 2; a <= maxArity; a++) {
          b.runSinglePairBenchmark(BenchmarkSuite.Output.CSV,
            "BidirectionalDijkstra " + a + "-ary Heap",
            BidirectionalDijkstra::<BDDVertex>singlePair,
            new Quad(G.getItem1(), G.getItem1().getVertices().get(0),
              G.getItem1().getVertices().get(1), a));
        }
      }
    }
  }


}
