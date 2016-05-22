import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

import splib.util.Pair;
import splib.util.GraphCreator;
import splib.algo.BidirectionalDijkstra;
import splib.algo.Dijkstra;
import splib.data.Graph;
import splib.data.BDDVertex;
import splib.data.SPVertex;
import splib.data.Vertex;

public class TestBidirectionalDijkstra {

  static private double DELTA = 1e-15;

  @Test
  public void test_singlePair() {
    Graph<BDDVertex> G = new Graph<BDDVertex>();

    BDDVertex v1 = new BDDVertex();
    G.addVertex(v1);
    BDDVertex v2 = new BDDVertex();
    G.addVertex(v2);
    BDDVertex v3 = new BDDVertex();
    G.addVertex(v3);
    BDDVertex v4 = new BDDVertex();
    G.addVertex(v4);
    BDDVertex v5 = new BDDVertex();
    G.addVertex(v5);

    G.addEdge(0, 1, 3.0);
    G.addEdge(0, 4, 10.0);
    G.addEdge(1, 4, 5.0);
    G.addEdge(1, 3, 4.0);
    G.addEdge(1, 2, 1.0);
    G.addEdge(2, 3, 2.0);
    G.addEdge(3, 4, 1.0);

    double length;

    length = BidirectionalDijkstra.singlePair(G, v1, v2, 3);
    assertEquals(3.0, length, DELTA);

    length = BidirectionalDijkstra.singlePair(G, v1, v4, 3);
    assertEquals(6.0, length, DELTA);

    length = BidirectionalDijkstra.singlePair(G, v1, v5, 3);
    assertEquals(7.0, length, DELTA);

    length = BidirectionalDijkstra.singlePair(G, v3, v5, 3);
    assertEquals(3.0, length, DELTA);

    length = BidirectionalDijkstra.singlePair(G, v2, v4, 3);
    assertEquals(3.0, length, DELTA);

    length = BidirectionalDijkstra.singlePair(G, v5, v2, 3);
    assertEquals(4.0, length, DELTA);

    length = BidirectionalDijkstra.singlePair(G, v4, v5, 3);
    assertEquals(1.0, length, DELTA);
  }

  @Test
  public void test_dijkstraCompare() throws IllegalAccessException, InstantiationException {
    Graph<BDDVertex> G;
    BDDVertex s, t;
    Double length;

    for (int i = 50; i <= 1000; i += 50) {
      float p = 1.0f;
      for (int j = 0; j < 10; j++) {
        p = p / 10.0f;
        System.out.println("Testing BDD on Erdos-Renyi(" + i + ", " + p + ")");
        G = GraphCreator.erdosrenyi(BDDVertex.class, i, p);
        s = G.getVertices().get(0);
        t = G.getVertices().get(1);
        length = BidirectionalDijkstra.<BDDVertex>singlePair(G, s, t, 4);
        Dijkstra.<BDDVertex>singleSource(G, s, 4);
        assertEquals(t.getEstimate(), length, DELTA);
      }
    }

  }

}
