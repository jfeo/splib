import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

import splib.util.GraphCreator;
import splib.util.GraphDrawer;
import splib.util.Pair;
import splib.algo.Astar;
import splib.algo.Dijkstra;
import splib.data.Graph;
import splib.data.EuclidianSPVertex;
import splib.data.Vertex;


public class TestAstar {

  static private double DELTA = 1e-15;

  @Test
  public void test_dijkstraCompare() throws IllegalAccessException, InstantiationException {
    double astarDistance;
    double dijkstraDistance;

    int num = 1;
    for (int i = 200; i <= 1000; i += 200) {
      for (int d = 4; d < 16; d += 4) {
        Pair<Graph<EuclidianSPVertex>, ArrayList<Pair<Double, Double>>> euclidian = GraphCreator.euclidian(EuclidianSPVertex.class, 100, i, d);
        Graph<EuclidianSPVertex> G = euclidian.getItem1();
        EuclidianSPVertex s = G.getVertices().get(0);
        EuclidianSPVertex t = G.getVertices().get(1);

        Dijkstra.<EuclidianSPVertex>singleSource(G, s, 4);
        ArrayList<EuclidianSPVertex> dijkstraPath = new ArrayList();

        // Extract the path found by Dijkstra
        EuclidianSPVertex v = t;
        while (v != null) {
          dijkstraPath.add(v);
          v = (EuclidianSPVertex)v.getPredecessor();
        }
        dijkstraDistance = t.getEstimate();

        astarDistance = Astar.<EuclidianSPVertex>singlePair(G, s, t, Astar::euclidianHeuristic, 4);
        // Generate path as list of vertices
        ArrayList<EuclidianSPVertex> astarPath = new ArrayList();
        v = t;
        while (v != null) {
          astarPath.add(v);
          v = (EuclidianSPVertex)v.getPredecessor();
        }
        ArrayList<EuclidianSPVertex> astarRelax = new ArrayList();
        for (EuclidianSPVertex u : G.getVertices()) {
          if (u.getPredecessor() != null) {
            astarRelax.add(u);
          }
        }


        // Graph dumping
        String filename;
        if (Math.abs(dijkstraDistance - astarDistance) > DELTA) {
          dumpSVG();
        }

        num++;
        assertEquals(dijkstraDistance, astarDistance, DELTA);
      }
    }

  }

  public void dumpSVG() {
    // GraphDrawer.<EuclidianSPVertex>graphSVG(100, filename, G, euclidian.getItem2(),
    //   new GraphDrawer.SVGElement("circle", null, 0d, Color.black, 1d, 1d, 2d),
    //   new ArrayList<Pair<GraphDrawer.SVGElement, List<EuclidianSPVertex>>>(){{
    //     add(new Pair(new GraphDrawer.SVGElement("circle", null, 0d, Color.white, 1d, 1d, 1d), astarRelax));
    //     add(new Pair(new GraphDrawer.SVGElement("circle", Color.black, 1d, Color.yellow, 1d, 1d, 3d),
    //       new ArrayList<EuclidianSPVertex>(){{add(s);}}));
    //     add(new Pair(new GraphDrawer.SVGElement("circle", Color.black, 1d, Color.red, 1d, 1d, 3d),
    //       new ArrayList<EuclidianSPVertex>(){{add(t);}}));
    //     add(new Pair(new GraphDrawer.SVGElement("circle", Color.blue, 1d, null, 0d, 1d, 7d), astarPath));
    //     add(new Pair(new GraphDrawer.SVGElement("circle", Color.green, 1d, null, 0d, 1d, 8d), dijkstraPath));
    //   }},
    //   new ArrayList());
  }

}
