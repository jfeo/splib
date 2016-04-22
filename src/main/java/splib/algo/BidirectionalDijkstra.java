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
  public static Integer singlePair(Heap<BDDVertex> hs, Heap<BDDVertex> ht,
      Graph<BDDVertex> G, BDDVertex s, BDDVertex t) {
    BidirectionalDijkstra.initializeSinglePair(G, s, t);
    PriorityQueue<BDDVertex> Qs = new PriorityQueue<BDDVertex>(hs);
    PriorityQueue<BDDVertex> Qt = new PriorityQueue<BDDVertex>(ht);
    ArrayList<BDDVertex> Ss = new ArrayList<BDDVertex>();
    ArrayList<BDDVertex> St = new ArrayList<BDDVertex>();

    Qs.insert(s, s.getEstimate());
    Qt.insert(t, t.getSuccessorEstimate());

    while (Qs.top() != Qt.top()) {
      BDDVertex u = Qs.extract();
      Ss.add(u);
      for (Pair<Vertex, Integer> v : u.getAdjacency()) {
        Dijkstra.relax(Qs, u, (BDDVertex)v.getItem1(), v.getItem2());
      }
      if (u == t){
        return Qs.top().getEstimate();
      }

      u = Qt.extract();
      St.add(u);
      for (Pair<Vertex, Integer> v : u.getAdjacency()) {
        BidirectionalDijkstra.relax(Qt, u, (BDDVertex)v.getItem1(), v.getItem2());
      }

      if (u == s){
        return Qt.top().getSuccessorEstimate();
      }
    }

    // Check if the shortest path found, is in the best shortest path
    int bestPath = Qs.top().getEstimate() + Qt.top().getSuccessorEstimate();
    for (BDDVertex v : Ss){
      if (v.getEstimate() + v.getSuccessorEstimate() < bestPath) {
        bestPath = v.getEstimate() + v.getSuccessorEstimate();
      }
    }

    for (BDDVertex v : St){
      if (v.getEstimate() + v.getSuccessorEstimate() < bestPath) {
        bestPath = v.getEstimate() + v.getSuccessorEstimate();
      }
    }

    return bestPath;
  }


  /**
   * Initialize the graph.
   */
  private static void initializeSinglePair(Graph<BDDVertex> G, BDDVertex s, BDDVertex t){
    for (BDDVertex v : G.getVertices()){
      v.setEstimate(Integer.MAX_VALUE);
      v.setSuccessorEstimate(Integer.MAX_VALUE);
      v.setPredecessor(null);
    }
    s.setEstimate(0);
    t.setSuccessorEstimate(0);
  }


  /**
   * Relax an edge.
   * @param Q The priority queue to use with the edge.
   * @param u The start vertex.
   * @param v The end vertex.
   * @param weight The weight of the edge.
   */
  public static void relax(PriorityQueue<BDDVertex> Q, BDDVertex u, BDDVertex v, int weight) {
    int newEstimate = u.getSuccessorEstimate() + weight;
    if (v.getSuccessorEstimate() > newEstimate) {
      v.setPredecessor(u);
      v.setEstimate(newEstimate);
      Q.insert(v, newEstimate);
      if (v.getIndex() != null) {
        Q.changeKey(v.getIndex(), newEstimate);
      }
    }
  }


  private static Integer checkShortestPath(ArrayList<BDDVertex> S,
      PriorityQueue<BDDVertex> Qs, PriorityQueue<BDDVertex> Qt) {
    int bestPath = Qs.top().getEstimate() + Qt.top().getSuccessorEstimate();
    S.removeAll(Collections.singleton(null));
    for (BDDVertex v : S) {
      if (v.getEstimate() < bestPath) {
        bestPath = v.getEstimate();
      }
    }

    return bestPath;
  }


}
