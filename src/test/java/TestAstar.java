import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

import java.util.Comparator;
import splib.util.GraphCreator;
import splib.util.Pair;
import splib.algo.Astar;
import splib.util.ForwardComparator;
import splib.algo.Dijkstra;
import splib.data.Graph;
import splib.data.PlanarSPVertex;
import splib.data.Vertex;
import splib.util.MinBinaryHeap;

public class TestAstar {

  public void pointprint(PlanarSPVertex v) {
    System.out.println(v.getPosition().getItem1() + ", " + v.getPosition().getItem2());
  }

  static private double DELTA = 1e-15;

  @Test
  public void test_singlePair01() {
    ForwardComparator<PlanarSPVertex> fCompare = new ForwardComparator();
    Graph<PlanarSPVertex> G = new Graph<PlanarSPVertex>();
    PlanarSPVertex v0 = new PlanarSPVertex(0.0, 0.0);
    PlanarSPVertex v1 = new PlanarSPVertex(0.0, 3.0);
    PlanarSPVertex v2 = new PlanarSPVertex(5.0, 0.0);
    PlanarSPVertex v3 = new PlanarSPVertex(5.0, 3.0);
    PlanarSPVertex v4 = new PlanarSPVertex(0.0, 6.0);
    PlanarSPVertex v5 = new PlanarSPVertex(10.0, 6.0);
    PlanarSPVertex v6 = new PlanarSPVertex(15.0, 6.0);
    PlanarSPVertex v7 = new PlanarSPVertex(15.0, 9.0);
    PlanarSPVertex v8 = new PlanarSPVertex(10.0, 9.0);

    G.addVertex(v0);
    G.addVertex(v1);
    G.addVertex(v2);
    G.addVertex(v3);
    G.addVertex(v4);
    G.addVertex(v5);
    G.addVertex(v6);
    G.addVertex(v7);
    G.addVertex(v8);

    G.addEdge(0, 1, 3.0);
    G.addEdge(1, 3, 5.0);
    G.addEdge(2, 3, 3.0);
    G.addEdge(1, 4, 3.0);
    G.addEdge(4, 5, 10.0);
    G.addEdge(5, 6, 5.0);
    G.addEdge(6, 7, 3.0);
    G.addEdge(7, 8, 5.0);
    G.addEdge(8, 5, 3.0);


    double length = Astar.singlePair(new MinBinaryHeap<PlanarSPVertex>(
          new Astar.AstarComparator(v8, Astar::euclidianHeuristic)), G, v0, v8,
          Astar::euclidianHeuristic);
    Dijkstra.singleSource(new MinBinaryHeap<PlanarSPVertex>(fCompare), G, v0);
    assertEquals(v8.getEstimate(), length, DELTA);
  }

  @Test
  public void test_singlePair() {
    double length;
    Graph<PlanarSPVertex> G;
    ForwardComparator<PlanarSPVertex> fCompare = new ForwardComparator();
    PlanarSPVertex t;
    PlanarSPVertex s;

    // Test case
    for (int i = 10; i <= 1000; i += 10) {
      for (int d = 3; d <= i / 3; d++) {
        System.out.printf("Testing A* on graph with %d vertices and a degree of %d\n", i, d);
        G = GraphCreator.planarGraph(i, d);
        s = G.getVertices().get(0);
        t = G.getVertices().get(1);
        length = Astar.singlePair(new MinBinaryHeap<PlanarSPVertex>(
              new Astar.AstarComparator(t, Astar::euclidianHeuristic)), G, s, t,
              Astar::euclidianHeuristic);
        Dijkstra.singleSource(new MinBinaryHeap<PlanarSPVertex>(fCompare), G, s);
        assertEquals(t.getEstimate(), length, DELTA);
      }

    }

    // Test case
    G = GraphCreator.planarGraph(100, 4);
    s = G.getVertices().get(0);
    t = G.getVertices().get(1);
    length = Astar.singlePair(new MinBinaryHeap<PlanarSPVertex>(
          new Astar.AstarComparator(t, Astar::euclidianHeuristic)), G, s, t,
          Astar::euclidianHeuristic);
    Dijkstra.singleSource(new MinBinaryHeap<PlanarSPVertex>(fCompare), G, s);
    assertEquals(length, t.getEstimate(), DELTA);

    // Test case
    G = GraphCreator.planarGraph(1000, 3);
    s = G.getVertices().get(0);
    t = G.getVertices().get(1);
    length = Astar.singlePair(new MinBinaryHeap<PlanarSPVertex>(
          new Astar.AstarComparator(t, Astar::euclidianHeuristic)), G, s, t,
          Astar::euclidianHeuristic);
    Dijkstra.singleSource(new MinBinaryHeap<PlanarSPVertex>(fCompare), G, s);
    assertEquals(length, t.getEstimate(), DELTA);


//     length = Astar.singlePair(new MinBinaryHeap<PlanarSPVertex>(new AstarComparator(v4, Astar.euclidianHeuristic)), G, v1, v4, Astar.euclidianHeuristic);
//     assertEquals(6.0, length, DELTA);

//     length = Astar.singlePair(new MinBinaryHeap<PlanarSPVertex>(new AstarComparator(v5, Astar.euclidianHeuristic)), G, v1, v5, Astar.euclidianHeuristic);
//     assertEquals(7.0, length, DELTA);

//     length = Astar.singlePair(new MinBinaryHeap<PlanarSPVertex>(new AstarComparator(v5, Astar.euclidianHeuristic)), G, v3, v5, Astar.euclidianHeuristic);
//     assertEquals(3.0, length, DELTA);

//     length = Astar.singlePair(new MinBinaryHeap<PlanarSPVertex>(new AstarComparator(v4, Astar.euclidianHeuristic)), G, v2, v4, Astar.euclidianHeuristic);
//     assertEquals(3.0, length, DELTA);

//     length = Astar.singlePair(new MinBinaryHeap<PlanarSPVertex>(new AstarComparator(v2, Astar.euclidianHeuristic)), G, v5, v2, Astar.euclidianHeuristic);
//     assertEquals(4.0, length, DELTA);

//     length = Astar.singlePair(new MinBinaryHeap<PlanarSPVertex>(new AstarComparator(v5, Astar.euclidianHeuristic)), G, v4, v5, Astar.euclidianHeuristic);
//     assertEquals(1.0, length, DELTA);
  }

  @Test
  public void dijkstraAndAstar() {

}















}
