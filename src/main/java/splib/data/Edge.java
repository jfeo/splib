package splib.data;


/**
 * Implementation of an edge in a graph
 */
public class Edge {
  public Vertex u;
  public Vertex v;

  public int weight;

  public Edge(int weight, Vertex v, Vertex u) {
    this.v = v;
    this.u = u;
    this.weight = weight;
  }
}
