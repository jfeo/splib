package splib.data;

import splib.util.Pair;
import splib.data.Vertex;
import java.util.ArrayList;
import java.lang.Comparable;

public class SPVertex extends Vertex {

  protected Integer pred;
  protected Double estimate;

  public SPVertex(Integer pred, double estimate) {
    super();
    this.pred = pred;
    this.estimate = estimate;
  }

  public SPVertex() {
    super();
    this.pred = null;
    this.estimate = 1.0d / 0.0d; // Infinity
  }

  public Integer getPredecessor() {
    return this.pred;
  }

  public void setPredecessor(Integer v) {
    this.pred = v;
  }

  public Double getEstimate() {
    return this.estimate;
  }

  public void setEstimate(double e) {
    this.estimate = e;
  }

  // public boolean isAdjacent(SPVertex v) {
  //   return super.isAdjacent((Vertex)v);
  // }

}
