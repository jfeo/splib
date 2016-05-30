package splib.data;

import splib.util.Pair;
import splib.data.Vertex;
import splib.data.SPVertex;

import java.util.ArrayList;
import java.util.HashMap;


public class TZSPVertex extends SPVertex {


  protected ArrayList<Pair<TZSPVertex, Double>> witnesses;
  protected HashMap<TZSPVertex, Double> bunch;
  protected HashMap<TZSPVertex, Double> cluster;


  public TZSPVertex(SPVertex prev, double estimate) {
    super(prev, estimate);
    this.bunch = new HashMap<TZSPVertex, Double>();
    this.cluster = new HashMap<TZSPVertex, Double>();
    this.witnesses = new ArrayList<Pair<TZSPVertex, Double>>();
  }


  public TZSPVertex() {
    super();
    this.bunch = new HashMap<TZSPVertex, Double>();
    this.cluster = new HashMap<TZSPVertex, Double>();
    this.witnesses = new ArrayList<Pair<TZSPVertex, Double>>();
  }


  public void initialize(int k) {
    this.bunch.clear();
    this.cluster.clear();
    this.witnesses.clear();
    for (int i = 0; i < k; i++) {
      witnesses.add(new Pair(null, null));
    }
    witnesses.add(new Pair(null, 1d/0d));
  }

  public Pair<TZSPVertex, Double> getWitness(int i) {
    return this.witnesses.get(i);
  }


  public void setWitness(int i, TZSPVertex witness, double weight) {
    this.witnesses.set(i, new Pair<TZSPVertex, Double>(witness, weight));
  }


  public HashMap<TZSPVertex, Double> getBunch() {
    return this.bunch;
  }


  public HashMap<TZSPVertex, Double> getCluster() {
    return this.cluster;
  }


}
