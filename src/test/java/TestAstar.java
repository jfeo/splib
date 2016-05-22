import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

import java.util.Comparator;

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
  public void test_singlePair() {
    double length;
    Graph<PlanarSPVertex> G;
    PlanarSPVertex t;
    PlanarSPVertex s;

    // Test case
    for (int i = 50; i <= 1000; i += 50) {
      for (int d = 3; d <= i / 3; d++) {
        System.out.printf("Testing A* on graph with %d vertices and a degree of %d\n", i, d);
        G = GraphCreator.planar(i, d);
        s = G.getVertices().get(0);
        t = G.getVertices().get(1);
        length = Astar.singlePair(G, s, t, Astar::euclidianHeuristic, 4);
        Dijkstra.<PlanarSPVertex>singleSource(G, s, 4);
        assertEquals(t.getEstimate(), length, DELTA);
      }
    }
  }


}
