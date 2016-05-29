import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

import java.util.Comparator;
import java.util.ArrayList;

import splib.util.GraphCreator;
import splib.util.Pair;
import splib.algo.Astar;
import splib.algo.Dijkstra;
import splib.data.Graph;
import splib.data.PlanarSPVertex;
import splib.data.Vertex;


public class TestAstar {

  public void pointprint(PlanarSPVertex v) {
    System.out.println(v.getPosition().getItem1() + ", " + v.getPosition().getItem2());
  }

  static private double DELTA = 1e-15;

  @Test
  public void test_dijkstraCompare() throws IllegalAccessException, InstantiationException {
    Graph<PlanarSPVertex> G;
    PlanarSPVertex s, t;
    double astarDistance;
    double dijkstraDistance;

    int num = 1;
    for (int i = 200; i <= 1000; i += 200) {
      for (int d = 4; d < 16; d += 4) {
        Pair<Graph<PlanarSPVertex>, ArrayList<Pair<Double, Double>>> planar = GraphCreator.planar(PlanarSPVertex.class, 100, i, d);
        G = planar.getItem1();
        s = G.getVertices().get(0);
        t = G.getVertices().get(1);

        Dijkstra.<PlanarSPVertex>singleSource(G, s, 4);
        ArrayList<PlanarSPVertex> dijkstraPath = new ArrayList();
        PlanarSPVertex v = t;
        while (v != null) {
          dijkstraPath.add(v);
          v = (PlanarSPVertex)v.getPredecessor();
        }
        dijkstraDistance = t.getEstimate();

        Dijkstra.initializeSingleSource(G, s);


        astarDistance = Astar.<PlanarSPVertex>singlePair(G, s, t, Astar::euclidianHeuristic, 4);
        // Generate path as list of vertices
        ArrayList<PlanarSPVertex> astarPath = new ArrayList();
        v = t;
        while (v != null) {
          astarPath.add(v);
          v = (PlanarSPVertex)v.getPredecessor();
        }

        if (Math.abs(dijkstraDistance - astarDistance) > DELTA) {
          GraphCreator.<PlanarSPVertex>dumpTestSvg(100, "astar_test_graph_fail_"+num+".svg", G, planar.getItem2(), s, t, astarPath, dijkstraPath, null);
        } else {
          GraphCreator.<PlanarSPVertex>dumpTestSvg(100, "astar_test_graph_success_"+num+".svg", G, planar.getItem2(), s, t, astarPath, dijkstraPath, null);
        }
        num++;
        assertEquals(dijkstraDistance, astarDistance, DELTA);
      }
    }

  }


}
