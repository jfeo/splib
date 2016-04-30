import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

import splib.util.Pair;
import splib.algo.BidirectionalDijkstra;
import splib.data.Graph;
import splib.data.BDDVertex;
import splib.data.Vertex;
import splib.util.MinBinaryHeap;

public class TestBidirectionalDijkstra {

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

    G.addEdge(0, 1, 3.0f);
    G.addEdge(0, 4, 10.0f);
    G.addEdge(1, 4, 5.0f);
    G.addEdge(1, 3, 4.0f);
    G.addEdge(1, 2, 1.0f);
    G.addEdge(2, 3, 2.0f);
    G.addEdge(3, 4, 1.0f);

    int length;
    length = BidirectionalDijkstra.singlePair(new MinBinaryHeap<BDDVertex>(),
        new MinBinaryHeap<BDDVertex>(), G, v1, v2);
    assertEquals(3, length);

    length = BidirectionalDijkstra.singlePair(new MinBinaryHeap<BDDVertex>(),
        new MinBinaryHeap<BDDVertex>(), G, v1, v4);
    assertEquals(6, length);

    length = BidirectionalDijkstra.singlePair(new MinBinaryHeap<BDDVertex>(),
        new MinBinaryHeap<BDDVertex>(), G, v1, v5);
    assertEquals(7, length);

    length = BidirectionalDijkstra.singlePair(new MinBinaryHeap<BDDVertex>(),
        new MinBinaryHeap<BDDVertex>(), G, v3, v5);
    assertEquals(3, length);

    length = BidirectionalDijkstra.singlePair(new MinBinaryHeap<BDDVertex>(),
        new MinBinaryHeap<BDDVertex>(), G, v2, v4);
    assertEquals(3, length);

    length = BidirectionalDijkstra.singlePair(new MinBinaryHeap<BDDVertex>(),
        new MinBinaryHeap<BDDVertex>(), G, v5, v2);
    assertEquals(4, length);

    length = BidirectionalDijkstra.singlePair(new MinBinaryHeap<BDDVertex>(),
        new MinBinaryHeap<BDDVertex>(), G, v4, v5);
    assertEquals(1, length);
  }

}
