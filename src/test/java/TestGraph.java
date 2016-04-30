import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import splib.util.Pair;
import splib.data.Graph;
import splib.data.SPVertex;
import splib.data.Vertex;


public class TestGraph {

  private static double DELTA = 1e-15;


  @Test
  public void test_addVertex() {
    Graph G = new Graph<Vertex>();
    String error = "Failure - Wrong index of created vertex";
    assertEquals(error, G.addVertex(new Vertex()), 0);
    assertEquals(error, G.addVertex(new Vertex()), 1);
    assertEquals(error, G.addVertex(new Vertex()), 2);
    assertEquals(error, G.addVertex(new Vertex()), 3);
    assertEquals(error, G.addVertex(new Vertex()), 4);
  }


  @Test
  public void test_addEdge() {
    Graph G = new Graph<Vertex>();
    String addVertexError = "Failure - Wrong index of created vertex";
    assertEquals(addVertexError, G.addVertex(new Vertex()), 0);
    assertEquals(addVertexError, G.addVertex(new Vertex()), 1);
    assertEquals(addVertexError, G.addVertex(new Vertex()), 2);
    assertEquals(addVertexError, G.addVertex(new Vertex()), 3);
    assertEquals(addVertexError, G.addVertex(new Vertex()), 4);

    String addEdgeError = "Failure - The edge is not as expected.";
    String getWeightError = "Failure - The weight is not as expected.";
    G.addEdge(0, 1, 1.0);
    Vertex v = (Vertex)G.getVertices().get(0);
    Pair<Vertex, Double> u = v.getAdjacency().get(0);
    Vertex w = (Vertex)G.getVertices().get(1);
    assertEquals(addEdgeError, u.getItem1(), w);
    assertEquals(getWeightError, u.getItem2(), 1.0, DELTA);

    G.addEdge(0, 4, 2.0);
    v = (Vertex)G.getVertices().get(0);
    u = v.getAdjacency().get(1);
    w = (Vertex)G.getVertices().get(4);
    assertEquals(addEdgeError, u.getItem1(), w);
    assertEquals(getWeightError, u.getItem2(), 2.0, DELTA);

    G.addEdge(1, 4, 3.0);
    v = (Vertex)G.getVertices().get(1);
    u = v.getAdjacency().get(1);
    w = (Vertex)G.getVertices().get(4);
    assertEquals(addEdgeError, u.getItem1(), w);
    assertEquals(getWeightError, u.getItem2(), 3.0, DELTA);

    G.addEdge(1, 3, 4.0);
    v = (Vertex)G.getVertices().get(1);
    u = v.getAdjacency().get(2);
    w = (Vertex)G.getVertices().get(3);
    assertEquals(addEdgeError, u.getItem1(), w);
    assertEquals(getWeightError, u.getItem2(), 4.0, DELTA);

    G.addEdge(1, 2, 5.0);
    v = (Vertex)G.getVertices().get(1);
    u = v.getAdjacency().get(3);
    w = (Vertex)G.getVertices().get(2);
    assertEquals(addEdgeError, u.getItem1(), w);
    assertEquals(getWeightError, u.getItem2(), 5.0, DELTA);

    G.addEdge(2, 3, 6.0);
    v = (Vertex)G.getVertices().get(2);
    u = v.getAdjacency().get(1);
    w = (Vertex)G.getVertices().get(3);
    assertEquals(addEdgeError, u.getItem1(), w);
    assertEquals(getWeightError, u.getItem2(), 6.0, DELTA);

    G.addEdge(3, 4, 7.0);
    v = (Vertex)G.getVertices().get(3);
    u = v.getAdjacency().get(2);
    w = (Vertex)G.getVertices().get(4);
    assertEquals(addEdgeError, u.getItem1(), w);
    assertEquals(getWeightError, u.getItem2(), 7.0, DELTA);
  }


}
