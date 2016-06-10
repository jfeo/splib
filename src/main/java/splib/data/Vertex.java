package splib.data;

import java.lang.RuntimeException;
import java.util.ArrayList;
import java.util.Collection;
import splib.util.Pair;
import java.lang.Comparable;


/**
 * A representation of a vertex in a graph, specifically used in shortest path
 * algorithms.
 */
public class Vertex {


  // // protected Integer index;
  // protected ArrayList<Pair<Vertex, Double>> adjacency;


  /**
   * Initialize the vertex.
   */
  public Vertex() {
    // this.adjacency = new ArrayList<Pair<Vertex, Double>>();
  }


  /**
   * Add an adjacency relation (or edge) from this vertex, to vertex v, with
   * the given weight.
   * @param vIndex The index of the vertex that is adjacent to this one.
   * @param weight The weight of the edge between the,.
   */
  // public void addAdjacency(Vertex v, Double weight) {
  //   this.adjacency.add(new Pair<Vertex, Double>(v, weight));
  // }


  /**
   * Remove the adjacency relation (or edge) from this vertex, to vertex v.
   * @param v The vertex whose relation to this vertex is to be removed.
   */
  // public void removeAdjacency(Vertex v) {
  //   for (int i = 0; i < this.adjacency.size(); i++) {
  //     Pair<Vertex, Double> u = this.adjacency.get(i);
  //     if (u.getItem1() == v) {
  //       this.adjacency.remove(i);
  //       i--;
  //     }
  //   }
  // }


  /**
   * Get the list of vertices adjacent to this one, along with the weight of
   * their respective edges.
   * @return A list of two-tuples, containing the target vertex and weight of
   * all edges from this vertex.
   */
  // public ArrayList<Pair<Vertex, Double>> getAdjacency() {
  //   return this.adjacency;
  // }


  /**
   * Get of the edge from this vertex to the given vertex v.
   * @param v The vertex to which the edge should go.
   * @return The weight of the edge between this vertex and the vertex v.
   */
  // public Double getWeight(SPVertex v) {
  //   for (Pair<Vertex, Double> u : this.adjacency) {
  //     if (v == u.getItem1()) {
  //       return u.getItem2();
  //     }
  //   }
  //   return null;
  // }


  /**
   * Find out whether the given vertex is adjacent to this one.
   * @param v The vertex that might be adjacent to this one.
   * @return True if v is adjancent to this vertex.
   */
  // public boolean isAdjacent(Vertex v) {
  //   for (Pair<Vertex, Double> u : this.adjacency) {
  //     if (v == u.getItem1()) {
  //       return true;
  //     }
  //   }
  //   return false;
  // }


}
