package splib.algo;


import java.util.ArrayList;
import java.util.Collections;
import splib.data.*;
import splib.util.Heap;
import splib.util.PriorityQueue;
import splib.util.Pair;


/**
 * A class implementing the bidirectional variant of Dijkstra's single pair
 * shortest path algorithm, and needed auxillary methods.
 */
public class BidirectionalDijkstra {

  /**
   *  Single pair bidirectional Dijkstra.
   *  @param hs A heap for the forward search.
   *  @param ht A heap for the backwards search.
   *  @param G The graph to perform the search on.
   *  @param s The source vertex.
   *  @param t The target vertex.
   *  @return ...
   */
  public static Double singlePair(Heap<BDDVertex> hs, Heap<BDDVertex> ht,
      Graph<BDDVertex> G, BDDVertex s, BDDVertex t) {
    BidirectionalDijkstra.initializeSinglePair(G, s, t);
    PriorityQueue<BDDVertex> Qs = new PriorityQueue<BDDVertex>(hs);
    PriorityQueue<BDDVertex> Qt = new PriorityQueue<BDDVertex>(ht);
    ArrayList<BDDVertex> Ss = new ArrayList<BDDVertex>();
    ArrayList<BDDVertex> St = new ArrayList<BDDVertex>();

    Qs.insert(s);
    Qt.insert(t);

    // Continue, while the minimum elements of the two queues, are not identical
    while (!Qs.isEmpty() && !Qt.isEmpty() && Qs.top() != Qt.top()) {

      BDDVertex u = Qs.extract();
      Ss.add(u);

      for (Pair<Vertex, Double> v : u.getAdjacency()) {
        Dijkstra.relax(Qs, u, (BDDVertex)v.getItem1(), v.getItem2());
      }

      if (u == t){
        break;
      }
      if (u == Qt.top()) {
        break;
      }

      u = Qt.extract();
      St.add(u);

      for (Pair<Vertex, Double> v : u.getAdjacency()) {
        BidirectionalDijkstra.relax(Qt, u, (BDDVertex)v.getItem1(), v.getItem2());
      }

      if (u == s) {
        break;
      }
      if (u == Qs.top()) {
        break;

      }
    }

    if (Qs.top() == null || Qt.top() == null) {
      return 1.0d / 0.0f;
    }

    // Check if the shortest path found, is in the best shortest path
    double bestPath = Qs.top().getEstimate() + Qt.top().getSuccessorEstimate();
    for (BDDVertex v : Ss) {
      if (v.getEstimate() + v.getSuccessorEstimate() < bestPath) {
        bestPath = v.getEstimate() + v.getSuccessorEstimate();
      }
    }

    for (BDDVertex v : St){
      if (v.getEstimate() + v.getSuccessorEstimate() < bestPath) {
        bestPath = v.getEstimate() + v.getSuccessorEstimate();
      }
    }

    // for (BDDVertex v : Qs.getHeap().getElements()) {
    //   if (v.getEstimate() + v.getSuccessorEstimate() < bestPath) {
    //     bestPath = v.getEstimate() + v.getSuccessorEstimate();
    //   }
    // }

    // for (BDDVertex v : Qt.getHeap().getElements()) {
    //   if (v.getEstimate() + v.getSuccessorEstimate() < bestPath) {
    //     bestPath = v.getEstimate() + v.getSuccessorEstimate();
    //   }
    // }

    return bestPath;
  }


  /**
   * Initialize the graph.
   */
  private static void initializeSinglePair(Graph<BDDVertex> G, BDDVertex s, BDDVertex t){
    for (BDDVertex v : G.getVertices()){
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
  public static void relax(PriorityQueue<BDDVertex> Q,
      BDDVertex u, BDDVertex v, double weight) {
    double newEstimate = u.getSuccessorEstimate() + weight;
    if (v.getSuccessorEstimate() > newEstimate) {
      v.setSuccessor(u);
      v.setSuccessorEstimate(newEstimate);
      Q.insert(v);
    }
  }


  private static Double checkShortestPath(ArrayList<BDDVertex> S,
      PriorityQueue<BDDVertex> Qs, PriorityQueue<BDDVertex> Qt) {
    double bestPath = Qs.top().getEstimate() + Qt.top().getSuccessorEstimate();
    S.removeAll(Collections.singleton(null));
    for (BDDVertex v : S) {
      if (v.getEstimate() < bestPath) {
        bestPath = v.getEstimate();
      }
    }

    return bestPath;
  }


}
