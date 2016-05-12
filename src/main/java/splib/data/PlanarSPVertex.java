package splib.data;

import splib.util.Pair;
import splib.data.Vertex;
import java.util.ArrayList;
import java.lang.Comparable;

public class PlanarSPVertex extends SPVertex {

  protected Pair<Double, Double> position;

  public PlanarSPVertex(double x, double y) {
    super();
    this.position = new Pair(x, y);
  }

  public PlanarSPVertex(PlanarSPVertex pred, double estimate, double x, double y) {
    super(pred, estimate);
    this.position = new Pair(x, y);
  }

  public Pair<Double, Double> getPosition() {
    return this.position;
  }

}
