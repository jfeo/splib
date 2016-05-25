import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

import java.util.Comparator;
import splib.util.Pair;
import splib.algo.ThorupZwick;
import splib.data.Graph;
import splib.data.TZSPVertex;
import splib.data.Vertex;
import splib.util.Heap;

public class TestThorupZwick {

  static private double DELTA = 1e-15;

  @Test
  public void test_thorupZwick() {
    Graph<TZSPVertex> G = new Graph<TZSPVertex>();
    int k = 3;

    TZSPVertex v1 = new TZSPVertex(k);
    G.addVertex(v1);
    TZSPVertex v2 = new TZSPVertex(k);
    G.addVertex(v2);
    TZSPVertex v3 = new TZSPVertex(k);
    G.addVertex(v3);
    TZSPVertex v4 = new TZSPVertex(k);
    G.addVertex(v4);
    TZSPVertex v5 = new TZSPVertex(k);
    G.addVertex(v5);

    G.addEdge(0, 1, 3.0);
    G.addEdge(0, 4, 10.0);
    G.addEdge(1, 4, 5.0);
    G.addEdge(1, 3, 4.0);
    G.addEdge(1, 2, 1.0);
    G.addEdge(2, 3, 2.0);
    G.addEdge(3, 4, 1.0);

    ThorupZwick tz = new ThorupZwick(k, G, 4);
  }

}
