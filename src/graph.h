#ifndef SPLIB_GRAPH_H_
#define SPLIB_GRAPH_H_

#include <vector>
#include <tuple>


namespace shortestpaths {


class Graph {

private:
  std::vector<std::vector<std::tuple<int, int>>> adjacency;

public:
  Graph();
  ~Graph();

  int add_vertex();
  void add_edge(int v, int u, int w);
  int get_weight(int v, int u);

}; // class Graph


} // namespace shortestpaths


#endif // GRAPH
