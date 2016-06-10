package splib.algo;


import java.util.ArrayList;
import java.util.Collections;
import splib.data.Graph;
import splib.data.Vertex;
import splib.data.BDDVertex;;
import splib.util.Heap;
import splib.util.Heap.Index;
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
  public static <V extends BDDVertex> Pair<Double, Integer> singlePair(Graph<V> G, int s, int t, int heapArity) {
    BidirectionalDijkstra.initializeSinglePair(G, s, t);
    Heap<Integer> Qs = new Heap<Integer>((i, j) ->
        G.getVertex(i).getEstimate().compareTo(G.getVertex(j).getEstimate()),
        heapArity);
    Heap<Integer> Qt = new Heap<Integer>((i, j) ->
        G.getVertex(i).getSuccessorEstimate().compareTo(G.getVertex(j).getSuccessorEstimate()),
        heapArity);
    ArrayList<Index> indices = new ArrayList<Index>();
    ArrayList<Integer> Ss = new ArrayList<Integer>();
    ArrayList<Integer> St = new ArrayList<Integer>();

    G.getVertex(s).setSourceStatus(BDDVertex.Status.Queued);
    G.getVertex(s).setSourceIndex(Qs.insert(s));
    G.getVertex(t).setTargetStatus(BDDVertex.Status.Queued);
    G.getVertex(t).setTargetIndex(Qt.insert(t));

    // Continue, while the minimum elements of the two queues, are not identical
    while (!Qs.isEmpty() || !Qt.isEmpty()) {

      if (!Qs.isEmpty()) {
        int u = Qs.extract();
        G.getVertex(u).setSourceStatus(BDDVertex.Status.HasBeenQueued);
        Ss.add(u);
        if (G.getVertex(u).getTargetStatus() == BDDVertex.Status.HasBeenQueued) {
          break;
        }

        for (Pair<Integer, Double> edge : G.getAdjacency(u)) {
          BidirectionalDijkstra.relaxSource(G, Qs, u, edge.getItem1(), edge.getItem2());
        }
      }

      if (!Qt.isEmpty()) {
        int u = Qt.extract();
        G.getVertex(u).setTargetStatus(BDDVertex.Status.HasBeenQueued);
        St.add(u);
        if (G.getVertex(u).getSourceStatus() == BDDVertex.Status.HasBeenQueued) {
          break;
        }

        for (Pair<Integer, Double> edge : G.getAdjacency(u)) {
          BidirectionalDijkstra.relaxTarget(G, Qt, u, edge.getItem1(), edge.getItem2());
        }
      }
    }

    // Check if the shortest path found, is in the best shortest path
    double bestPath = 1d/0d;
    V bestV = null;

    for (Pair<Integer, ?> qElem : Qs.getElements()) {
      V v = G.getVertex(qElem.getItem1());
      if (v.getEstimate() + v.getSuccessorEstimate() <= bestPath) {
        bestPath = v.getEstimate() + v.getSuccessorEstimate();
        bestV = (V)v;
      }
    }

    for (Pair<Integer, ?> qElem : Qt.getElements()) {
      V v = G.getVertex(qElem.getItem1());
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
  private static <V extends BDDVertex> void initializeSinglePair(Graph<V> G, int s, int t) {
    for (V v : G.getVertices()){
      v.setEstimate(1.0d / 0.0d); // Infinity
      v.setSuccessorEstimate(1.0d / 0.0d); // Infinity
      v.setPredecessor(null);
      v.setSuccessor(null);
      v.setSourceStatus(BDDVertex.Status.NotQueued);
      v.setTargetStatus(BDDVertex.Status.NotQueued);
    }
    G.getVertex(s).setEstimate(0.0);
    G.getVertex(t).setSuccessorEstimate(0.0);
  }


  /**
   * Relax an edge.
   * @param Q The priority queue to use with the edge.
   * @param u The start vertex.
   * @param v The end vertex.
   * @param weight The weight of the edge.
   */
  public static <V extends BDDVertex> void relaxTarget(Graph<V> G, Heap<Integer> Q, int i, int j, double weight) {
    V u = G.getVertex(i);
    V v = G.getVertex(j);
    double newEstimate = u.getSuccessorEstimate() + weight;
    if (v.getSuccessorEstimate() > newEstimate) {
      v.setSuccessor(i);
      v.setSuccessorEstimate(newEstimate);
      if (v.getTargetStatus() == BDDVertex.Status.Queued) {
        Q.changeKey(v.getTargetIndex().getValue());
      } else {
        v.setTargetStatus(BDDVertex.Status.Queued);
        v.setTargetIndex(Q.insert(j));
      }
    }
  }


  public static <V extends BDDVertex> void relaxSource(Graph<V> G, Heap<Integer> Q, int i, int j, double weight) {
    V u = G.getVertex(i);
    V v = G.getVertex(j);
    double newEstimate = u.getEstimate() + weight;
    if (v.getEstimate() > newEstimate) {
      v.setPredecessor(i);
      v.setEstimate(newEstimate);
      if (v.getSourceStatus() == BDDVertex.Status.Queued) {
        Q.changeKey(v.getSourceIndex().getValue());
      } else {
        v.setSourceStatus(BDDVertex.Status.Queued);
        v.setSourceIndex(Q.insert(j));
      }
    }
  }


}
