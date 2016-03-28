import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

import splib.util.Pair;
import splib.algo.Dijkstra;
import splib.data.Graph;
import splib.data.SPVertex;
import splib.data.Vertex;
import splib.util.GraphCreator;

public class TestGraphCreator {

  @Test
  public void test_importOSM() {
    try {
      Graph G = GraphCreator.importOSM("cph_center.osm");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      assertNull(e);
    }
    assertEquals(0, 1);
  }

}
