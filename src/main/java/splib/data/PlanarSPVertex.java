package splib.data;

import splib.util.Pair;
import splib.data.Vertex;
import java.util.ArrayList;
import java.lang.Comparable;

public class PlanarSPVertex extends SPVertex {

  public enum Status {
    Neither,
    Open,
    Closed
  }

  protected Pair<Double, Double> position;
  protected Status status;

  public PlanarSPVertex(double x, double y) {
    super();
    this.position = new Pair(x, y);
    this.status = Status.Neither;
  }

  public PlanarSPVertex(PlanarSPVertex pred, double estimate, double x, double y) {
    super(pred, estimate);
    this.position = new Pair(x, y);
    this.status = Status.Neither;
  }

  public PlanarSPVertex() {
    super();
    this.status = Status.Neither;
  }

  public void setPosition(double x, double y) {
    this.position = new Pair<Double, Double>(x, y);
  }


  public Pair<Double, Double> getPosition() {
    return this.position;
  }

  public void Close() {
    this.status = Status.Closed;
  }

  public boolean isClosed() {
    return this.status == Status.Closed;
  }

  public void Open() {
    this.status = Status.Open;
  }

  public boolean isOpen() {
    return this.status == Status.Open;
  }

}
