package splib.algo;


import splib.data.Graph;
import splib.data.SPVertex;
import splib.data.TZSPVertex;
import splib.data.Vertex;
import splib.util.Heap;
import splib.util.PriorityQueue;
import splib.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.lang.Math;



public class ThorupZwick {

  private Graph<TZSPVertex> G;
  private int k;
  private Heap<TZSPVertex> h;
  private ArrayList<ArrayList<TZSPVertex>> A;
  private HashMap<TZSPVertex, ArrayList<TZSPVertex>> B;
  private HashMap<TZSPVertex, ArrayList<TZSPVertex>> C;

  public ThorupZwick(int k, Graph<TZSPVertex> G, Heap<TZSPVertex> h) {
    this.k = k;
    this.G = G;
    this.h = h;
    this.A = new ArrayList<ArrayList<TZSPVertex>>();
    this.B = new HashMap<TZSPVertex, ArrayList<TZSPVertex>>();
    this.C = new HashMap<TZSPVertex, ArrayList<TZSPVertex>>();
    this.preprocess();
    System.out.println("Done preprocessing!");
  }	
  /**
   * Preprocess a graph.
   */
  public void preprocess() {
    this.A.add(new ArrayList<TZSPVertex>());
    this.A.get(0).addAll(this.G.getVertices());
  

    // Compute i-centers
    for (int i = 1; i < k; i++) {
      this.A.add(new ArrayList<TZSPVertex>());

      ArrayList<TZSPVertex> preA = this.A.get(i-1);
      ArrayList<TZSPVertex> curA = this.A.get(i);

      for (TZSPVertex v : preA) {
        if (Math.random() < Math.pow(curA.size(), - 1 / (double)k)) {
          curA.add(v);
        }
      }
    }
    this.A.add(new ArrayList<TZSPVertex>());

    for (int i = k - 1; i >= 0; i--) {
      // Compute witnesses
      // Insert 'fake' vertex with 0 edges to vertices in A_i, for SSSP
      TZSPVertex s = new TZSPVertex(this.k);
      for (TZSPVertex w : this.A.get(i)) {
        this.G.addEdge(s, w, 0.0);
        w.setWitness(i, w, 0.0);
      }

      // Perform SSSP from 'fake' vertex, and find witnesses of all vertices
      Dijkstra.<TZSPVertex>singleSource(this.h, this.G, s);
      for (TZSPVertex v : this.G.getVertices()) {
    	Pair<TZSPVertex, Double> preWitness = v.getWitness(i+1);
    	if ((Double)v.getEstimate() == preWitness.getItem2()) {
    		v.setWitness(i, preWitness.getItem1(), v.getEstimate());
    	} else {
    		TZSPVertex witness = (TZSPVertex)v.getPredecessor();
        	while (witness != null && witness.getWitness(i).getItem1() != witness) {
        		witness = (TZSPVertex)witness.getPredecessor();
        	}
            v.setWitness(i, witness, v.getEstimate());	
    	}
      }
      this.G.removeVertex(s);

      // Compute clusters
      ArrayList<TZSPVertex> tmp = (ArrayList<TZSPVertex>)A.get(i).clone();
      tmp.removeAll(this.A.get(i+1));
      for (TZSPVertex w : tmp) {
        this.singleSource(w, i);
        // C(w) = { v in V | d(w, v) < d(A_i+1, v) }
        ArrayList<TZSPVertex> wC = new ArrayList<TZSPVertex>();
        for (TZSPVertex v : this.G.getVertices()) {
          if (v.getWitness(i+1).getItem1() != null && v.getEstimate() < v.getWitness(i+1).getItem2()) {
            wC.add(v);
          }
        }
        C.put(w, wC);
      }
    }

    // Compute bunches
    for (TZSPVertex v : this.G.getVertices()) {
    	ArrayList<TZSPVertex> vB = new ArrayList<TZSPVertex>();
    	for (TZSPVertex w : this.G.getVertices()) {
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
  public Double query(TZSPVertex u, TZSPVertex v) {
    TZSPVertex w = u;

    int i = 0;
    while (!B.get(v).contains(w)) {
      i++;
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
    if (v.getWitness(i+1).getItem1() != null && v.getWitness(i+1).getItem2() > newEstimate) {
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
