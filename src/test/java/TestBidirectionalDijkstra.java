import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.awt.Color;

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
    int num = 1;
    for (int i = 100; i <= 100; i += 10) {
      for (int d = 3; d < 10; d++) {
        Pair<Graph<BDDVertex>, ArrayList<Pair<Double, Double>>> planar = GraphCreator.planar(BDDVertex.class, 100, i, d);
        Graph<BDDVertex> G = planar.getItem1();
        BDDVertex s = G.getVertices().get(0);
        BDDVertex t = G.getVertices().get(1);

        Dijkstra.<BDDVertex>singleSource(G, s, 4);
        ArrayList<BDDVertex> dijkstraPath = new ArrayList();
        BDDVertex v = t;
        while (v != null) {
          dijkstraPath.add(v);
          v = (BDDVertex)v.getPredecessor();
        }
        double dijkstraDistance = t.getEstimate();

        Pair<Double, BDDVertex> result = BidirectionalDijkstra.<BDDVertex>singlePair(G, s, t, 4);

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

        // Graph dumping
        String filename;
        if (Math.abs(dijkstraDistance - result.getItem1()) > DELTA) {
          filename = "bdd_test_graph_failure_"+num+".svg";
        } else {
          filename = "bdd_test_graph_success_"+num+".svg";
        }

        GraphCreator.<BDDVertex>graphSVG(100, filename, G, planar.getItem2(),
              new GraphCreator.SVGElement("circle", null, Color.black, 1d, 2d),
              new Pair(new GraphCreator.SVGElement("circle", Color.black, Color.yellow, 1d, 3d), new ArrayList(){{add(s);}}),
              new Pair(new GraphCreator.SVGElement("circle", Color.black, Color.red, 1d, 3d), new ArrayList(){{add(t);}}),
              new Pair(new GraphCreator.SVGElement("circle", Color.blue, null, 1d, 7d), bddPath),
              new Pair(new GraphCreator.SVGElement("circle", Color.green, null, 1d, 8d), dijkstraPath),
              new Pair(new GraphCreator.SVGElement("rect", Color.yellow, null, 1d, 10d), new ArrayList(){{add(result.getItem2());}}));

        num++;
        assertEquals(dijkstraDistance, result.getItem1(), DELTA);
      }
    }
  }
}
