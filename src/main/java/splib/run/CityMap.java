package splib.run;

import java.util.ArrayList;
import java.util.List;
import java.io.FileNotFoundException;
import java.awt.Color;
import java.util.Collections;

import splib.algo.Astar;
import splib.algo.BidirectionalDijkstra;
import splib.data.EuclidianSPVertex;
import splib.data.BDDVertex;
import splib.data.Graph;
import splib.util.GraphCreator;
import splib.util.Pair;
import splib.util.GraphDrawer;
import splib.util.GraphDrawer.SVGElement;

public class CityMap {

  public static void main(String[] args)
    throws InstantiationException, IllegalAccessException, FileNotFoundException {
    // A* first
    Pair<Graph<EuclidianSPVertex>, ArrayList<Pair<Double, Double>>> astarG
      = GraphCreator.<EuclidianSPVertex>DIMACS("/home/feo/code/splib/analysis/USA-road-d.NY.gr",
          "/home/feo/code/splib/analysis/USA-road-d.NY.co", EuclidianSPVertex.class, 10000d);

    // Times Square to Yankees Stadium
    System.out.println(Astar.singlePair(astarG.getItem1(), 187560, 135135,
          Astar.euclidianHeuristic(astarG.getItem1().getVertex(135135)), 4));

    // Generate A* path
    ArrayList<Integer> astarPath = new ArrayList();
    Integer v = 135135;
    while (v != null) {
      astarPath.add(v);
      v = astarG.getItem1().getVertex(v).getPredecessor();
    }

    // A* relax
    ArrayList<Integer> astarRelax = new ArrayList();
    for (int i = 0; i < astarG.getItem1().getVertexCount(); i++) {
      EuclidianSPVertex u = (EuclidianSPVertex)astarG.getItem1().getVertex(i);
      if (u.getPredecessor() != null)
        astarRelax.add(i);
    }

    ArrayList<Pair<SVGElement, List<Integer>>> mlists = new ArrayList<Pair<SVGElement, List<Integer>>>();
    mlists.add(new Pair(new SVGElement("circle", Color.GREEN, 1.0d, null, 0.0d, 1.0d, 3.0d), astarRelax));
    mlists.add(new Pair(new SVGElement("circle", Color.MAGENTA, 1.0d, null, 0.0d, 1.0d, 5.0d), astarPath));

    GraphDrawer.graphSVG(1000d, "nyc-astar.svg", astarG.getItem1(), astarG.getItem2(),
        null, mlists, new ArrayList());

    // BDD second
    Pair<Graph<BDDVertex>, ArrayList<Pair<Double, Double>>> bddG
      = GraphCreator.<BDDVertex>DIMACS("/home/feo/code/splib/analysis/USA-road-d.NY.gr",
          "/home/feo/code/splib/analysis/USA-road-d.NY.co", BDDVertex.class, 10000d);

    Pair<Double, Integer> result = BidirectionalDijkstra.singlePair(bddG.getItem1(), 187560, 135135, 4);
    System.out.println(result.getItem1());


    // Generate BDD path
    ArrayList<Integer> bddPath = new ArrayList();
    v = result.getItem2();
    while (v != null) {
      bddPath.add(v);
      v = bddG.getItem1().getVertex(v).getPredecessor();
    }
    Collections.reverse(bddPath);
    v = result.getItem2();
    while (v != null) {
      bddPath.add(v);
      v = bddG.getItem1().getVertex(v).getSuccessor();
    }

    // BDD relaxations
    ArrayList<Integer> bddSuccRelax = new ArrayList();
    ArrayList<Integer> bddPredRelax = new ArrayList();
    for (int i = 0; i < bddG.getItem1().getVertexCount(); i++) {
      BDDVertex u = (BDDVertex)bddG.getItem1().getVertex(i);
      if (u.getSuccessor() != null)
        bddSuccRelax.add(i);
      if (u.getPredecessor() != null)
        bddPredRelax.add(i);
    }

    // Draw lists...
    mlists = new ArrayList<Pair<SVGElement, List<Integer>>>();
    mlists.add(new Pair(new SVGElement("circle", Color.BLUE, 1.0d, null, 0.0d, 1.0d, 4.0d), bddSuccRelax));
    mlists.add(new Pair(new SVGElement("circle", Color.GREEN, 1.0d, null, 0.0d, 1.0d, 3.0d), bddPredRelax));
    mlists.add(new Pair(new SVGElement("circle", Color.MAGENTA, 1.0d, null, 0.0d, 1.0d, 5.0d), bddPath));

    GraphDrawer.graphSVG(10000d, 10000d, "nyc-bdd.svg", bddG.getItem1(), bddG.getItem2(),
        null, mlists, new ArrayList());
  }


}
