package splib.algo;


import splib.data.Graph;
import splib.data.SPVertex;
import splib.algo.Oracle;
import splib.data.Vertex;
import splib.util.Heap;
import splib.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.lang.Math;


public class ThorupZwick <V extends SPVertex> implements Oracle<V> {

  private static final Double DELTA = 1e-10;

  private int k;
  private ArrayList<ArrayList<Pair<Integer, Double>>> witnesses;
  private ArrayList<HashMap<Integer, Double>> bunches;

  public ThorupZwick(Integer k) {
    this.k = k;
  }

  public int getK() {
    return k;
  }

  public HashMap<Integer, Double> getBunch(int v) {
    return this.bunches.get(v);
  }

  public Pair<Integer, Double> getWitness(int v, int i) {
    return this.witnesses.get(v).get(i);
  }

  /**
   * Preprocess a graph.
   */
  public ArrayList<ArrayList<Integer>> preprocess(Graph<V> G, int heapArity) {
    ArrayList<ArrayList<Integer>> A = new ArrayList<ArrayList<Integer>>();
    A.add(new ArrayList<Integer>());

    // initialize vertices for this constant k
    ArrayList<HashMap<Integer, Double>> clusters = new ArrayList<HashMap<Integer, Double>>();
    this.witnesses = new ArrayList<ArrayList<Pair<Integer, Double>>>();
    this.bunches = new ArrayList<HashMap<Integer, Double>>();
    for (int v = 0; v < G.getVertexCount(); v++) {
      this.witnesses.add(new ArrayList<Pair<Integer, Double>>());
      for (int i = 0; i < k; i++) {
        this.witnesses.get(v).add(new Pair(null, null));
      }
      this.witnesses.get(v).add(new Pair(null, 1d/0d));
      this.bunches.add(new HashMap<Integer, Double>());
      clusters.add(new HashMap<Integer, Double>());
      A.get(0).add(v);
    }

    // Compute i-centers
    for (int i = 1; i < k; i++) {
      A.add(new ArrayList<Integer>());

      ArrayList<Integer> preA = A.get(i-1);
      ArrayList<Integer> curA = A.get(i);

      double thresh = Math.pow(G.getVertexCount(),
                               0 - 1.0d / (double)this.k);
      for (Integer v : preA) {
        if (Math.random() < thresh) {
          curA.add(v);
        }
      }

      // make sure the i-center is not empty, and refill it if it is
      if (curA.isEmpty()) {
        i--;
        continue;
      }
    }
    A.add(new ArrayList<Integer>());

    // Compute clusters
    for (int i = k - 1; i >= 0; i--) {
      // Compute witnesses
      // Insert 'fake' vertex with 0 edges to vertices in A_i, for SSSP
      int s = G.addVertex((V)new SPVertex());
      for (Integer w : A.get(i)) {
        G.addEdge(s, w, 0.0);
        this.witnesses.get(w).set(i, new Pair(w, 0.0));
      }

      // Perform SSSP from 'fake' vertex, and find witnesses of all vertices
      Dijkstra.<V>singleSource(G, s, heapArity);
      for (int v = 0; v < G.getVertexCount() - 1; v++) {
        Pair<Integer, Double> preWitness = this.witnesses.get(v).get(i+1);
        if (Math.abs(G.getVertex(v).getEstimate() - preWitness.getItem2()) < DELTA) { // double comparison
          this.witnesses.get(v).set(i, new Pair(preWitness.getItem1(), G.getVertex(v).getEstimate()));
        } else {
          int cand = v;
          // follow the path back to the i-center, in order to find the closest
          // member of A_i to set as witness
          while (G.getVertex(cand).getPredecessor() != null && G.getVertex(cand).getPredecessor() != s) {
            cand = G.getVertex(cand).getPredecessor();
          }
          this.witnesses.get(v).set(i, new Pair(cand, G.getVertex(v).getEstimate()));
        }
      }
      G.popVertex();

      // Compute clusters
      ArrayList<Integer> tmp = (ArrayList<Integer>)A.get(i).clone();
      tmp.removeAll(A.get(i+1));
      for (Integer w : tmp) {
        for (Integer v : this.singleSource(G, w, i, heapArity)) {
          clusters.get(w).put(v, G.getVertex(v).getEstimate());
        }
      }
    }

    // Compute bunches
    for (int v = 0; v < G.getVertexCount(); v++) {
      for (int w = 0; w < G.getVertexCount(); w++) {
        if (clusters.get(w).containsKey(v)) {
          this.bunches.get(v).put(w, clusters.get(w).get(v));
        }
      }
    }

    return A;
  }


  /**
   * Query the distance between two vertices.
   * @param u The vertex on one end of the path.
   * @param v The vertex on the other end of the path.
   */
  public Double query(int u, int v) {
    int w = u;

    int i = 0;
    while (!this.bunches.get(v).containsKey(w)) {
      i++;
      if (i == this.k) {
        return 1d / 0d;
      }
      int vSwap = v;
      v = u;
      u = vSwap;
      w = this.witnesses.get(u).get(i).getItem1();
    }

    return this.witnesses.get(u).get(i).getItem2() + this.bunches.get(v).get(w);
  }


  /**
   * Single source dijkstra, modified so relaxation occurs something with A.
   * @param s The source vertex. Undefined behaviour, if this vertex is not in G.
   * @param i The index of the A set.
   * @return The vertices.
   */
  private ArrayList<Integer> singleSource(Graph<V> G, int s, int i, int heapArity) {
    this.initializeSingleSource(G, s);
    ArrayList<Integer> S = new ArrayList<Integer>();
    Heap<Integer> Q = new Heap<Integer>((v, u) -> {
      return G.getVertex(v).getEstimate().compareTo(G.getVertex(u).getEstimate());
    }, heapArity);

    Q.insert(s);

    // Relax edges adjacent to the minimum estimate distance vertex
    while (!Q.isEmpty()) {
      Integer u = Q.extract();
      S.add(u);
      for (Pair<Integer, Double> edge : G.getAdjacency(u)) {
        this.relax(G, Q, u, edge.getItem1(), edge.getItem2(), i);
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
  private void relax(Graph<V> G, Heap<Integer> Q, int u, int v, double weight, int i) {
    double newEstimate = G.getVertex(u).getEstimate() + weight;
    if (newEstimate < G.getVertex(v).getEstimate()
        && newEstimate < this.witnesses.get(v).get(i+1).getItem2()) {
      G.getVertex(v).setPredecessor(u);
      G.getVertex(v).setEstimate(newEstimate);
      Q.insert(v);
    }
  }


  /**
   * Set all vertices in the graph to have an estimate distance of the max
   * integer value, and a null predecessor, and set the source vertex to have
   * an estimate distance of 0.
   * @param G The graph to work on.
   * @param s The source vertex.
   */
  private void initializeSingleSource(Graph<V> G, Integer s) {
    for (int v = 0; v < G.getVertexCount(); v++) {
      G.getVertex(v).setEstimate(1.0f / 0.0f); // Infinity
      G.getVertex(v).setPredecessor(null);
    }
    G.getVertex(s).setEstimate(0);
  }


}
