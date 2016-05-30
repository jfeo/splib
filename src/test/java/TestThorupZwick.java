import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

import splib.util.Pair;
import splib.algo.ThorupZwick;
import splib.algo.Dijkstra;
import splib.data.Graph;
import splib.data.TZSPVertex;
import splib.data.Vertex;
import splib.util.Heap;
import splib.util.GraphCreator;

public class TestThorupZwick {

  static private double DELTA = 1e-15;

  @Test
  public void test_thorupZwick() {
    Graph<TZSPVertex> G = new Graph<TZSPVertex>();
    int k = 3;

    TZSPVertex v1 = new TZSPVertex();
    G.addVertex(v1);
    TZSPVertex v2 = new TZSPVertex();
    G.addVertex(v2);
    TZSPVertex v3 = new TZSPVertex();
    G.addVertex(v3);
    TZSPVertex v4 = new TZSPVertex();
    G.addVertex(v4);
    TZSPVertex v5 = new TZSPVertex();
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

  @Test
  public void test_randomGraph() throws InstantiationException, IllegalAccessException {
    for (int i = 100; i <= 1000; i += 100) {
      Pair<Graph<TZSPVertex>, ArrayList<Pair<Double, Double>>> planar = GraphCreator.planar(TZSPVertex.class, 100d, i, 5);
      Graph<TZSPVertex> G = planar.getItem1();
      List<GraphCreator.SVGElement> AElements = new ArrayList<GraphCreator.SVGElement>(){{
        add(new GraphCreator.SVGElement("circle", Color.black, Color.white, 1d, 6d));
        add(new GraphCreator.SVGElement("circle", Color.black, Color.lightGray, 1d, 5d));
        add(new GraphCreator.SVGElement("circle", Color.black, Color.gray, 1d, 4d));
        add(new GraphCreator.SVGElement("circle", Color.black, Color.gray, 1d, 3d));
        add(new GraphCreator.SVGElement("circle", Color.black, Color.darkGray, 1d, 3d));
        add(new GraphCreator.SVGElement("circle", Color.black, Color.black, 1d, 2d));
      }};
      for (int k = 3; k < 7; k++) {
        ThorupZwick tz = new ThorupZwick<TZSPVertex>(k, G, 4);
        TZSPVertex s = G.getVertices().get(0);
        TZSPVertex t = G.getVertices().get(1);
        ArrayList<ArrayList<TZSPVertex>> A = new ArrayList<ArrayList<TZSPVertex>>();
        ArrayList<TZSPVertex> witnesses = new ArrayList<TZSPVertex>();

        for (int j = 0; j < k; j++) {
          A.add(new ArrayList<TZSPVertex>((ArrayList<TZSPVertex>)tz.getA().get(j)));
          A.get(j).removeAll((ArrayList<TZSPVertex>)tz.getA().get(j+1));
          witnesses.add(s.getWitness(j).getItem1());
        }

        GraphCreator.<TZSPVertex>graphSVG(100d, "tz_" + k + "_" + i + ".svg",
            G, planar.getItem2(),
            new GraphCreator.SVGElement("circle", null, Color.black, 1d, 2d),
            new Pair(AElements.get(0), A.get(0)),
            new Pair(AElements.get(1), A.get(1)),
            new Pair(AElements.get(2), A.get(2)),
            new Pair(AElements.get(3), k < 4 ? new ArrayList() : A.get(3)),
            new Pair(AElements.get(4), k < 5 ? new ArrayList() : A.get(4)),
            new Pair(AElements.get(5), k < 6 ? new ArrayList() : A.get(5)),
            new Pair(new GraphCreator.SVGElement("circle", Color.orange, null, 1d, 8d), witnesses),
            new Pair(new GraphCreator.SVGElement("circle", null, Color.red, 1d, 1d), new ArrayList<TZSPVertex>(){{add(s);}}));

        // double tzlength = tz.query(s, t);
        // Dijkstra.<TZSPVertex>singleSource(G, s, 4);
        // double dilength = t.getEstimate();
        // System.out.println(tzlength + ", " + dilength);
        // assertTrue(tzlength <= dilength * (2 * k - 1));
      }
    }
  }

}
