package splib.util;

import splib.util.IndexKeeper;
import splib.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;


public abstract class Heap<E> {


  protected HashMap<E, Integer> indexMap;
  protected ArrayList<Pair<E, Integer>> elements;


  Heap() {
    this.elements = new ArrayList<Pair<E, Integer>>();
    this.indexMap = new HashMap<E, Integer>();
  }


  Heap(ArrayList<Pair<E, Integer>> elements) {
    this.elements = new ArrayList<Pair<E, Integer>>(elements);
    this.indexMap = new HashMap<E, Integer>();
    for (int i = 0; i < elements.size(); i++) {
      this.indexMap.put(this.elements.get(i).getItem1(), i);
    }
    for (int i = (int)Math.floor((double)(elements.size() - 1) / 2.0); i >= 0;
        i--) {
      this.heapify(i);
    }
  }


  public ArrayList<Pair<E, Integer>> getElements() {
    return this.elements;
  }


  public HashMap<E, Integer> getIndexMap() {
    return this.indexMap;
  }

  abstract void heapify(int i);
  abstract void changeKey(int i, int key);

  void changeKey(E elem, int key) {
    this.changeKey(this.indexMap.get(elem), key);
  }

}
