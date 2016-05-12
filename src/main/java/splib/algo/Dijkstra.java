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
  public static ArrayList<SPVertex> singleSource(
      Heap<SPVertex> h, Graph<SPVertex> G, SPVertex s) {
    Dijkstra.initializeSingleSource(G, s);

    ArrayList<SPVertex> S = new ArrayList<SPVertex>();
    PriorityQueue<SPVertex> Q = new PriorityQueue<SPVertex>(h);

    Q.insert(s);

    // Relax edges adjacent to the minimum estimate distance vertex
    while (!Q.isEmpty()) {
      SPVertex u = Q.extract();
      S.add(u);
      for (Pair<Vertex, Double> v : u.getAdjacency()) {
        Dijkstra.relax(Q, u, (SPVertex)v.getItem1(), v.getItem2());
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
  public static void initializeSingleSource(Graph<SPVertex> G, SPVertex s) {
    for (SPVertex v : G.getVertices()) {
      v.setEstimate(Double.MAX_VALUE);
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
  public static void relax(PriorityQueue Q, SPVertex u, SPVertex v, double weight) {
     double newEstimate = u.getEstimate() + weight;
    if (v.getEstimate() > newEstimate) {
      v.setPredecessor(u);
      v.setEstimate(newEstimate);
      Q.insert(v);
    }
  }
}
