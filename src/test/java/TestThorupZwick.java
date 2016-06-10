import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.util.HashMap;

import splib.util.Pair;
import splib.util.Triple;
import splib.algo.ThorupZwick;
import splib.algo.Dijkstra;
import splib.data.Graph;
import splib.data.SPVertex;
import splib.data.Vertex;
import splib.util.Heap;
import splib.util.GraphCreator;
import splib.util.GraphDrawer;

public class TestThorupZwick {

  static private double DELTA = 1e-15;

  @Test
  public void test_randomGraph() throws InstantiationException, IllegalAccessException {
    for (int i = 100; i <= 1000; i += 150) {
      Pair<Graph<SPVertex>, ArrayList<Pair<Double, Double>>> euclidian = GraphCreator.euclidian(SPVertex.class, 100d, i, 5);
      Graph<SPVertex> G = euclidian.getItem1();
      for (int k = 3; k < 7; k++) {
        ThorupZwick tz = new ThorupZwick<SPVertex>(k);
        ArrayList<ArrayList<SPVertex>> A = tz.preprocess(G, 4);

        for (int s = 0; s < G.getVertexCount(); s++){
          Dijkstra.<SPVertex>singleSource(G, s, 4);
          for (int t = s + 1; t < G.getVertexCount(); t++) {
            double tzlength = 0;
            try {
               tzlength = tz.query(s, t);
            } catch (Exception ex) {
              querySVG(G, euclidian.getItem2(), A, k, tz, s, t);
              throw ex;
            }

            double dilength = G.getVertex(t).getEstimate();
            assertTrue(tzlength <= dilength * (2 * k - 1));
          }
        }
      }
    }
  }

  public void querySVG(Graph<SPVertex> G, ArrayList<Pair<Double,Double>> positions, ArrayList<ArrayList<SPVertex>> A, int k, ThorupZwick oracle, int s, int t) {
    // Drawing styles
    List<GraphDrawer.SVGElement> AElements = new ArrayList<GraphDrawer.SVGElement>(){{
      add(new GraphDrawer.SVGElement("circle", Color.black, 1d, Color.white, 1d, 1d, 6d));
      add(new GraphDrawer.SVGElement("circle", Color.black, 1d, Color.lightGray, 1d, 1d, 5d));
      add(new GraphDrawer.SVGElement("circle", Color.black, 1d, Color.gray, 1d, 1d, 4d));
      add(new GraphDrawer.SVGElement("circle", Color.black, 1d, Color.gray, 1d, 1d, 3d));
      add(new GraphDrawer.SVGElement("circle", Color.black, 1d, Color.darkGray, 1d, 1d, 3d));
      add(new GraphDrawer.SVGElement("circle", Color.black, 1d, Color.black, 1d, 1d, 2d));
    }};

    ArrayList<ArrayList<SPVertex>> A_exclusive = new ArrayList<ArrayList<SPVertex>>();
    ArrayList<SPVertex> witnesses = new ArrayList<SPVertex>();

    ArrayList<Pair<GraphDrawer.SVGElement, List<SPVertex>>> vertexMarkLists = new ArrayList();
    for (int j = 0; j < k; j++) {
      A_exclusive.add(new ArrayList<SPVertex>(A.get(j)));
      A_exclusive.get(j).removeAll(A.get(j+1));
    }

    // mock query, for debugging
    Integer u = s;
    Integer v = t;
    Integer w = u;
    int i = 0;
    while (!oracle.getBunch(v).containsKey(w)) {
      i++;

      ArrayList<Pair<GraphDrawer.SVGElement, List<Integer>>> vmls = new ArrayList();
      for (int j = 0; j < oracle.getK(); j++) {
        vmls.add(new Pair(AElements.get(j), A_exclusive.get(j)));
      }

      ArrayList<Integer> vp = new ArrayList<Integer>();
      ArrayList<Integer> up = new ArrayList<Integer>();
      ArrayList<Integer> wp = new ArrayList<Integer>();
      for (int j = 0; j < oracle.getK(); j++) {
        vp.add((Integer)oracle.getWitness(v, j).getItem1());
        up.add((Integer)oracle.getWitness(u, j).getItem1());
        wp.add((Integer)oracle.getWitness(w, j).getItem1());
      }
      vmls.add(new Pair(new GraphDrawer.SVGElement("circle", null, 0d, Color.red, 1d, 1d, 2d), new ArrayList<Integer>(){{add(s);}}));
      vmls.add(new Pair(new GraphDrawer.SVGElement("circle", null, 0d, Color.red, 1d, 1d, 2d), new ArrayList<Integer>(){{add(t);}}));

      ArrayList<Integer> vs = new ArrayList(); vs.add(v);
      vmls.add(new Pair(new GraphDrawer.SVGElement("circle", Color.blue, 1d, null, 0d, 1d, 7d), vs));
      vmls.add(new Pair(new GraphDrawer.SVGElement("circle", Color.blue, 1d, null, 0d, 1d, 11d), vp));

      ArrayList<Integer> us = new ArrayList(); us.add(u);
      vmls.add(new Pair(new GraphDrawer.SVGElement("circle", Color.magenta, 1d, null, 0d, 1d, 8d), us));
      vmls.add(new Pair(new GraphDrawer.SVGElement("circle", Color.magenta, 1d, null, 0d, 1d, 12d), up));

      ArrayList<Integer> ws = new ArrayList(); ws.add(w);
      vmls.add(new Pair(new GraphDrawer.SVGElement("circle", Color.red, 1d, null, 0d, 1d, 9d), ws));
      vmls.add(new Pair(new GraphDrawer.SVGElement("circle", Color.red, 1d, null, 0d, 1d, 13d), wp));

      ArrayList<Triple<GraphDrawer.SVGPath, Integer, List<Integer>>> vpls = new ArrayList();
      ArrayList<Integer> vBunch = new ArrayList<Integer>();
      for (Integer j : (List<Integer>)oracle.getBunch(v).keySet()) vBunch.add(j);
      vpls.add(new Triple(new GraphDrawer.SVGPath(Color.blue, 0.5d, 1d, "round"), v, vBunch));
      ArrayList<Integer> uBunch = new ArrayList<Integer>();
      for (Integer j : (List<Integer>)oracle.getBunch(u).keySet()) uBunch.add(j);
      vpls.add(new Triple(new GraphDrawer.SVGPath(Color.magenta, 0.5d, 1d, "round"), u, uBunch));
      ArrayList<Integer> wBunch = new ArrayList<Integer>();
      for (Integer j : (List<Integer>)oracle.getBunch(w).keySet()) wBunch.add(j);
      vpls.add(new Triple(new GraphDrawer.SVGPath(Color.red, 0.5d, 1d, "round"), w, wBunch));

      GraphDrawer.graphSVG(100d, "tz_query_" + oracle.getK() + "_" + G.getVertexCount() + "_" + i + ".svg",
          G, positions,
          new GraphDrawer.SVGElement("circle", null, 0d, Color.black, 1d, 1d, 2d),
          vmls, vpls);

      Integer vSwap = v;
      v = u;
      u = vSwap;
      w = (Integer)oracle.getWitness(u, i).getItem1();
    }
  }
}
