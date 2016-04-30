package splib.algo;


import splib.data.Graph;
import splib.data.TZSPVertex;
import splib.data.Vertex;
import splib.util.Heap;
import splib.util.PriorityQueue;
import splib.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.lang.Math;



public class ThorupZwick {

  private Graph<TZSPVertex> G;
  private int k;
  private Heap<TZSPVertex> h;
  private ArrayList<HashSet<TZSPVertex>> A;
  private HashMap<TZSPVertex, ArrayList<TZSPVertex>> B;
  private HashMap<TZSPVertex, ArrayList<TZSPVertex>> C;

  public ThorupZwick(int k, Graph<TZSPVertex> G, Heap<TZSPVertex> h) {
    this.k = k;
    this.G = G;
    this.h = h;
    this.A = new ArrayList<HashSet<TZSPVertex>>();
    this.B = new HashMap<TZSPVertex, ArrayList<TZSPVertex>>();
    this.C = new HashMap<TZSPVertex, ArrayList<TZSPVertex>>();
    this.preprocess();
  }

  /**
   * Preprocess a graph.
   */
  public void preprocess() {
    this.A.add(new HashSet<TZSPVertex>());
    this.A.get(0).addAll(this.G.getVertices());
    Random rand = new Random();

    // Compute i-centers
    for (int i = 1; i < k - 1; i++) {
      this.A.add(new HashSet<TZSPVertex>());

      HashSet<TZSPVertex> preA = this.A.get(i-1);
      HashSet<TZSPVertex> curA = this.A.get(i);

      for (TZSPVertex v : preA) {
        if (Math.random() < Math.pow(curA.size(), - 1 / (double)k)) {
          curA.add(v);
        }
      }
    }

    for (int i = k - 1; i >= 0; i--) {

      // Compute witnesses
      // Insert new source vertex
      int sIndex = this.G.addVertex(new TZSPVertex());
      for (TZSPVertex w = this.A.get(i).iterator().next(); this.A.get(i).iterator().hasNext();) {

      }

      for (TZSPVertex v : this.G.getVertices()) {
        // compute d(A_i, v) and find p_i(v) in A_i such that d(p_i(v), v) = d(A_i, v)
        double witnessWeight = Double.MAX_VALUE;
        TZSPVertex witness = null;
        for (TZSPVertex w = this.A.get(i).iterator().next(); this.A.get(i).iterator().hasNext();) {
          if (w.getEstimate() < witness.getEstimate()) {
            witness = w;
          }
        }
        v.setWitness(i, witness, witness.getEstimate());
      }

      // Compute clusters
      HashSet<TZSPVertex> tmp = (HashSet<TZSPVertex>)A.get(i).clone();
      tmp.removeAll(this.A.get(i+1));
      for (TZSPVertex w = tmp.iterator().next(); tmp.iterator().hasNext();) {
        this.singleSource(w, i);
        // C(w) = { v in V | d(w, v) < d(A_i+1, v) }
        ArrayList<TZSPVertex> wC = new ArrayList<TZSPVertex>();
        for (TZSPVertex v : this.G.getVertices()) {
          if (v.getEstimate() < v.getWitness(i+1).getItem2()) {
            wC.add(v);
          }
        }
        C.put(w, wC);
      }
    }

    // Compute bunches
    HashSet<TZSPVertex> tmp = (HashSet<TZSPVertex>)A.get(0).clone();
    tmp.removeAll(this.A.get(1));
    for (TZSPVertex v = tmp.iterator().next(); tmp.iterator().hasNext();) {
      // C(w) = { v in V | d(w, v) < d(A_i+1, v) }
      ArrayList<TZSPVertex> vB = new ArrayList<TZSPVertex>();
      for (TZSPVertex w : this.G.getVertices()) {
        for (TZSPVertex c : this.C.get(w)) {
          if (w == c) {
            vB.add(w);
          }
        }
      }
      B.put(v, vB);
    }
  }


  /**
   * Query the distance between two vertices.
   * @param u The vertex on one end of the path.
   * @param v The vertex on the other end of the path.
   */
  public Double query(TZSPVertex u, TZSPVertex v) {
    TZSPVertex w = u;
    int i = 0;

    while (!B.get(v).contains(w)) {
      i += 1;
      TZSPVertex tmp = v;
      v = u;
      u = tmp;
      w = u.getWitness(i).getItem1();
    }

    return 0.0;
  }


  /**
   * Single source dijkstra, modified so relaxation occurs something with A.
   * @param s The source vertex. Undefined behaviour, if this vertex is not in G.
   * @param i The index of the A set.
   * @return The vertices.
   */
  private ArrayList<TZSPVertex> singleSource(TZSPVertex s, int i) {
    this.initializeSingleSource(s);
    ArrayList<TZSPVertex> S = new ArrayList<TZSPVertex>();
    PriorityQueue<TZSPVertex> Q = new PriorityQueue<TZSPVertex>(this.h);
    for (TZSPVertex v : this.G.getVertices()) {
      Q.insert(v);
    }
    // Relax edges adjacent to the minimum estimate distance vertex
    while (!Q.isEmpty()) {
      TZSPVertex u = Q.extract();
      S.add(u);
      for (Pair<Vertex, Double> v : u.getAdjacency()) {
        this.relax(Q, u, (TZSPVertex)v.getItem1(), v.getItem2(), i);
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
  private void relax(PriorityQueue Q, TZSPVertex u, TZSPVertex v, double weight, int i) {
    double newEstimate = u.getEstimate() + weight;
    if (v.getWitness(i+1).getItem2() > newEstimate) {
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
  private void initializeSingleSource(TZSPVertex s) {
    for (TZSPVertex v : this.G.getVertices()) {
      v.setEstimate(Double.MAX_VALUE);
      v.setPredecessor(null);
    }
    s.setEstimate(0);
  }


}
