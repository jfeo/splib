package splib.data;

import splib.util.Pair;
import splib.data.Vertex;
import java.util.ArrayList;

public class SPVertex extends Vertex {

  protected SPVertex pred;
  protected int estimate;

  public SPVertex(SPVertex pred, int estimate) {
    super();
    this.pred = pred;
    this.estimate = estimate;
  }

  public SPVertex() {
    super();
    this.pred = null;
    this.estimate = Integer.MAX_VALUE;
  }

  public SPVertex getPredecessor() {
    return this.pred;
  }

  public void setPredecessor(SPVertex v) {
    this.pred = v;
  }

  public int getEstimate() {
    return this.estimate;
  }

  public void setEstimate(int e) {
    this.estimate = e;
  }

  public void addAdjacency(SPVertex v, Integer w) {
    super.addAdjacency((Vertex)v, w);
  }

  public boolean isAdjacent(SPVertex v) {
    return super.isAdjacent((Vertex)v);
  }

}
