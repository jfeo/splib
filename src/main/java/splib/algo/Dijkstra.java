package splib.algo;


import java.lang.RuntimeException;
import java.util.ArrayList;
import splib.data.*;
import splib.util.PriorityQueue;
import splib.util.Pair;


/**
 * A class implementing Dijkstras shortest path algorithm, and needed helpers
 */
public class Dijkstra {


  /**
   * Single source dijkstra.
   * @param G The graph to perform the single source shortest path search on.
   * @param s The source vertex. Undefined behaviour, if this vertex is not in G.
   * @return The vertices.
   */
  public static ArrayList<SPVertex> singleSource(Graph<SPVertex> G, SPVertex s) {
    s.setEstimate(0);
    ArrayList<SPVertex> S = new ArrayList<SPVertex>();
    PriorityQueue<SPVertex> Q = new PriorityQueue();
    for (SPVertex v : G.getVertices()) {
      Q.insert(v, v.getEstimate());
    }
    // Relax edges adjacent to the minimum estimate distance vertex
    while (!Q.isEmpty()) {
      SPVertex u = Q.extractMinimum();
      S.add(u);
      for (Pair<Vertex, Integer> v : u.getAdjacency()) {
        Dijkstra.relax(Q, u, (SPVertex)v.getItem1(), v.getItem2());
      }
    }
    return S;
  }


  /**
   * Relax the edge between two vertices.
   * @param Q The priority queue the vertices reside in.
   * @param u The first vertex.
   * @param v The second vertex.
   * @param weight The weight of the edge between the first and the second vertex.
   */
  public static void relax(PriorityQueue Q, SPVertex u, SPVertex v, int weight) {
    int vIndex = Q.indexOf(v);
    int newEstimate = u.getEstimate() + weight;
    if (v.getEstimate() > newEstimate) {
      v.setPredecessor(u);
      v.setEstimate(newEstimate);
      if (vIndex != -1) {
        Q.decreaseKey(vIndex, newEstimate);
      }
    }
  }


}
