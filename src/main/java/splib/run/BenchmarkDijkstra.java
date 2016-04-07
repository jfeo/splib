package splib.run;


import splib.util.BenchmarkSuite;
import splib.algo.Dijkstra;
import java.util.ArrayList;
import splib.util.MinThreeHeap;
import splib.util.MinBinaryHeap;
import splib.util.Heap;
import splib.util.GraphCreator;
import splib.data.SPVertex;
import splib.data.Graph;

public class BenchmarkDijkstra {


  public static void main(String[] arg) {
    Heap<SPVertex> h = new MinBinaryHeap<SPVertex>();

    // Warm-up
    System.out.println("Warming up for Benchmarking...");
    BenchmarkSuite b = new BenchmarkSuite();
    for (int i = 0; i < 10000; i++) {
      Graph<SPVertex> G = GraphCreator.complete(50);
      b.addSingleSourceBenchmark("", Dijkstra::singleSource,
          new MinBinaryHeap<SPVertex>(), G, G.getVertices().get(10));
      b.addSingleSourceBenchmark("", Dijkstra::singleSource,
          new MinThreeHeap<SPVertex>(), G, G.getVertices().get(10));
    }
    b.run(BenchmarkSuite.Output.NONE);
    b = null;
    System.gc();
    System.out.println("Warm-up complete.");


    b = new BenchmarkSuite();

    for (int i = 100; i < 1000; i += 50) {
      Graph<SPVertex> G1 = GraphCreator.complete(i);
      b.addSingleSourceBenchmark("Dijkstra, Binary Heap", Dijkstra::singleSource,
          new MinBinaryHeap<SPVertex>(), G1, G1.getVertices().get(10));

      Graph<SPVertex> G2 = GraphCreator.complete(i);
      b.addSingleSourceBenchmark("Dijkstra, Three Heap", Dijkstra::singleSource,
          new MinThreeHeap<SPVertex>(), G2, G2.getVertices().get(0));
    }

    for (int i = 1000; i < 4000; i += 500) {
      Graph<SPVertex> G1 = GraphCreator.erdosrenyi(i, 0.01381551055f);
      b.addSingleSourceBenchmark("Dijkstra, Binary Heap", Dijkstra::singleSource,
          new MinBinaryHeap<SPVertex>(), G1, G1.getVertices().get(0));

      Graph<SPVertex> G2 = GraphCreator.erdosrenyi(i, 0.01381551055f);
      b.addSingleSourceBenchmark("Dijkstra, Three Heap", Dijkstra::singleSource,
          new MinThreeHeap<SPVertex>(), G2, G2.getVertices().get(0));
    }

    b.run(BenchmarkSuite.Output.REGULAR);
  }


}
