package splib.data;


import splib.data.Vertex;
import splib.data.SPVertex;
import splib.data.Vertex;


public class BDDVertex extends SPVertex {

  protected BDDVertex succ;
  protected int succEstimate;

  public BDDVertex() {
    super();
    this.succEstimate = Integer.MAX_VALUE;
    this.succ = null;
  }

  public BDDVertex(int succEstimate, int estimate, BDDVertex succ, BDDVertex pred) {
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

  public void setSuccessorEstimate(Integer estimate) {
    this.succEstimate = estimate;
  }

  public int getSuccessorEstimate() {
    return this.succEstimate;
  }

}
