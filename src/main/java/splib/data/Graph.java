package splib.data;

import java.util.ArrayList;
import java.lang.Integer;

import splib.util.Pair;

public class Graph<V extends Vertex> {
  /**
   * A simple graph, implemented as an adjacency list.
   */

  private ArrayList<V> vertices;
  private int edgeCount;

  public Graph() {
    this.vertices = new ArrayList<V>();
  }

  public int addVertex(V v) {
    this.vertices.add(v);
    return this.vertices.size() - 1;
  }

  public void removeVertex(V v) {
    this.vertices.remove(v);
    for (Pair<Vertex, Double> u : v.getAdjacency()) {
      u.getItem1().removeAdjacency(v);
    }
  }

  public void addEdge(int uIndex, int vIndex, double weight) {
    V u = this.vertices.get(uIndex);
    V v = this.vertices.get(vIndex);
    u.addAdjacency(v, weight);
    v.addAdjacency(u, weight);
    this.edgeCount++;
  }

  public ArrayList<V> getVertices() {
    return this.vertices;
  }

  public int getVertexCount() {
    return this.vertices.size();
  }

  public int getEdgeCount() {
    return this.edgeCount;
  }
}
