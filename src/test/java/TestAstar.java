import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

import java.util.Comparator;
import java.util.ArrayList;
import java.awt.Color;

import splib.util.GraphCreator;
import splib.util.Pair;
import splib.algo.Astar;
import splib.algo.Dijkstra;
import splib.data.Graph;
import splib.data.PlanarSPVertex;
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
        Pair<Graph<PlanarSPVertex>, ArrayList<Pair<Double, Double>>> planar = GraphCreator.planar(PlanarSPVertex.class, 100, i, d);
        Graph<PlanarSPVertex> G = planar.getItem1();
        PlanarSPVertex s = G.getVertices().get(0);
        PlanarSPVertex t = G.getVertices().get(1);

        Dijkstra.<PlanarSPVertex>singleSource(G, s, 4);
        ArrayList<PlanarSPVertex> dijkstraPath = new ArrayList();

        // Extract the path found by Dijkstra
        PlanarSPVertex v = t;
        while (v != null) {
          dijkstraPath.add(v);
          v = (PlanarSPVertex)v.getPredecessor();
        }
        dijkstraDistance = t.getEstimate();

        astarDistance = Astar.<PlanarSPVertex>singlePair(G, s, t, Astar::euclidianHeuristic, 4);
        // Generate path as list of vertices
        ArrayList<PlanarSPVertex> astarPath = new ArrayList();
        v = t;
        while (v != null) {
          astarPath.add(v);
          v = (PlanarSPVertex)v.getPredecessor();
        }

        // Graph dumping
        String filename;
        if (Math.abs(dijkstraDistance - astarDistance) > DELTA) {
          filename = "astar_test_graph_failure_"+num+".svg";
        } else {
          filename = "astar_test_graph_success_"+num+".svg";
        }

        GraphCreator.<PlanarSPVertex>graphSVG(100, filename, G, planar.getItem2(),
          new GraphCreator.SVGElement("circle", null, Color.black, 1d, 2d),
          new Pair(new GraphCreator.SVGElement("circle", Color.black, Color.yellow, 1d, 3d),
            new ArrayList(){{add(s);}}),
          new Pair(new GraphCreator.SVGElement("circle", Color.black, Color.red, 1d, 3d),
            new ArrayList(){{add(t);}}),
          new Pair(new GraphCreator.SVGElement("circle", Color.blue, null, 1d, 7d), astarPath),
          new Pair(new GraphCreator.SVGElement("circle", Color.green, null, 1d, 8d), dijkstraPath));
        num++;
        assertEquals(dijkstraDistance, astarDistance, DELTA);
      }
    }

  }


}
