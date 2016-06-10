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

    BenchmarkSuite<BDDVertex> b_bdd = new BenchmarkSuite<BDDVertex>(BDDVertex.class);
    BenchmarkSuite<EuclidianSPVertex> b_astar = new BenchmarkSuite<EuclidianSPVertex>(EuclidianSPVertex.class);

    int maxArity = 10;

    for (int i = 150; i < 5000; i += 150) {
      for (int d = 2; d < i / 2; d *= 3) {
        Pair<Graph<BDDVertex>, ArrayList<Pair<Double, Double>>> G_Bdd = GraphCreator.<BDDVertex>euclidian(BDDVertex.class, 100d, i, d);
        Graph<EuclidianSPVertex> G_Astar = GraphCreator.<EuclidianSPVertex,
          BDDVertex>copy(EuclidianSPVertex.class, G_Bdd.getItem1(), G_Bdd.getItem2());
        for (int a = 2; a <= maxArity; a++) {
          // run bdd on a bunch of pairs
          for (int u = 1; u < i; u += i/5) {
            b_bdd.runSinglePairBenchmark(BenchmarkSuite.Output.CSV,
              "BDD - " + (u+a),
              BidirectionalDijkstra::<BDDVertex>singlePair,
              new Quad(G_Bdd.getItem1(), G_Bdd.getItem1().getVertices().get(0),
                G_Bdd.getItem1().getVertices().get(u), a));

            EuclidianSPVertex target = G_Astar.getVertex(u);
            BenchmarkSuite.SinglePairAlgorithm<EuclidianSPVertex> astar = (Gr, s, t, arity) -> Astar.<EuclidianSPVertex>singlePair(Gr, s, t, Astar.euclidianHeuristic(target), arity);
            b_astar.runSinglePairBenchmark(BenchmarkSuite.Output.CSV,
              "A* - " + (u+a),
              astar, new Quad(G_Astar, G_Astar.getVertices().get(0),
                G_Astar.getVertices().get(u), a));
          }
        }
      }
    }
  }


}
