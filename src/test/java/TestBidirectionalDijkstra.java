import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;

import splib.util.Pair;
import splib.util.GraphCreator;
import splib.algo.BidirectionalDijkstra;
import splib.algo.Dijkstra;
import splib.data.Graph;
import splib.data.BDDVertex;
import splib.data.SPVertex;
import splib.data.Vertex;

public class TestBidirectionalDijkstra {

  static private double DELTA = 1e-8;

  @Test
  public void test_dijkstraCompare() throws IllegalAccessException, InstantiationException {
    Graph<BDDVertex> G;
    BDDVertex s, t;
    Pair<Double, BDDVertex> result;

    int num = 1;
    for (int i = 100; i <= 100; i += 10) {
      for (int d = 3; d < 10; d++) {
        Pair<Graph<BDDVertex>, ArrayList<Pair<Double, Double>>> planar = GraphCreator.planar(BDDVertex.class, 100, i, d);
        G = planar.getItem1();
        s = G.getVertices().get(0);
        t = G.getVertices().get(1);

        Dijkstra.<BDDVertex>singleSource(G, s, 4);
        ArrayList<BDDVertex> dijkstraPath = new ArrayList();
        BDDVertex v = t;
        while (v != null) {
          dijkstraPath.add(v);
          v = (BDDVertex)v.getPredecessor();
        }
        double dijkstraDistance = t.getEstimate();

        result = BidirectionalDijkstra.<BDDVertex>singlePair(G, s, t, 4);

        // Generate path as list of vertices
        ArrayList<BDDVertex> bddPath = new ArrayList();
        v = result.getItem2();
        while (v != null) {
          bddPath.add(v);
          v = (BDDVertex)v.getPredecessor();
        }
        Collections.reverse(bddPath);
        v = result.getItem2().getSuccessor();
        while (v != null) {
          bddPath.add(v);
          v = v.getSuccessor();
        }

        if (Math.abs(dijkstraDistance - result.getItem1()) > DELTA) {
          GraphCreator.<BDDVertex>dumpTestSvg(100, "bdd_test_graph_fail_"+num+".svg", G, planar.getItem2(), s, t, bddPath, dijkstraPath, result.getItem2());
        } else {
          GraphCreator.<BDDVertex>dumpTestSvg(100, "bdd_test_graph_success_"+num+".svg", G, planar.getItem2(), s, t, bddPath, dijkstraPath, result.getItem2());
        }
        num++;
        assertEquals(dijkstraDistance, result.getItem1(), DELTA);
      }
    }

  }

}
