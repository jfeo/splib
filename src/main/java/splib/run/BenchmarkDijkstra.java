package splib.run;


import splib.util.BenchmarkSuite;
import splib.algo.Dijkstra;
import java.util.ArrayList;
import java.lang.Math;
import splib.util.MinThreeHeap;
import splib.util.MinBinaryHeap;
import splib.util.Heap;
import splib.util.GraphCreator;
import splib.data.SPVertex;
import splib.data.Graph;
import splib.util.ForwardComparator;

public class BenchmarkDijkstra {


  public static void main(String[] arg) {
    ForwardComparator fCompare = new ForwardComparator();

    // Warm-up
    // System.out.println("Warming up for Benchmarking...");
    BenchmarkSuite b = new BenchmarkSuite();
    for (int i = 0; i < 10000; i++) {
      Graph<SPVertex> G = GraphCreator.complete(50);
      b.addSingleSourceBenchmark("", Dijkstra::singleSource, new MinBinaryHeap(fCompare),
          G, G.getVertices().get(10));
      b.addSingleSourceBenchmark("", Dijkstra::singleSource, new MinThreeHeap(fCompare),
          G, G.getVertices().get(10));
    }
    b.run(BenchmarkSuite.Output.NONE);
    b = null;
    System.gc();
    // System.out.println("Warm-up complete.");


    b = new BenchmarkSuite();

    for (int i = 500; i < 5000; i += 500) {
      Graph<SPVertex> G1 = GraphCreator.complete(i);
      b.runSingleSourceBenchmark(BenchmarkSuite.Output.CSV,
          "Dijkstra, Binary Heap", Dijkstra::singleSource, new MinBinaryHeap(fCompare),
          G1, G1.getVertices().get(10), false);
    }

    for (int i = 500; i < 5000; i += 500) {
      Graph<SPVertex> G2 = GraphCreator.complete(i);
      b.runSingleSourceBenchmark(BenchmarkSuite.Output.CSV,
          "Dijkstra, Three Heap", Dijkstra::singleSource, new MinThreeHeap(fCompare), G2,
          G2.getVertices().get(0), false);
    }

    for (int i = 500; i < 5000; i += 500) {
      Graph<SPVertex> G1 = GraphCreator.erdosrenyi(i, 0.01381551055f);
      b.runSingleSourceBenchmark(BenchmarkSuite.Output.CSV,
          "Dijkstra, Binary Heap", Dijkstra::singleSource, new MinBinaryHeap(fCompare), G1,
          G1.getVertices().get(0), false);
    }

    for (int i = 500; i < 5000; i += 500) {
      Graph<SPVertex> G2 = GraphCreator.erdosrenyi(i, 0.01381551055f);
      b.runSingleSourceBenchmark(BenchmarkSuite.Output.CSV,
          "Dijkstra, Three Heap", Dijkstra::singleSource, new MinThreeHeap(fCompare), G2,
          G2.getVertices().get(0), false);
    }

    for (int i = 100; i < 4600; i += 100) {
      for (float p = 1.0f; p > 2.0f * Math.log((double)i) / (double)i; p -= 0.01f) {
        Graph<SPVertex> G1 = GraphCreator.erdosrenyi(i, p);
        b.runSingleSourceBenchmark(BenchmarkSuite.Output.CSV,
            "Dijkstra, Binary Heap", Dijkstra::singleSource, new MinBinaryHeap(fCompare), G1,
            G1.getVertices().get(0), false);
      }
    }

    for (int i = 100; i < 4600; i += 100) {
      for (float p = 1.0f; p > 2.0f * Math.log((double)i) / (double)i; p -= 0.01f) {
        Graph<SPVertex> G2 = GraphCreator.erdosrenyi(i, p);
        b.runSingleSourceBenchmark(BenchmarkSuite.Output.CSV,
            "Dijkstra, Three Heap", Dijkstra::singleSource, new MinThreeHeap(fCompare), G2,
            G2.getVertices().get(0), false);
      }
    }
  }


}
