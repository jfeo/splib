import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

import java.util.Comparator;
import splib.util.GraphCreator;
import splib.util.Pair;
import splib.algo.Astar;
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
  public void test_singlePair() {
    System.out.println("Running test");
    Graph<PlanarSPVertex> G = GraphCreator.planarGraph(100, 10);

    // PlanarSPVertex v1 = new PlanarSPVertex();
    // G.addVertex(v1);
    // PlanarSPVertex v2 = new PlanarSPVertex();
    // G.addVertex(v2);
    // PlanarSPVertex v3 = new PlanarSPVertex();
    // G.addVertex(v3);
    // PlanarSPVertex v4 = new PlanarSPVertex();
    // G.addVertex(v4);
    // PlanarSPVertex v5 = new PlanarSPVertex();
    // G.addVertex(v5);

    // G.addEdge(0, 1, 3.0);
    // G.addEdge(0, 4, 10.0);
    // G.addEdge(1, 4, 5.0);
    // G.addEdge(1, 3, 4.0);
    // G.addEdge(1, 2, 1.0);
    // G.addEdge(2, 3, 2.0);
    // G.addEdge(3, 4, 1.0);

    System.out.println("Running algorithm");
    double length;

    length = Astar.singlePair(new MinBinaryHeap<PlanarSPVertex>(new Astar.AstarComparator(G.getVertices().get(1), Astar::euclidianHeuristic)), G, G.getVertices().get(0), G.getVertices().get(1),
        Astar::euclidianHeuristic);
    System.out.println(length);
    System.out.println(Astar.euclidianHeuristic(G.getVertices().get(0), G.getVertices().get(1)));
    pointprint(G.getVertices().get(0));
    pointprint(G.getVertices().get(1));


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

}
