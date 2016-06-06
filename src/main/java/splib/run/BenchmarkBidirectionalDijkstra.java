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
    //Warm-up
    // BenchmarkSuite<BDDVertex> b = new BenchmarkSuite<BDDVertex>();
    // for (int i = 0; i < 10000; i++) {
    //   Graph<BDDVertex> G = GraphCreator.<BDDVertex>complete(BDDVertex.class, 50);
    //   BDDVertex s = (BDDVertex)G.getVertices().get(10);
    //   BDDVertex t = (BDDVertex)G.getVertices().get(5);
    //   BidirectionalDijkstra.<BDDVertex>singlePair(G, s, t, 4);
    //   b.runSinglePairBenchmark(BenchmarkSuite.Output.CSV, "",
    //       BidirectionalDijkstra::<BDDVertex>singlePair,
    //       new Quad(G, s, t, 3));
    // }
    // b.run(BenchmarkSuite.Output.NONE);
    // b = null;
    // System.gc();

    // Warm-up complete
    BenchmarkSuite<BDDVertex> b = new BenchmarkSuite<BDDVertex>();
    // b = new BenchmarkSuite<BDDVertex>();

    for (int i = 500; i < 5000; i += 500) {
      Pair<Graph<BDDVertex>, ?> G = GraphCreator.<BDDVertex>euclidian(BDDVertex.class, 100d, i, 4);
      for (int a = 2; a < 6; a++) {
        b.runSinglePairBenchmark(BenchmarkSuite.Output.CSV,
            "Bidirectional Dijkstra, " + a + "-ary Heap", BidirectionalDijkstra::singlePair,
            new Quad(G.getItem1(), G.getItem1().getVertices().get(10), G.getItem1().getVertices().get(5) , a));
      }
    }

    for (int i = 500; i < 5000; i += 500) {
      Pair<Graph<BDDVertex>, ?> G = GraphCreator.<BDDVertex>euclidian(BDDVertex.class, 100d, i, 4);
      for (int a = 2; a < 6; a++) {
        b.runSinglePairBenchmark(BenchmarkSuite.Output.CSV,
            "Bidirectional Dijkstra, " + a + "-ary Heap", BidirectionalDijkstra::singlePair,
            new Quad(G.getItem1(), G.getItem1().getVertices().get(0), G.getItem1().getVertices().get(1), a));
      }
    }

    for (int i = 100; i < 4600; i += 100) {
      for (float p = 1.0f; p > 2.0f * Math.log((double)i) / (double)i; p -= 0.01f) {
        for (int a = 2; a < 6; a++) {
          Graph<BDDVertex> G = GraphCreator.<BDDVertex>erdosrenyi(BDDVertex.class, i, p);
          b.runSinglePairBenchmark(BenchmarkSuite.Output.CSV,
              "Bidirectional Dijkstra, " + a + "-ary Heap", BidirectionalDijkstra::singlePair,
              new Quad(G, G.getVertices().get(0), G.getVertices().get(1), a));
          }
      }
    }
  }


}
