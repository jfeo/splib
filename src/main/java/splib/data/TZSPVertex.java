package splib.data;

import splib.util.Pair;
import splib.data.Vertex;
import java.util.ArrayList;

public class TZSPVertex extends SPVertex {


  protected ArrayList<Pair<TZSPVertex, Double>> witnesses;


  public TZSPVertex(SPVertex prev, double estimate) {
    super(prev, estimate);
    this.witnesses = new ArrayList<Pair<TZSPVertex, Double>>();
  }


  public TZSPVertex() {
    super();
    this.witnesses = new ArrayList<Pair<TZSPVertex, Double>>();
  }


  public Pair<TZSPVertex, Double> getWitness(int i) {
    return this.witnesses.get(i);
  }

  public void setWitness(int i, TZSPVertex witness, double weight) {
    while (i >= this.witnesses.size()) {
      this.witnesses.add(null);
    }
    this.witnesses.set(i, new Pair<TZSPVertex, Double>(witness, weight));
  }


}
