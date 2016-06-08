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
    BenchmarkSuite<SPVertex> b = new BenchmarkSuite<SPVertex>(SPVertex.class);
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
    b = new BenchmarkSuite<SPVertex>(SPVertex.class);
    int numHeaps = 10;


    for (int i = 150; i < 5000; i += 150) {
      for (int d = 2; d < i / 2; d *= 3) {
        Pair<Graph<SPVertex>, ?> G1 = GraphCreator.<SPVertex>euclidian(SPVertex.class, 100d, i, d);
        for (int a = 2; a <= numHeaps; a++) {
          b.runSingleSourceBenchmark(BenchmarkSuite.Output.CSV,
              "Dijkstra, " + a + "-ary Heap", Dijkstra::singleSource,
              new Triple(G1.getItem1(), G1.getItem1().getVertices().get(10), a));
        }
      }
    }
  }


}
