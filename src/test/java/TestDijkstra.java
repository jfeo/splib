import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import splib.util.Pair;
import splib.algo.Dijkstra;
import splib.data.Graph;
import splib.data.SPVertex;
import splib.data.Vertex;

public class TestDijkstra {

  static private double DELTA = 1e-15;

  @Test
  public void test_singleSource() {
    Graph G = new Graph<SPVertex>();

    SPVertex v1 = new SPVertex();
    G.addVertex(v1);
    SPVertex v2 = new SPVertex();
    G.addVertex(v2);
    SPVertex v3 = new SPVertex();
    G.addVertex(v3);
    SPVertex v4 = new SPVertex();
    G.addVertex(v4);
    SPVertex v5 = new SPVertex();
    G.addVertex(v5);

    G.addEdge(0, 1, 3.0);
    G.addEdge(0, 4, 10.0);
    G.addEdge(1, 4, 5.0);
    G.addEdge(1, 3, 4.0);
    G.addEdge(1, 2, 1.0);
    G.addEdge(2, 3, 2.0);
    G.addEdge(3, 4, 1.0);

    Dijkstra.singleSource(G, 0, 4);

    assertNull("Failure - v1 has a predecessor.", v1.getPredecessor());
    assertEquals("Failure - v1 is not predecessor of v2", v1, G.getVertex(v2.getPredecessor()));
    assertEquals("Failure - v2 is not predecessor of v3", v2, G.getVertex(v3.getPredecessor()));
    assertEquals("Failure - v3 is not predecessor of v4", v3, G.getVertex(v4.getPredecessor()));
    assertEquals("Failure - v4 is not predecessor of v5", v4, G.getVertex(v5.getPredecessor()));
    assertEquals("Failure - distance v1 is wrong", v1.getEstimate(), 0.0, DELTA);
    assertEquals("Failure - distance v2 is wrong", v2.getEstimate(), 3.0, DELTA);
    assertEquals("Failure - distance v3 is wrong", v3.getEstimate(), 4.0, DELTA);
    assertEquals("Failure - distance v4 is wrong", v4.getEstimate(), 6.0, DELTA);
    assertEquals("Failure - distance v5 is wrong", v5.getEstimate(), 7.0, DELTA);
  }

}
