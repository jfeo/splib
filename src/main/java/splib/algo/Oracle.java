package splib.algo;


import splib.data.Graph;
import splib.data.SPVertex;


public interface Oracle <V extends SPVertex> {

  Double query(int v, int u);
  Object preprocess(Graph<V> G, int heapArity);
  int getK();

}
