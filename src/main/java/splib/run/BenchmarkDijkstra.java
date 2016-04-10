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
    // Warm-up
    // System.out.println("Warming up for Benchmarking...");
    BenchmarkSuite b = new BenchmarkSuite();
    for (int i = 0; i < 10000; i++) {
      Graph<SPVertex> G = GraphCreator.complete(50);
      b.addSingleSourceBenchmark("", Dijkstra::singleSource, new MinBinaryHeap(),
          G, G.getVertices().get(10));
      b.addSingleSourceBenchmark("", Dijkstra::singleSource, new MinThreeHeap(),
          G, G.getVertices().get(10));
    }
    b.run(BenchmarkSuite.Output.NONE);
    b = null;
    System.gc();
    // System.out.println("Warm-up complete.");


    b = new BenchmarkSuite();

    for (int i = 500; i <= 5000; i += 500) {
      Graph<SPVertex> G1 = GraphCreator.complete(i);
      b.runSingleSourceBenchmark(BenchmarkSuite.Output.CSV,
          "Dijkstra, Binary Heap", Dijkstra::singleSource, new MinBinaryHeap(),
          G1, G1.getVertices().get(10), false);
    }
    for (int i = 500; i <= 5000; i += 50) {
      Graph<SPVertex> G2 = GraphCreator.complete(i);
      b.runSingleSourceBenchmark(BenchmarkSuite.Output.CSV,
          "Dijkstra, Three Heap", Dijkstra::singleSource, new MinThreeHeap(), G2,
          G2.getVertices().get(0), false);
    }

    for (int i = 500; i <= 5000; i += 500) {
      Graph<SPVertex> G1 = GraphCreator.erdosrenyi(i, 0.01381551055f);
      b.runSingleSourceBenchmark(BenchmarkSuite.Output.CSV,
          "Dijkstra, Binary Heap", Dijkstra::singleSource, new MinBinaryHeap(), G1,
          G1.getVertices().get(0), false);
    }

    for (int i = 500; i <= 5000; i += 500) {
      Graph<SPVertex> G2 = GraphCreator.erdosrenyi(i, 0.01381551055f);
      b.runSingleSourceBenchmark(BenchmarkSuite.Output.CSV,
          "Dijkstra, Three Heap", Dijkstra::singleSource, new MinThreeHeap(), G2,
          G2.getVertices().get(0), false);
    }

    for (int i = 100; i <= 4000; i += 100) {
      for (float p = 1.0f; p >= 0.0f; p -= 0.01f) {
        Graph<SPVertex> G1 = GraphCreator.erdosrenyi(i, p);
        b.runSingleSourceBenchmark(BenchmarkSuite.Output.CSV,
            "Dijkstra, Binary Heap", Dijkstra::singleSource, new MinBinaryHeap(), G1,
            G1.getVertices().get(0), false);
      }
    }

    for (int i = 100; i <= 4000; i += 100) {
      for (float p = 0.8f; p >= 0.0f; p -= 0.01f) {
        Graph<SPVertex> G2 = GraphCreator.erdosrenyi(i, p);
        b.runSingleSourceBenchmark(BenchmarkSuite.Output.CSV,
            "Dijkstra, Three Heap", Dijkstra::singleSource, new MinThreeHeap(), G2,
            G2.getVertices().get(0), false);
      }
    }
  }


}
