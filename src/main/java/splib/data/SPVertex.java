package splib.data;

import splib.util.Pair;
import splib.data.Vertex;
import java.util.ArrayList;
import java.lang.Comparable;

public class SPVertex extends Vertex {

  protected SPVertex pred;
  protected double estimate;

  public SPVertex(SPVertex pred, double estimate) {
    super();
    this.pred = pred;
    this.estimate = estimate;
  }

  public SPVertex() {
    super();
    this.pred = null;
    this.estimate = Double.MAX_VALUE;
  }

  public SPVertex getPredecessor() {
    return this.pred;
  }

  public void setPredecessor(SPVertex v) {
    this.pred = v;
  }

  public double getEstimate() {
    return this.estimate;
  }

  public void setEstimate(double e) {
    this.estimate = e;
  }

  public boolean isAdjacent(SPVertex v) {
    return super.isAdjacent((Vertex)v);
  }

}
