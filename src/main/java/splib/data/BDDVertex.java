package splib.data;


import splib.util.Heap.Index;
import splib.data.SPVertex;
import splib.data.Vertex;


public class BDDVertex extends SPVertex {

  public enum Status {
    NotQueued,
    Queued,
    HasBeenQueued
  }

  protected Integer succ;
  protected Double succEstimate;
  protected Status sstatus;
  protected Status tstatus;
  protected Index sIndex;
  protected Index tIndex;

  public BDDVertex() {
    super();
    this.succEstimate = 1.0d / 0.0d; // Infinity
    this.succ = null;
    this.sstatus = Status.NotQueued;
    this.tstatus = Status.NotQueued;
    this.sIndex = null;
    this.tIndex = null;
  }

  public BDDVertex(Double succEstimate, Double estimate, Integer succ, Integer pred) {
    super(pred, estimate);
    this.succ = succ;
    this.succEstimate = succEstimate;
    this.sstatus = Status.NotQueued;
    this.tstatus = Status.NotQueued;
  }

  public void setSourceIndex(Index sIndex) {
    this.sIndex = sIndex;
  }

  public void setTargetIndex(Index tIndex) {
    this.tIndex = tIndex;
  }

  public Index getSourceIndex() {
    return this.sIndex;
  }

  public Index getTargetIndex() {
    return this.tIndex;
  }

  public void setSuccessor(Integer succ) {
    this.succ = succ;
  }

  public Integer getSuccessor() {
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
