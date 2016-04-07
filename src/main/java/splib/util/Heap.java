package splib.util;

import splib.util.IndexKeeper;
import splib.util.Pair;
import java.util.ArrayList;
import java.lang.Math;


public abstract class Heap<E extends IndexKeeper> {


  protected ArrayList<Pair<E, Integer>> elements;


  Heap() {
    this.elements = new ArrayList<Pair<E, Integer>>();
  }


  Heap(ArrayList<Pair<E, Integer>> elements) {
    this.elements = new ArrayList<Pair<E, Integer>>(elements);
    for (int i = (int)Math.floor((double)(elements.size() - 1) / 2.0); i >= 0;
        i--) {
      this.heapify(i);
    }
  }


  public ArrayList<Pair<E, Integer>> getElements() {
    return this.elements;
  }


  abstract void heapify(int i);
  abstract void changeKey(int i, int key);


}
