package splib.data;

import java.lang.RuntimeException;
import java.util.ArrayList;
import splib.util.Pair;
import java.lang.Comparable;


/**
 * A representation of a vertex in a graph, specifically used in shortest path
 * algorithms.
 */
public class Vertex {


  protected ArrayList<Pair<Vertex, Integer>> adjacency;


  /**
   * Initialize the vertex.
   */
  public Vertex() {
    this.adjacency = new ArrayList<Pair<Vertex, Integer>>();
  }


  /**
   * Add an adjacency relation (or edge) from this vertex, to vertex v, with
   * the given weight.
   * @param v The vertex that is adjacent to this one.
   * @param weight The weight of the edge between the,.
   */
  public void addAdjacency(Vertex v, int weight) {
    this.adjacency.add(new Pair<Vertex, Integer>(v, weight));
  }


  /**
   * Get the list of vertices adjacent to this one, along with the weight of
   * their respective edges.
   * @return A list of two-tuples, containing the target vertex and weight of
   * all edges from this vertex.
   */
  public ArrayList<Pair<Vertex, Integer>> getAdjacency() {
    return this.adjacency;
  }


  /**
   * Get of the edge from this vertex to the given vertex v.
   * @param v The vertex to which the edge should go.
   * @return The weight of the edge between this vertex and the vertex v.
   */
  public int getWeight(Vertex v) {
    for (Pair<Vertex, Integer> u : this.adjacency) {
      if (v == u.getItem1()) {
        return u.getItem2();
      }
    }
    return -1;
  }


  /**
   * Find out whether the given vertex is adjacent to this one.
   * @param v The vertex that might be adjacent to this one.
   * @return True if v is adjancent to this vertex.
   */
  public boolean isAdjacent(Vertex v) {
    for (Pair<Vertex, Integer> u : this.adjacency) {
      if (v == u.getItem1()) {
        return true;
      }
    }
    return false;
  }


}
