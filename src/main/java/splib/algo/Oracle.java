package splib.algo;


import splib.data.SPVertex;


public interface Oracle <V extends SPVertex> {

  Double query(V v, V u);
  int getHeapArity();
  int getK();

}
