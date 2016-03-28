package splib.data;

import java.util.ArrayList;
import java.lang.Integer;

import splib.util.Pair;

public class Graph<V extends Vertex> {
  /**
   * A simple graph, implemented as an adjacency list.
   */

  public ArrayList<V> vertices;

  public Graph() {
    this.vertices = new ArrayList<V>();
  }

  public int addVertex(V v)
  {
    this.vertices.add(v);
    return this.vertices.size() - 1;
  }

  public void addEdge(int uIndex, int vIndex, int weight) {
    V u = this.vertices.get(uIndex);
    V v = this.vertices.get(vIndex);
    u.addAdjacency(v, weight);
    v.addAdjacency(u, weight);
  }

  public ArrayList<V> getVertices()
  {
    return this.vertices;
  }
}
