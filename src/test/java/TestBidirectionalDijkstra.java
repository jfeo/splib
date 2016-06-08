import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.awt.Color;

import splib.util.Pair;
import splib.util.GraphCreator;
import splib.util.GraphDrawer;
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
    for (int i = 100; i <= 1000; i += 100) {
      for (int d = 3; d < i / 2; d += d) {
        Pair<Graph<BDDVertex>, ArrayList<Pair<Double, Double>>> euclidian = GraphCreator.euclidian(BDDVertex.class, 100, i, d);
        Graph<BDDVertex> G = euclidian.getItem1();
        BDDVertex s = G.getVertices().get(0);

        for (int a = 2; a < 10; a++) {
          BDDVertex t = G.getVertices().get(a);
          Dijkstra.<BDDVertex>singleSource(G, s, a);
          ArrayList<BDDVertex> dijkstraPath = new ArrayList();
          BDDVertex v = t;
          while (v != null) {
            dijkstraPath.add(v);
            v = (BDDVertex)v.getPredecessor();
          }
          double dijkstraDistance = t.getEstimate();

          Pair<Double, BDDVertex> result = BidirectionalDijkstra.<BDDVertex>singlePair(G, s, t, a);

        // Generate path as list of vertices
        // ArrayList<BDDVertex> bddPath = new ArrayList();
        // v = result.getItem2();
        // while (v != null) {
        //   bddPath.add(v);
        //   v = (BDDVertex)v.getPredecessor();
        // }
        // Collections.reverse(bddPath);
        // v = result.getItem2();
        // while (v != null) {
        //   bddPath.add(v);
        //   v = v.getSuccessor();
        // }

        // Graph dumping
        // String filename;
        // if (Math.abs(dijkstraDistance - result.getItem1()) > DELTA) {
        //   dumpSVG();
        // }

        // ArrayList<BDDVertex> bddSourceRelax = new ArrayList();
        // ArrayList<BDDVertex> bddTargetRelax = new ArrayList();
        // for (BDDVertex u : G.getVertices()) {
        //   if (u.getPredecessor() != null)
        //     bddSourceRelax.add(u);
        //   if (u.getSuccessor() != null)
        //     bddTargetRelax.add(u);
        // }

          num++;
          assertEquals(dijkstraDistance, result.getItem1(), DELTA);
        }
      }
    }
  }

  public void dumpSVG() {

    // GraphDrawer.<BDDVertex>graphSVG(100, filename, G, euclidian.getItem2(),
    //       new GraphDrawer.SVGElement("circle", null, 0d, Color.black, 1d, 1d, 2d),
    //       new ArrayList<Pair<GraphDrawer.SVGElement, List<BDDVertex>>>(){{
    //         add(new Pair(new GraphDrawer.SVGElement("circle", Color.yellow, 0.5d, null, 0d, 1d, 2d), bddSourceRelax));
    //         add(new Pair(new GraphDrawer.SVGElement("circle", Color.red, 0.5d, null, 0d, 1d, 2d), bddTargetRelax));
    //         add(new Pair(new GraphDrawer.SVGElement("circle", Color.black, 1d, Color.yellow, 1d, 1d, 3d), new ArrayList(){{add(s);}}));
    //         add(new Pair(new GraphDrawer.SVGElement("circle", Color.black, 1d, Color.red, 1d, 1d, 3d), new ArrayList(){{add(t);}}));
    //         add(new Pair(new GraphDrawer.SVGElement("circle", Color.blue, 1d, null, 0d, 1d, 7d), bddPath));
    //         add(new Pair(new GraphDrawer.SVGElement("circle", Color.green, 1d, null, 0d, 1d, 8d), dijkstraPath));
    //         add(new Pair(new GraphDrawer.SVGElement("rect", Color.yellow, 1d, null, 0d, 1d, 10d), new ArrayList(){{add(result.getItem2());}}));
    //       }}, new ArrayList());

  }
}
