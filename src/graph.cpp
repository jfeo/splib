#include "graph.h"


namespace shortestpaths {


/*
 * Construct a new empty graph.
 */
Graph::Graph() {
  this->adjacency = std::vector<std::vector<std::tuple<int, int>>>();;
}


/*
 * Deconstruct a graph.
 */
Graph::~Graph() {

}


/*
 * Add a vertex, returning its index.
 */
int Graph::add_vertex() {
  this->adjacency.push_back(std::vector<std::tuple<int, int>>());
  return this->adjacency.size() - 1;
}


/*
 * Add an edge with weight w between vertices v and u.
 */
void Graph::add_edge(int v, int u, int w) {
  this->adjacency[v].push_back(std::make_tuple(u, w));
  this->adjacency[u].push_back(std::make_tuple(v, w));
}


/*
 * Returns the weight of the edge between vertices v and u.
 */
int Graph::get_weight(int v, int u) {
  for (auto edge : this->adjacency[v]) {
    int i; int w;
    std::tie(i, w) = edge;

    if (i == u) {
      return w;
    }
  }
}


}; // namespace shortestpaths
