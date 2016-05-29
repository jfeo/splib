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
  public static <V extends BDDVertex> Pair<Double, V> singlePair(Graph<V> G, V s, V t, int heapArity) {
    BidirectionalDijkstra.initializeSinglePair(G, s, t);
    Heap<BDDVertex> Qs = new Heap<BDDVertex>((v, u) ->
        v.getEstimate().compareTo(u.getEstimate()), heapArity);
    Heap<BDDVertex> Qt = new Heap<BDDVertex>((v, u) ->
        v.getSuccessorEstimate().compareTo(u.getSuccessorEstimate()), heapArity);
    ArrayList<V> Ss = new ArrayList<V>();
    ArrayList<V> St = new ArrayList<V>();

    s.setSourceStatus(BDDVertex.Status.Queued);
    Qs.insert(s);
    t.setTargetStatus(BDDVertex.Status.Queued);
    Qt.insert(t);

    // Continue, while the minimum elements of the two queues, are not identical
    while (!Qs.isEmpty() || !Qt.isEmpty()) {
      V u;
      if (!Qs.isEmpty()) {
        u = (V)Qs.extract();
        u.setSourceStatus(BDDVertex.Status.HasBeenQueued);
        Ss.add(u);
        if (u.getTargetStatus() == BDDVertex.Status.HasBeenQueued) {
          break;
        }

        for (Pair<Vertex, Double> v : u.getAdjacency()) {
          BidirectionalDijkstra.relaxSource(Qs, u, (V)v.getItem1(), v.getItem2());
        }
      }

      if (!Qt.isEmpty()) {
        u = (V)Qt.extract();
        u.setTargetStatus(BDDVertex.Status.HasBeenQueued);
        St.add(u);
        if (u.getSourceStatus() == BDDVertex.Status.HasBeenQueued) {
          break;
        }

        for (Pair<Vertex, Double> v : u.getAdjacency()) {
          BidirectionalDijkstra.relaxTarget(Qt, u, (V)v.getItem1(), v.getItem2());
        }
      }
    }

    if (Qs.top() == null || Qt.top() == null) {
      return new Pair(1.0d / 0.0f, null);
    }

    // Check if the shortest path found, is in the best shortest path
    double bestPath = s.getSuccessorEstimate();
    V bestV = s;
    for (BDDVertex v : G.getVertices()) {
      if (v.getEstimate() + v.getSuccessorEstimate() <= bestPath) {
        bestPath = v.getEstimate() + v.getSuccessorEstimate();
        bestV = (V)v;
      }
    }

    return new Pair(bestPath, bestV);
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
  public static <V extends BDDVertex> void relaxTarget(Heap<V> Q, V u, V v, double weight) {
    double newEstimate = u.getSuccessorEstimate() + weight;
    if (v.getSuccessorEstimate() > newEstimate) {
      v.setSuccessor(u);
      v.setSuccessorEstimate(newEstimate);
      if (v.getTargetStatus() == BDDVertex.Status.Queued) {
        Q.changeKey(v);
      } else {
        v.setTargetStatus(BDDVertex.Status.Queued);
        Q.insert(v);
      }
    }
  }


  public static <V extends BDDVertex> void relaxSource(Heap<V> Q, V u, V v, double weight) {
    double newEstimate = u.getEstimate() + weight;
    if (v.getEstimate() > newEstimate) {
      v.setPredecessor(u);
      v.setEstimate(newEstimate);
      if (v.getSourceStatus() == BDDVertex.Status.Queued) {
        Q.changeKey(v);
      } else {
        v.setSourceStatus(BDDVertex.Status.Queued);
        Q.insert(v);
      }
    }
  }


}
