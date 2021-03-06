package splib.algo;


import java.lang.RuntimeException;
import java.util.ArrayList;
import splib.data.Vertex;
import splib.data.SPVertex;
import splib.data.Graph;
import splib.util.Heap;
import splib.util.Pair;
import java.util.Comparator;


/**
 * A class implementing Dijkstras shortest path algorithm, and needed helpers
 */
public class Dijkstra {


  /**
   * Single source dijkstra.
   * @param heapArity The arity of the heap, used for sorting elements.
   * @param G The graph to perform the single source shortest path search on.
   * @param s The source vertex. Undefined behaviour, if this vertex is not in G.
   * @return The vertices.
   */
  public static <V extends SPVertex> ArrayList<Integer> singleSource(Graph<V> G,
      int s, int heapArity)  {
    Dijkstra.initializeSingleSource(G, s);

    ArrayList<Integer> S = new ArrayList<Integer>();
    Heap<Integer> Q = new Heap<Integer>((v, u) ->
        G.getVertex(v).getEstimate().compareTo(G.getVertex(u).getEstimate()),
        heapArity);

    Q.insert(s);

    // Relax edges adjacent to the minimum estimate distance vertex
    while (!Q.isEmpty()) {
      int u = Q.extract();
      S.add(u);
      for (Pair<Integer, Double> edge : G.getAdjacency(u)) {
        Dijkstra.relax(G, Q, u, edge.getItem1(), edge.getItem2());
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
  public static <V extends SPVertex> void initializeSingleSource(Graph<V> G,
      int s) {
    for (V v : G.getVertices()) {
      v.setEstimate(1.0f / 0.0f); // Infinity
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
  public static <V extends SPVertex> void relax(Graph<V> G, Heap Q, int i, int j, double weight) {
    V u = G.getVertex(i);
    V v = G.getVertex(j);
    double newEstimate = u.getEstimate() + weight;
    if (v.getEstimate() > newEstimate) {
      v.setPredecessor(i);
      v.setEstimate(newEstimate);
      Q.insert(j);
    }
  }
}
