package splib.algo;


import java.util.ArrayList;
import java.util.Collections;
import splib.data.Graph;
import splib.data.Vertex;
import splib.data.BDDVertex;;
import splib.util.Heap;
import splib.util.Pair;


/**
 * A class implementing the bidirectional variant of Dijkstra's single pair
 * shortest path algorithm, and needed auxillary methods.
 */
public class BidirectionalDijkstra {

  /**
   *  Single pair bidirectional Dijkstra.
   *  @param G The graph to perform the search on.
   *  @param s The source vertex.
   *  @param t The target vertex.
   *  @return ...
   */
  public static <V extends BDDVertex> Double singlePair(Graph<V> G, V s, V t, int heapArity) {
    BidirectionalDijkstra.initializeSinglePair(G, s, t);
    Heap<BDDVertex> Qs = new Heap<BDDVertex>((v, u) ->
        v.getEstimate().compareTo(u.getEstimate()), heapArity);
    Heap<BDDVertex> Qt = new Heap<BDDVertex>((v, u) ->
        v.getSuccessorEstimate().compareTo(u.getEstimate()), heapArity);
    ArrayList<V> Ss = new ArrayList<V>();
    ArrayList<V> St = new ArrayList<V>();

    Qs.insert(s);
    Qt.insert(t);

    // Continue, while the minimum elements of the two queues, are not identical
    while (Qs.top() != Qt.top()) {

      V u = (V)Qs.extract();
      Ss.add(u);

      if (u == null) {
        System.out.println("WAT?!");
      }

      for (Pair<Vertex, Double> v : u.getAdjacency()) {
        if (v == null) {
          System.out.println("wat?");
        }
        Dijkstra.relax(Qs, u, (V)v.getItem1(), v.getItem2());
      }

      if (u == t){
        return u.getEstimate();
      }
      if (u == Qt.top()) {
        return u.getEstimate() + Qt.top().getSuccessorEstimate();
      }

      u = (V)Qt.extract();
      St.add(u);

      for (Pair<Vertex, Double> v : u.getAdjacency()) {
        BidirectionalDijkstra.relax(Qt, u, (V)v.getItem1(), v.getItem2());
      }

      if (u == s) {
        return u.getSuccessorEstimate();
      }
      if (u == Qs.top()) {
        return u.getSuccessorEstimate() + Qs.top().getEstimate();
      }
    }

    // Check if the shortest path found, is in the best shortest path
    double bestPath = Qs.top().getEstimate() + Qt.top().getSuccessorEstimate();
    for (V v : Ss){
      if (v.getEstimate() + v.getSuccessorEstimate() < bestPath) {
        bestPath = v.getEstimate() + v.getSuccessorEstimate();
      }
    }

    for (V v : St){
      if (v.getEstimate() + v.getSuccessorEstimate() < bestPath) {
        bestPath = v.getEstimate() + v.getSuccessorEstimate();
      }
    }

    return bestPath;
  }


  /**
   * Initialize the graph.
   */
  private static <V extends BDDVertex> void initializeSinglePair(Graph<V> G, V s, V t) {
    for (V v : G.getVertices()){
      v.setEstimate(1.0d / 0.0d); // Infinity
      v.setSuccessorEstimate(1.0d / 0.0d); // Infinity
      v.setPredecessor(null);
    }
    s.setEstimate(0.0);
    t.setSuccessorEstimate(0.0);
  }


  /**
   * Relax an edge.
   * @param Q The priority queue to use with the edge.
   * @param u The start vertex.
   * @param v The end vertex.
   * @param weight The weight of the edge.
   */
  public static <V extends BDDVertex> void relax(Heap<V> Q, V u, V v, double weight) {
    double newEstimate = u.getSuccessorEstimate() + weight;
    if (v.getSuccessorEstimate() > newEstimate) {
      v.setSuccessor(u);
      v.setSuccessorEstimate(newEstimate);
      Q.insert(v);
    }
  }


  private static <V extends BDDVertex> Double checkShortestPath(ArrayList<V> S,
      Heap<V> Qs, Heap<V> Qt) {
    double bestPath = Qs.top().getEstimate() + Qt.top().getSuccessorEstimate();
    S.removeAll(Collections.singleton(null));
    for (V v : S) {
      if (v.getEstimate() < bestPath) {
        bestPath = v.getEstimate();
      }
    }

    return bestPath;
  }


}
