package splib.data;

import java.lang.Integer;
import java.util.ArrayList;
import java.util.Iterator;

import splib.util.Pair;

public class Graph<V extends Vertex> {
  /**
   * A simple graph, implemented as an adjacency list.
   */

  private ArrayList<V> vertices;
  private int edgeCount;

  private ArrayList<ArrayList<Pair<Integer, Double>>> adjacency;

  public Graph() {
    this.vertices = new ArrayList<V>();
    this.adjacency = new ArrayList<ArrayList<Pair<Integer, Double>>>();
  }

  public int addVertex(V v) {
    this.vertices.add(v);
    this.adjacency.add(new ArrayList<Pair<Integer, Double>>());
    return this.vertices.size() - 1;
  }

  public void popVertex() {
    for (int i = 0; i < this.adjacency.size() - 1; i++) {
      for (int e = 0; e < this.adjacency.get(i).size(); e++) {
        Pair<Integer, Double> edge = this.adjacency.get(i).get(e);
        if (edge.getItem1() == this.vertices.size() - 1) {
          this.adjacency.get(i).remove(e);
          e--;
          edgeCount--;
        }
      }
    }
    this.adjacency.remove(this.vertices.size() - 1);
    this.vertices.remove(this.vertices.size() - 1);
  }

  public void addEdge(int u, int v, double weight) {
    this.adjacency.get(u).add(new Pair(v, weight));
    this.adjacency.get(v).add(new Pair(u, weight));
    edgeCount++;
  }

  public ArrayList<Pair<Integer, Double>> getAdjacency(int i) {
    return this.adjacency.get(i);
  }

  public ArrayList<V> getVertices() {
    return this.vertices;
  }

  public V getVertex(int i) {
    return this.vertices.get(i);
  }

  public int getVertexCount() {
    return this.vertices.size();
  }

  public int getEdgeCount() {
    return this.edgeCount;
  }
}
