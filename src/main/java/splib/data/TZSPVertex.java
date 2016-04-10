package splib.data;

import splib.util.Pair;
import splib.data.Vertex;
import java.util.ArrayList;

public class TZSPVertex extends SPVertex {


  protected ArrayList<Pair<TZSPVertex, Integer>> witnesses;


  public TZSPVertex(SPVertex prev, int estimate) {
    super(prev, estimate);
    this.witnesses = new ArrayList<Pair<TZSPVertex, Integer>>();
  }


  public TZSPVertex() {
    super();
    this.witnesses = new ArrayList<Pair<TZSPVertex, Integer>>();
  }


  public Pair<TZSPVertex, Integer> getWitness(int i) {
    return this.witnesses.get(i);
  }

  public void setWitness(int i, TZSPVertex witness, Integer weight) {
    while (i >= this.witnesses.size()) {
      this.witnesses.add(null);
    }
    this.witnesses.set(i, new Pair<TZSPVertex, Integer>(witness, weight));
  }


}
