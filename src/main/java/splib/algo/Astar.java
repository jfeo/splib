package splib.algo;


import java.lang.RuntimeException;
import java.util.ArrayList;
import java.util.Comparator;
import splib.data.*;
import splib.util.Heap;
import splib.util.PriorityQueue;
import splib.util.Pair;


/**
 * A class implementing A* shortest path algorithm, and needed helpers
 */
public class Astar {



  public static class AstarComparator<V extends PlanarSPVertex> implements Comparator<V> {
    private PlanarSPVertex t;
    private Heuristic h;

    public AstarComparator(PlanarSPVertex t, Heuristic h) {
      this.t = t;
      this.h = h;
    }

    public int compare(V v, V u) {
      if (v.getEstimate() + this.h.heuristic(v, this.t) <
          u.getEstimate() + this.h.heuristic(u, this.t)) {
        return -1;
      } else if (v.getEstimate() + this.h.heuristic(v, this.t) >
                 u.getEstimate() + this.h.heuristic(u, this.t)) {
        return 1;
      } else {
        return 0;
      }
    }
  }



  @FunctionalInterface
  public interface Heuristic {
    public double heuristic(PlanarSPVertex v, PlanarSPVertex u);
  }


  /**
   * Single pair dijkstra.
   * @param <H> The heap type used for priority queue.
   * @param G The graph to perform the single source shortest path search on.
   * @param s The source vertex. Undefined behaviour, if this vertex is not in G.
   * @return The vertices.
   */
  public static double singlePair(Heap<PlanarSPVertex> h, Graph<PlanarSPVertex> G,
      PlanarSPVertex s, PlanarSPVertex t, Heuristic heur) {
    Astar.initializeSingleSource(G, s);

    PriorityQueue<PlanarSPVertex> open = new PriorityQueue<PlanarSPVertex>(h);

    open.insert(s);

    // Relax edges adjacent to the minimum estimate distance vertex
    while (!open.isEmpty()) {
      PlanarSPVertex u = open.extract();
      if (u == t) {
        break;
      }

      for (Pair<Vertex, Double> v : u.getAdjacency()) {
        // prevent overwriting previous estimates/predecessors
        // looping back
        if (v.getItem1() == u.getPredecessor()) {
          continue;
        }
        Astar.relax(heur, open, u, (PlanarSPVertex)v.getItem1(), t, v.getItem2());
        if (v.getItem1() == t) {
          return t.getEstimate();
        }
      }
    }
    return t.getEstimate();
  }


  /**
   * Set all vertices in the graph to have an estimate distance of the max
   * integer value, and a null predecessor, and set the source vertex to have
   * an estimate distance of 0.
   * @param G The graph to work on.
   * @param s The source vertex.
   */
  public static void initializeSingleSource(Graph<PlanarSPVertex> G, SPVertex s) {
    for (PlanarSPVertex v : G.getVertices()) {
      v.setEstimate(1.0d / 0.0d); // Infinity
      v.setPredecessor(null);
    }
    s.setEstimate(0);
  }


  /**
   * Relax the edge between two vertices.
   * @param Q The priority queue the vertices reside in.
   * @param u The first vertex.
   * @param v The second vertex.
   * @param weight The weight of the edge between the first and the second vertex.
   */
  public static void relax(Heuristic h, PriorityQueue<PlanarSPVertex> open, PlanarSPVertex u, PlanarSPVertex v,
      PlanarSPVertex t, double weight) {
    double newEstimate = u.getEstimate() + weight;
    if (v.getEstimate() + h.heuristic(v, t) > newEstimate + h.heuristic(v, t)) {
      if (v.getPredecessor() == null) {
        v.setPredecessor(u);
        v.setEstimate(newEstimate);
        open.insert(v);
      } else {
        v.setPredecessor(u);
        v.setEstimate(newEstimate);
        open.changeKey(v);
      }
    }
  }

  public static double euclidianHeuristic(PlanarSPVertex u, PlanarSPVertex v) {
    return Math.sqrt(Math.pow(v.getPosition().getItem1() - u.getPosition().getItem1(), 2)
                   + Math.pow(v.getPosition().getItem2() - u.getPosition().getItem2(), 2));
  }
}

