import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

import tobfeo.splib.Graph;

public class TestGraph {

  @Test
  public void test_addVertex() {
    Graph G = new Graph();
    Integer v = G.addVertex();
    assertEquals(v, Integer.valueOf(0));

    Integer u = G.addVertex();
    assertEquals(u, Integer.valueOf(1));
  }

  @Test
  public void test_addEdge() {
    Graph G = new Graph();

    Integer v = G.addVertex();
    Integer u = G.addVertex();

    G.addEdge(v, u, 15);
    assertEquals(G.getWeight(v, u), Integer.valueOf(15));
  }

}
