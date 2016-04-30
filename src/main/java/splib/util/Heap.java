package splib.util;

import splib.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;
import java.lang.Math;


public abstract class Heap<E> {


  protected HashMap<E, Integer> indexMap;
  protected ArrayList<E> elements;
  protected Comparator<E> comparator;


  Heap(Comparator<E> c) {
    this.comparator = c;
    this.elements = new ArrayList<E>();
    this.indexMap = new HashMap<E, Integer>();
  }


  Heap(Comparator<E> c, ArrayList<E> elements) {
    this.comparator = c;
    this.elements = new ArrayList<E>(elements);
    this.indexMap = new HashMap<E, Integer>();
    for (int i = 0; i < elements.size(); i++) {
      this.indexMap.put(this.elements.get(i), i);
    }
    for (int i = (int)Math.floor((double)(elements.size() - 1) / 2.0); i >= 0;
        i--) {
      this.heapify(i);
    }
  }


  public ArrayList<E> getElements() {
    return this.elements;
  }


  public HashMap<E, Integer> getIndexMap() {
    return this.indexMap;
  }

  abstract void heapify(int i);
  abstract void changeKey(int i);

  void changeKey(E elem) {
    this.changeKey(this.indexMap.get(elem));
  }

  public int compare(E e, E f) {
    return this.comparator.compare(e, f);
  }

}
