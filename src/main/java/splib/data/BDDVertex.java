package splib.data;


import splib.data.Vertex;
import splib.data.SPVertex;
import splib.data.Vertex;


public class BDDVertex extends SPVertex {

  protected BDDVertex succ;
  protected double succEstimate;

  public BDDVertex() {
    super();
    this.succEstimate = 1.0d / 0.0d; // Infinity
    this.succ = null;
  }

  public BDDVertex(double succEstimate, double estimate, BDDVertex succ, BDDVertex pred) {
    super(pred, estimate);
    this.succ = succ;
    this.succEstimate = succEstimate;
  }

  public void setSuccessor(BDDVertex succ) {
    this.succ = succ;
  }

  public BDDVertex getSuccessor() {
    return this.succ;
  }

  public void setSuccessorEstimate(Double estimate) {
    this.succEstimate = estimate;
  }

  public double getSuccessorEstimate() {
    return this.succEstimate;
  }

}
