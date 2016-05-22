package splib.algo;


import splib.data.Graph;
import splib.data.SPVertex;
import splib.data.TZSPVertex;
import splib.data.Vertex;
import splib.util.Heap;
import splib.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.lang.Math;



public class ThorupZwick <V extends TZSPVertex> {

  private Graph<V> G;
  private int k;
  private int heapArity;
  private ArrayList<ArrayList<V>> A;
  private HashMap<V, ArrayList<V>> B;
  private HashMap<V, ArrayList<V>> C;

  public ThorupZwick(int k, Graph<V> G, int heapArity) {
    this.k = k;
    this.G = G;
    this.heapArity = heapArity;
    this.A = new ArrayList<ArrayList<V>>();
    this.B = new HashMap<V, ArrayList<V>>();
    this.C = new HashMap<V, ArrayList<V>>();
    this.preprocess();
  }

  /**
   * Preprocess a graph.
   */
  public void preprocess() {
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
    }
    this.A.add(new ArrayList<V>());

    for (int i = k - 1; i >= 0; i--) {
      // Compute witnesses
      // Insert 'fake' vertex with 0 edges to vertices in A_i, for SSSP
      V s = (V)new TZSPVertex(this.k);
      for (V w : this.A.get(i)) {
        this.G.addEdge(s, w, 0.0);
        w.setWitness(i, w, 0.0);
      }

      // Perform SSSP from 'fake' vertex, and find witnesses of all vertices
      Dijkstra.<V>singleSource(this.G, s, this.heapArity);
      for (V v : this.G.getVertices()) {
        Pair<TZSPVertex, Double> preWitness = v.getWitness(i+1);
        if ((Double)v.getEstimate() == preWitness.getItem2()) {
          v.setWitness(i, preWitness.getItem1(), v.getEstimate());
        } else {
          V witness = (V)v.getPredecessor();
            while (witness != null
                && witness.getWitness(i).getItem1() != witness) {
              witness = (V)witness.getPredecessor();
            }
            v.setWitness(i, witness, v.getEstimate());
        }
      }
      this.G.removeVertex(s);

      // Compute clusters
      ArrayList<V> tmp = (ArrayList<V>)A.get(i).clone();
      tmp.removeAll(this.A.get(i+1));
      for (V w : tmp) {
        this.singleSource(w, i);
        // C(w) = { v in V | d(w, v) < d(A_i+1, v) }
        ArrayList<V> wC = new ArrayList<V>();
        for (V v : this.G.getVertices()) {
          if (v.getWitness(i+1).getItem1() != null
              && v.getEstimate() < v.getWitness(i+1).getItem2()) {
            wC.add(v);
          }
        }
        C.put(w, wC);
      }
    }

    // Compute bunches
    for (V v : this.G.getVertices()) {
      ArrayList<V> vB = new ArrayList<V>();
      for (V w : this.G.getVertices()) {
        if (this.C.get(w).contains(v)) {
          vB.add(w);
        }
      }
      this.B.put(v, vB);
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
    while (!B.get(v).contains(w)) {
      i++;
      V tmp = v;
      v = u;
      u = tmp;
      w = (V)u.getWitness(i).getItem1();
    }

    return 0.0;
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
    for (V v : this.G.getVertices()) {
      Q.insert(v);
    }
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
    if (v.getWitness(i+1).getItem1() != null
        && v.getWitness(i+1).getItem2() > newEstimate) {
      v.setPredecessor(u);
      v.setEstimate(newEstimate);
      Q.changeKey(v);
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
