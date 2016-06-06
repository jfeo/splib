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
import splib.util.Triple;
import splib.algo.ThorupZwick;
import splib.algo.Dijkstra;
import splib.data.Graph;
import splib.data.TZSPVertex;
import splib.data.Vertex;
import splib.util.Heap;
import splib.util.GraphCreator;
import splib.util.GraphDrawer;

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
      Pair<Graph<TZSPVertex>, ArrayList<Pair<Double, Double>>> euclidian = GraphCreator.euclidian(TZSPVertex.class, 100d, i, 5);
      Graph<TZSPVertex> G = euclidian.getItem1();
      List<GraphDrawer.SVGElement> AElements = new ArrayList<GraphDrawer.SVGElement>(){{
        add(new GraphDrawer.SVGElement("circle", Color.black, 1d, Color.white, 1d, 1d, 6d));
        add(new GraphDrawer.SVGElement("circle", Color.black, 1d, Color.lightGray, 1d, 1d, 5d));
        add(new GraphDrawer.SVGElement("circle", Color.black, 1d, Color.gray, 1d, 1d, 4d));
        add(new GraphDrawer.SVGElement("circle", Color.black, 1d, Color.gray, 1d, 1d, 3d));
        add(new GraphDrawer.SVGElement("circle", Color.black, 1d, Color.darkGray, 1d, 1d, 3d));
        add(new GraphDrawer.SVGElement("circle", Color.black, 1d, Color.black, 1d, 1d, 2d));
      }};
      for (int k = 3; k < 7; k++) {
        ThorupZwick tz = new ThorupZwick<TZSPVertex>(k, G, 4);
        TZSPVertex s = G.getVertices().get(0);
        TZSPVertex t = G.getVertices().get(1);
        ArrayList<ArrayList<TZSPVertex>> A = new ArrayList<ArrayList<TZSPVertex>>();
        ArrayList<TZSPVertex> witnesses = new ArrayList<TZSPVertex>();


        ArrayList<Pair<GraphDrawer.SVGElement, List<TZSPVertex>>> vertexMarkLists = new ArrayList();
        for (int j = 0; j < k; j++) {
          A.add(new ArrayList<TZSPVertex>((ArrayList<TZSPVertex>)tz.getA().get(j)));
          A.get(j).removeAll((ArrayList<TZSPVertex>)tz.getA().get(j+1));
          vertexMarkLists.add(new Pair(AElements.get(j), A.get(j)));
        }

        vertexMarkLists.add(new Pair(new GraphDrawer.SVGElement("circle", null, 0d, Color.red, 1d, 1d, 2d), new ArrayList<TZSPVertex>(){{add(s);}}));

        for (int j = 0; j < k; j++) {
          witnesses.add(s.getWitness(j).getItem1());
        }
        vertexMarkLists.add(new Pair(new GraphDrawer.SVGElement("circle", Color.orange, 1d, null, 0d, 1d, 8d), witnesses));

        ArrayList<Triple<GraphDrawer.SVGPath, TZSPVertex, List<TZSPVertex>>> vertexPathLists = new ArrayList();
        vertexPathLists.add(new Triple(new GraphDrawer.SVGPath(Color.green, 0.5d, 1d, "round"), s, new ArrayList<TZSPVertex>(){{addAll(s.getBunch().keySet());}}));

        GraphDrawer.<TZSPVertex>graphSVG(100d, "tz_" + k + "_" + i + ".svg",
            G, euclidian.getItem2(),
            new GraphDrawer.SVGElement("circle", null, 0d, Color.black, 1d, 1d, 2d),
            vertexMarkLists, vertexPathLists);

        TZSPVertex u = s;
        TZSPVertex v = t;
        TZSPVertex w = u;

        // mock query, for debugging
        int iter = 0;
        while (!v.getBunch().containsKey(w)) {
          iter++;

          ArrayList<Pair<GraphDrawer.SVGElement, List<TZSPVertex>>> vmls = new ArrayList();
          for (int j = 0; j < k; j++) {
            vmls.add(new Pair(AElements.get(j), A.get(j)));
          }

          ArrayList<TZSPVertex> vp = new ArrayList<TZSPVertex>();
          ArrayList<TZSPVertex> up = new ArrayList<TZSPVertex>();
          ArrayList<TZSPVertex> wp = new ArrayList<TZSPVertex>();
          for (int j = 0; j < k; j++) {
            vp.add(v.getWitness(j).getItem1());
            up.add(u.getWitness(j).getItem1());
            wp.add(w.getWitness(j).getItem1());
          }
          vmls.add(new Pair(new GraphDrawer.SVGElement("circle", null, 0d, Color.red, 1d, 1d, 2d), new ArrayList<TZSPVertex>(){{add(s);}}));
          vmls.add(new Pair(new GraphDrawer.SVGElement("circle", null, 0d, Color.red, 1d, 1d, 2d), new ArrayList<TZSPVertex>(){{add(t);}}));
          ArrayList<TZSPVertex> vs = new ArrayList(); vs.add(v);
          vmls.add(new Pair(new GraphDrawer.SVGElement("circle", Color.blue, 1d, null, 0d, 1d, 7d), vs));
          vmls.add(new Pair(new GraphDrawer.SVGElement("circle", Color.blue, 1d, null, 0d, 1d, 11d), vp));
          ArrayList<TZSPVertex> us = new ArrayList(); us.add(u);
          vmls.add(new Pair(new GraphDrawer.SVGElement("circle", Color.magenta, 1d, null, 0d, 1d, 8d), us));
          vmls.add(new Pair(new GraphDrawer.SVGElement("circle", Color.magenta, 1d, null, 0d, 1d, 12d), up));
          ArrayList<TZSPVertex> ws = new ArrayList(); ws.add(w);
          vmls.add(new Pair(new GraphDrawer.SVGElement("circle", Color.red, 1d, null, 0d, 1d, 9d), ws));
          vmls.add(new Pair(new GraphDrawer.SVGElement("circle", Color.red, 1d, null, 0d, 1d, 13d), wp));

          ArrayList<Triple<GraphDrawer.SVGPath, TZSPVertex, List<TZSPVertex>>> vpls = new ArrayList();
          vpls.add(new Triple(new GraphDrawer.SVGPath(Color.blue, 0.5d, 1d, "round"), v, new ArrayList<TZSPVertex>(){{addAll(s.getBunch().keySet());}}));
          vpls.add(new Triple(new GraphDrawer.SVGPath(Color.magenta, 0.5d, 1d, "round"), u, new ArrayList<TZSPVertex>(){{addAll(s.getBunch().keySet());}}));
          vpls.add(new Triple(new GraphDrawer.SVGPath(Color.red, 0.5d, 1d, "round"), w, new ArrayList<TZSPVertex>(){{addAll(s.getBunch().keySet());}}));

          GraphDrawer.<TZSPVertex>graphSVG(100d, "tz_" + k + "_" + i + "_" + iter + ".svg",
              G, euclidian.getItem2(),
              new GraphDrawer.SVGElement("circle", null, 0d, Color.black, 1d, 1d, 2d),
              vmls, vpls);
          TZSPVertex vSwap = v;
          v = u;
          u = vSwap;
          w = (TZSPVertex)u.getWitness(iter).getItem1();
        }

        double tzlength = u.getWitness(iter).getItem2() + v.getBunch().get(w);

        // double tzlength = tz.query(s, t);
        Dijkstra.<TZSPVertex>singleSource(G, s, 4);
        double dilength = t.getEstimate();
        assertTrue(tzlength <= dilength * (2 * k - 1));
      }
    }
  }
}
