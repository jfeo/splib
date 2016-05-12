package splib.data;

import splib.util.Pair;
import splib.data.Vertex;
import splib.data.SPVertex;
import java.util.ArrayList;

public class TZSPVertex extends SPVertex {


  protected ArrayList<Pair<TZSPVertex, Double>> witnesses;


  public TZSPVertex(SPVertex prev, double estimate, int k) {
    super(prev, estimate);
    this.witnesses = new ArrayList<Pair<TZSPVertex, Double>>();
    for (int i = 0; i <= k; i++) {
      witnesses.add(new Pair(null, null));
    }
  }


  public TZSPVertex(int k) {
    super();
    this.witnesses = new ArrayList<Pair<TZSPVertex, Double>>();
    for (int i = 0; i <= k; i++) {
      witnesses.add(new Pair(null, null));
    }
  }


  public Pair<TZSPVertex, Double> getWitness(int i) {
    return this.witnesses.get(i);
  }


  public void setWitness(int i, TZSPVertex witness, double weight) {
    this.witnesses.set(i, new Pair<TZSPVertex, Double>(witness, weight));
  }


}
