package splib.algo;


import java.lang.RuntimeException;
import java.util.ArrayList;
import java.util.Comparator;
import splib.data.*;
import splib.util.Heap;
import splib.util.Pair;


/**
 * A class implementing A* shortest path algorithm, and needed helpers
 */
public class Astar {


  @FunctionalInterface
  public interface Heuristic <V extends EuclidianSPVertex> {
    public double heuristic(V v, V u);
  }


  /**
   * The A* single-pair shortest path algorithm.
   * @param <H> The heap type used for priority queue.
   * @param G The graph to perform the single source shortest path search on.
   * @param s The source vertex. Undefined behaviour, if this vertex is not in
   * G.
   * @return The vertices.
   */
  public static <V extends EuclidianSPVertex> double singlePair(Graph<V> G, V s,
      V t, Heuristic h, int heapArity) {
    Astar.initializeSingleSource(G, s);

    Heap<EuclidianSPVertex> open = new Heap<EuclidianSPVertex>((v, u) -> {
      if (v.getEstimate() + h.heuristic(v, t) <
          u.getEstimate() + h.heuristic(u, t)) {
        return -1;
      } else if (v.getEstimate() + h.heuristic(v, t) >
                 u.getEstimate() + h.heuristic(u, t)) {
        return 1;
      } else {
        return 0;
      }
    }, heapArity);

    s.Open();
    open.insert(s);

    // Relax edges adjacent to the minimum estimate distance vertex
    while (open.top() != t && !open.isEmpty()) {
      V u = (V)open.extract();
      u.Close();

      for (Pair<Vertex, Double> v : u.getAdjacency()) {
        // prevent overwriting previous estimates/predecessors
        // looping back
        if (v.getItem1() == u.getPredecessor() ) {
          continue;
        }
        Astar.relax(h, open, u, (V)v.getItem1(), t, v.getItem2());
        if (v.getItem1() == t) {
          break;
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
  public static <V extends EuclidianSPVertex> void initializeSingleSource(
      Graph<V> G, SPVertex s) {
    for (V v : G.getVertices()) {
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
  public static <V extends EuclidianSPVertex> void relax(Heuristic h, Heap<V> open,
      V u, V v, V t,
      double weight) {
    double newEstimate = u.getEstimate() + weight;
    if (v.isOpen() && v.getEstimate() + h.heuristic(v, t) < newEstimate + h.heuristic(v, t))    {
      // skip
    } else if (v.isClosed() && v.getEstimate() + h.heuristic(v, t) < newEstimate + h.heuristic(v, t)) {
      // skip
    } else {
      v.setPredecessor(u);
      v.setEstimate(newEstimate);
      v.Open();
      open.insert(v);
    }

    // if (v.getEstimate() + h.heuristic(v, t) > newEstimate + h.heuristic(v, t)) {
    //   if (!v.isClosed() && !v.isOpen()) {
    //     v.setPredecessor(u);
    //     v.setEstimate(newEstimate);
    //     v.Open();
    //     open.insert(v);
    //   } else if (v.isOpen()) {
    //     v.setPredecessor(u);
    //     v.setEstimate(newEstimate);
    //     open.changeKey(v);
    //   }
    // }
  }

  public static <V extends EuclidianSPVertex> double euclidianHeuristic(V u, V v) {
    return Math.sqrt(Math.pow(v.getPosition().getItem1()
                              - u.getPosition().getItem1(), 2)
                   + Math.pow(v.getPosition().getItem2()
                              - u.getPosition().getItem2(), 2));
  }
}

