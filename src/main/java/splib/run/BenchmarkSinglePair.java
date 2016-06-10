package splib.run;


import splib.util.BenchmarkSuite;
import splib.algo.BidirectionalDijkstra;
import splib.algo.Astar;
import java.util.ArrayList;
import java.lang.Math;
import splib.util.Heap;
import splib.util.GraphCreator;
import splib.util.Pair;
import splib.util.Quad;
import splib.data.BDDVertex;
import splib.data.EuclidianSPVertex;
import splib.data.Graph;

public class BenchmarkSinglePair {


  public static void main(String[] arg) throws InstantiationException, IllegalAccessException {

    BenchmarkSuite<EuclidianSPVertex> ab =  new BenchmarkSuite<EuclidianSPVertex>(EuclidianSPVertex.class);
    BenchmarkSuite<BDDVertex> bb =  new BenchmarkSuite<BDDVertex>(BDDVertex.class);

    int maxArity = 10;

    for (int i = 5000; i > 0; i -= 500) {
      for (int d = i / 3; d > 0; d /= 3) {
        Pair<Graph<EuclidianSPVertex>, ArrayList<Pair<Double, Double>>> euclidian = GraphCreator.euclidian(EuclidianSPVertex.class, 100d, i, d);
        Graph<EuclidianSPVertex> G_astar =  euclidian.getItem1();
        Graph<BDDVertex> G_bdd = GraphCreator.<BDDVertex, EuclidianSPVertex>copy(BDDVertex.class, G_astar, euclidian.getItem2());

        for (int a = 2; a < maxArity; a++) {
          for (int t = 1; t <= 10; t++) {
            final EuclidianSPVertex target = G_astar.getVertex(t);
            ab.runSinglePairBenchmark(BenchmarkSuite.Output.CSV, "A*  (" + t + ")", (_G, _s, _t, _a) -> {return Astar.singlePair(_G, _s, _t, Astar.euclidianHeuristic(target), _a);}, new Quad(G_astar, 0, t, a));
            bb.runSinglePairBenchmark(BenchmarkSuite.Output.CSV, "BDD (" + t + ")", BidirectionalDijkstra::singlePair, new Quad(G_bdd, 0, t, a));
          }
        }
      }
    }


  }


}
