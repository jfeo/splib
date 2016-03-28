import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import splib.util.Pair;
import splib.algo.Dijkstra;
import splib.data.Graph;
import splib.data.SPVertex;
import splib.data.Vertex;

public class TestDijkstra {

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

    G.addEdge(0, 1, 3);
    G.addEdge(0, 4, 10);
    G.addEdge(1, 4, 5);
    G.addEdge(1, 3, 4);
    G.addEdge(1, 2, 1);
    G.addEdge(2, 3, 2);
    G.addEdge(3, 4, 1);

    Dijkstra.singleSource(G, v1);

    // Assert v1 <--> v2
    assertEquals("Failure - v1 is not predecessor of v2", v1, v2.getPredecessor());
  }

}
