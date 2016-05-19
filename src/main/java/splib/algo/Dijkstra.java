package splib.algo;


import java.lang.RuntimeException;
import java.util.ArrayList;
import splib.data.*;
import splib.util.Heap;
import splib.util.PriorityQueue;
import splib.util.Pair;


/**
 * A class implementing Dijkstras shortest path algorithm, and needed helpers
 */
public class Dijkstra {


  /**
   * Single source dijkstra.
   * @param <H> The heap type used for priority queue.
   * @param G The graph to perform the single source shortest path search on.
   * @param s The source vertex. Undefined behaviour, if this vertex is not in G.
   * @return The vertices.
   */
  public static <V extends SPVertex> ArrayList<V> singleSource(
      Heap<V> h, Graph<V> G, V s) {
    Dijkstra.initializeSingleSource(G, s);

    ArrayList<V> S = new ArrayList<V>();
    PriorityQueue<V> Q = new PriorityQueue<V>(h);

    Q.insert(s);

    // Relax edges adjacent to the minimum estimate distance vertex
    while (!Q.isEmpty()) {
      V u = Q.extract();
      S.add(u);
      for (Pair<Vertex, Double> v : u.getAdjacency()) {
        Dijkstra.relax(Q, u, (V)v.getItem1(), v.getItem2());
      }
    }
    return S;
  }


  /**
   * Set all vertices in the graph to have an estimate distance of the max
   * integer value, and a null predecessor, and set the source vertex to have
   * an estimate distance of 0.
   * @param G The graph to work on.
   * @param s The source vertex.
   */
  public static <V extends SPVertex> void initializeSingleSource(Graph<V> G, V s) {
    for (V v : G.getVertices()) {
      v.setEstimate(1.0f / 0.0f); // Infinity
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
  public static <V extends SPVertex> void relax(PriorityQueue<V> Q, V u, V v, double weight) {
    double newEstimate = u.getEstimate() + weight;
    if (v.getEstimate() > newEstimate) {
      v.setPredecessor(u);
      v.setEstimate(newEstimate);
      Q.insert(v);
    }
  }
}
