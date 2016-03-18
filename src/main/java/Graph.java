package tobfeo.splib;

import java.util.ArrayList;
import java.lang.IllegalArgumentException;
import java.lang.Integer;

public class Graph {
  /**
   * A simple graph, implemented as an adjacency list.
   */

  public ArrayList<ArrayList<Pair<Integer, Integer>>> adjacency;

  public Graph() {
    this.adjacency = new ArrayList<ArrayList<Pair<Integer, Integer>>>();
  }

  public Integer addVertex()
  {
    this.adjacency.add(new ArrayList<>());
    return this.adjacency.size() - 1;
  }

  public void addEdge(Integer v, Integer u, Integer w)
  {
    this.adjacency.get(v).add(new Pair<>(u, w));
    this.adjacency.get(u).add(new Pair<>(v, w));
  }

  public void remove(Integer v)
  {
    this.adjacency.remove(v);
  }

  public Integer getWeight(Integer v, Integer u)
  {
    for (Pair<Integer, Integer> edge : this.adjacency.get(v))
    {
      if (edge.x == u)
      {
        return edge.y;
      }
    }
    return null;
  }

}
