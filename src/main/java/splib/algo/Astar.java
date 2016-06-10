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
    public double heuristic(V v);
  }


  /** The A* single-pair shortest path algorithm.  @param <H> The heap type
   * used for priority queue.  @param G The graph to perform the single source
   * shortest path search on.  @param s The source vertex. Undefined behaviour,
   * if this vertex is not in G.  @return The vertices.
   */
  public static <V extends EuclidianSPVertex> double singlePair(Graph<V> G,
      Integer s, Integer t, Heuristic h, int heapArity) {
    Astar.initializeSingleSource(G, s);

    Heap<Integer> open = new Heap<Integer>((i, j) -> {
      V v = G.getVertex(i);
      V u = G.getVertex(j);
      if (v.getEstimate() + h.heuristic(v) <
          u.getEstimate() + h.heuristic(u)) {
        return -1;
      } else if (v.getEstimate() + h.heuristic(v) >
                 u.getEstimate() + h.heuristic(u)) {
        return 1;
      } else {
        return 0;
      }
    }, heapArity);

    G.getVertex(s).Open();
    open.insert(s);

    // Relax edges adjacent to the minimum estimate distance vertex
    while (true) {
      boolean stuff1 = open.top() != t;
      boolean stuff2 = !open.isEmpty();
      if (!(stuff1 && stuff2))
        break;
      int j = open.extract();
      G.getVertex(j).Close();

      for (Pair<Integer, Double> edge : G.getAdjacency(j)) {
        // prevent overwriting previous estimates/predecessors
        // looping back
        if (edge.getItem1() == G.getVertex(j).getPredecessor()) {
          continue;
        }
        Astar.relax(G, h, open, j, edge.getItem1(), edge.getItem2());
        if (edge.getItem1() == t) {
          break;
        }
      }
    }

    return G.getVertex(t).getEstimate();
  }


  /**
   * Set all vertices in the graph to have an estimate distance of the max
   * integer value, and a null predecessor, and set the source vertex to have
   * an estimate distance of 0.
   * @param G The graph to work on.
   * @param s The source vertex.
   */
  public static <V extends EuclidianSPVertex> void initializeSingleSource(
      Graph<V> G, int s) {
    for (V v : G.getVertices()) {
      v.setEstimate(1.0d / 0.0d); // Infinity
      v.setPredecessor(null);
    }
    G.getVertex(s).setEstimate(0);
  }


  /**
   * Relax the edge between two vertices.
   * @param Q The priority queue the vertices reside in.
   * @param u The first vertex.
   * @param v The second vertex.
   * @param weight The weight of the edge between the first and the second vertex.
   */
  public static <V extends EuclidianSPVertex> void relax(Graph<V> G, Heuristic h, Heap<Integer> open,
      int i, int j, double weight) {
    V u = G.getVertex(i);
    V v = G.getVertex(j);

    double newEstimate = u.getEstimate() + weight;
    if (v.isOpen() && v.getEstimate() + h.heuristic(v) < newEstimate + h.heuristic(v))    {
      // skip
    } else if (v.isClosed() && v.getEstimate() + h.heuristic(v) < newEstimate + h.heuristic(v)) {
      // skip
    } else {
      v.setPredecessor(i);
      v.setEstimate(newEstimate);
      v.Open();
      open.insert(j);
    }
  }

  public static <V extends EuclidianSPVertex> Heuristic<V> euclidianHeuristic(V t) {
    return v -> {
      return Math.sqrt(Math.pow(t.getPosition().getItem1()
                              - v.getPosition().getItem1(), 2)
                     + Math.pow(t.getPosition().getItem2()
                              - v.getPosition().getItem2(), 2));
    };
  }
}

