package splib.run;


import splib.util.BenchmarkSuite;
import splib.algo.Dijkstra;
import java.util.ArrayList;
import java.lang.Math;
import splib.util.Heap;
import splib.util.GraphCreator;
import splib.util.Pair;
import splib.util.Triple;
import splib.util.Quad;
import splib.data.SPVertex;
import splib.data.Graph;

public class BenchmarkDijkstra {


  public static void main(String[] arg) throws InstantiationException, IllegalAccessException {
    //Warm-up
    BenchmarkSuite<SPVertex> b = new BenchmarkSuite<SPVertex>();
    for (int i = 0; i < 10000; i++) {
      Graph<SPVertex> G = GraphCreator.complete(SPVertex.class, 50);
      b.addSingleSourceBenchmark("", Dijkstra::singleSource, G,
          G.getVertices().get(10), 3);
    }
    b.run(BenchmarkSuite.Output.NONE);
    b = null;
    System.gc();

    // Warm-up complete
    // BenchmarkSuite<SPVertex> b = new BenchmarkSuite<SPVertex>();
    b = new BenchmarkSuite<SPVertex>();
    int numHeaps = 10;

    for (int i = 500; i < 5000; i += 500) {
      for (int d = 2; d < 10; d++) {
        Pair<Graph<SPVertex>, ?> G1 = GraphCreator.<SPVertex>euclidian(SPVertex.class, 100d, i, d);
        for (int a = 2; a <= numHeaps; a++) {
          b.runSingleSourceBenchmark(BenchmarkSuite.Output.CSV,
              "Dijkstra, " + a + "-ary Heap", Dijkstra::singleSource,
              new Triple(G1.getItem1(), G1.getItem1().getVertices().get(10), a));
        }
      }
    }

    for (int i = 100; i < 4600; i += 100) {
      for (float p = 1.0f; p > 2.0f * Math.log((double)i) / (double)i; p -= 0.05f) {
        Graph<SPVertex> G = GraphCreator.<SPVertex>erdosrenyi(SPVertex.class, i, p);
        for (int a = 2; a <= numHeaps; a++) {
          b.runSingleSourceBenchmark(BenchmarkSuite.Output.CSV,
              "Dijkstra, " + a + "-ary Heap", Dijkstra::singleSource,
              new Triple(G, G.getVertices().get(0), a));
          }
      }
    }
  }


}
