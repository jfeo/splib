package splib.algo;


import splib.data.Graph;
import splib.data.SPVertex;
import splib.data.TZSPVertex;
import splib.algo.Oracle;
import splib.data.Vertex;
import splib.util.Heap;
import splib.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.lang.Math;



public class ThorupZwick <V extends TZSPVertex> implements Oracle<V> {

  private static final Double DELTA = 1e-10;

  private Graph<V> G;
  private int k;
  private int heapArity;
  private ArrayList<ArrayList<V>> A;

  public ThorupZwick(Integer k, Graph<V> G, Integer heapArity) {
    this.k = k;
    this.G = G;
    this.heapArity = heapArity;
    this.A = new ArrayList<ArrayList<V>>();
    this.preprocess();
  }

  public ArrayList<ArrayList<V>> getA() {
    return A;
  }

  public int getK() {
    return k;
  }

  public int getHeapArity() {
    return heapArity;
  }

  /**
   * Preprocess a graph.
   */
  private void preprocess() {

    // initialize vertices for this constant k
    for (V v : this.G.getVertices()) {
      v.initialize(this.k);
    }

    this.A.add(new ArrayList<V>());
    this.A.get(0).addAll(this.G.getVertices());

    // Compute i-centers
    for (int i = 1; i < k; i++) {
      this.A.add(new ArrayList<V>());

      ArrayList<V> preA = this.A.get(i-1);
      ArrayList<V> curA = this.A.get(i);

      double thresh = Math.pow(this.G.getVertices().size(),
                               0 - 1.0d / (double)this.k);
      for (V v : preA) {
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
    this.A.add(new ArrayList<V>());

    // Compute clusters
    for (int i = k - 1; i >= 0; i--) {
      // Compute witnesses
      // Insert 'fake' vertex with 0 edges to vertices in A_i, for SSSP
      V s = (V)new TZSPVertex();
      s.initialize(this.k);
      for (V w : this.A.get(i)) {
        this.G.addEdge(s, w, 0.0);
        w.setWitness(i, w, 0.0);
      }

      // Perform SSSP from 'fake' vertex, and find witnesses of all vertices
      Dijkstra.<V>singleSource(this.G, s, this.heapArity);
      for (V v : this.G.getVertices()) {
        Pair<TZSPVertex, Double> preWitness = v.getWitness(i+1);
        if (Math.abs(v.getEstimate() - preWitness.getItem2()) < DELTA) { // double comparison
          v.setWitness(i, preWitness.getItem1(), v.getEstimate());
        } else {
          V candidate = v;
          // follow the path back to the i-center, in order to find the closest
          // member of A_i to set as witness
          while (candidate.getPredecessor() != s && candidate.getPredecessor() != null) {
            candidate = (V)candidate.getPredecessor();
          }
          v.setWitness(i, candidate, v.getEstimate());
        }
      }
      this.G.removeVertex(s);

      // Compute clusters
      ArrayList<V> tmp = (ArrayList<V>)A.get(i).clone();
      tmp.removeAll(this.A.get(i+1));
      for (V w : tmp) {
        for (V v : this.singleSource(w, i)) {
          w.getCluster().put(v, v.getEstimate());
        }
      }
    }

    // Compute bunches
    for (V v : this.G.getVertices()) {
      for (V w : this.G.getVertices()) {
        if (w.getCluster().containsKey(v)) {
          v.getBunch().put(w, w.getCluster().get(v));
        }
      }
    }
  }


  /**
   * Query the distance between two vertices.
   * @param u The vertex on one end of the path.
   * @param v The vertex on the other end of the path.
   */
  public Double query(V u, V v) {
    V w = u;

    int i = 0;
    while (!v.getBunch().containsKey(w)) {
      i++;
      V vSwap = v;
      v = u;
      u = vSwap;
      w = (V)u.getWitness(i).getItem1();
    }

    return u.getWitness(i).getItem2() + v.getBunch().get(w);
  }


  /**
   * Single source dijkstra, modified so relaxation occurs something with A.
   * @param s The source vertex. Undefined behaviour, if this vertex is not in G.
   * @param i The index of the A set.
   * @return The vertices.
   */
  private ArrayList<V> singleSource(V s, int i) {
    this.initializeSingleSource(s);
    ArrayList<V> S = new ArrayList<V>();
    Heap<V> Q = new Heap<V>((V v, V u) -> {
      return v.getEstimate().compareTo(u.getEstimate());
    }, this.heapArity);

    Q.insert(s);

    // Relax edges adjacent to the minimum estimate distance vertex
    while (!Q.isEmpty()) {
      V u = Q.extract();
      S.add(u);
      for (Pair<Vertex, Double> v : u.getAdjacency()) {
        this.relax(Q, u, (V)v.getItem1(), v.getItem2(), i);
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
  private void relax(Heap<V> Q, V u, V v, double weight, int i) {
    double newEstimate = u.getEstimate() + weight;
    if (newEstimate < v.getEstimate()
        && newEstimate < v.getWitness(i+1).getItem2()) {
      v.setPredecessor(u);
      v.setEstimate(newEstimate);
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
  private void initializeSingleSource(V s) {
    for (V v : this.G.getVertices()) {
      v.setEstimate(1.0f / 0.0f); // Infinity
      v.setPredecessor(null);
    }
    s.setEstimate(0);
  }


}
