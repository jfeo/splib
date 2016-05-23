import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

import java.util.Comparator;
import splib.util.Pair;
import splib.util.GraphCreator;
import splib.algo.BidirectionalDijkstra;
import splib.algo.Dijkstra;
import splib.data.Graph;
import splib.data.BDDVertex;
import splib.data.BDDVertex;
import splib.data.Vertex;
import splib.util.MinBinaryHeap;
import splib.util.ForwardComparator;
import splib.util.BackwardComparator;

public class TestBidirectionalDijkstra {

  static private double DELTA = 1e-15;

  @Test
  public void test_singlePair() {
    Graph<BDDVertex> G = new Graph<BDDVertex>();
    ForwardComparator fCompare = new ForwardComparator();
    BackwardComparator bCompare = new BackwardComparator();;

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

    length = BidirectionalDijkstra.singlePair(new MinBinaryHeap<BDDVertex>(fCompare),
        new MinBinaryHeap<BDDVertex>(bCompare), G, v1, (BDDVertex)v2);
    assertEquals(3.0, length, DELTA);

    length = BidirectionalDijkstra.singlePair(new MinBinaryHeap<BDDVertex>(fCompare),
        new MinBinaryHeap<BDDVertex>(bCompare), G, v1, (BDDVertex)v4);
    assertEquals(6.0, length, DELTA);

    length = BidirectionalDijkstra.singlePair(new MinBinaryHeap<BDDVertex>(fCompare),
        new MinBinaryHeap<BDDVertex>(bCompare), G, v1, (BDDVertex)v5);
    assertEquals(7.0, length, DELTA);

    length = BidirectionalDijkstra.singlePair(new MinBinaryHeap<BDDVertex>(fCompare),
        new MinBinaryHeap<BDDVertex>(bCompare), G, v3, (BDDVertex)v5);
    assertEquals(3.0, length, DELTA);

    length = BidirectionalDijkstra.singlePair(new MinBinaryHeap<BDDVertex>(fCompare),
        new MinBinaryHeap<BDDVertex>(bCompare), G, v2, (BDDVertex)v4);
    assertEquals(3.0, length, DELTA);

    length = BidirectionalDijkstra.singlePair(new MinBinaryHeap<BDDVertex>(fCompare),
        new MinBinaryHeap<BDDVertex>(bCompare), G, v5, (BDDVertex)v2);
    assertEquals(4.0, length, DELTA);

    length = BidirectionalDijkstra.singlePair(new MinBinaryHeap<BDDVertex>(fCompare),
        new MinBinaryHeap<BDDVertex>(bCompare), G, v4, (BDDVertex)v5);
    assertEquals(1.0, length, DELTA);
  }

  @Test
  public void test_dijkstraCompare() {
    Graph<BDDVertex> G;
    ForwardComparator fCompare = new ForwardComparator();
    BackwardComparator bCompare = new BackwardComparator();;
    BDDVertex s;
    BDDVertex t;
    double length;

    for (int i = 100; i <= 1000; i += 100) {
      float p = 1.0f;
      for (int j = 0; j < 10; j++) {
        p = p / 2.0f;
        G = GraphCreator.erdosrenyi(BDDVertex.class, i, p);
        s = G.getVertices().get(0);
        t = G.getVertices().get(1);
        System.out.println("Testing BDD vs Dijstra: erdosrenyi(" + i + ", " + p + ")");
        length = BidirectionalDijkstra.singlePair(new MinBinaryHeap<BDDVertex>(fCompare),
            new MinBinaryHeap<BDDVertex>(bCompare), G, s, t);
        Dijkstra.singleSource(new MinBinaryHeap<BDDVertex>(fCompare), G, s);
        assertEquals(t.getEstimate(), length, DELTA);
      }
    }
  }


}
