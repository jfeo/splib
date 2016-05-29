package splib.data;


import splib.data.Vertex;
import splib.data.SPVertex;
import splib.data.Vertex;


public class BDDVertex extends SPVertex {

  public enum Status {
    NotQueued,
    Queued,
    HasBeenQueued
  }

  protected BDDVertex succ;
  protected Double succEstimate;
  protected Status sstatus;
  protected Status tstatus;

  public BDDVertex() {
    super();
    this.succEstimate = 1.0d / 0.0d; // Infinity
    this.succ = null;
    this.sstatus = Status.NotQueued;
    this.tstatus = Status.NotQueued;
  }

  public BDDVertex(Double succEstimate, Double estimate, BDDVertex succ, BDDVertex pred) {
    super(pred, estimate);
    this.succ = succ;
    this.succEstimate = succEstimate;
    this.sstatus = Status.NotQueued;
    this.tstatus = Status.NotQueued;
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

  public Double getSuccessorEstimate() {
    return this.succEstimate;
  }

  public void setSourceStatus(Status s) {
    this.sstatus = s;
  }

  public Status getSourceStatus() {
    return this.sstatus;
  }

  public void setTargetStatus(Status s) {
    this.tstatus = s;
  }

  public Status getTargetStatus() {
    return this.tstatus;
  }
}
