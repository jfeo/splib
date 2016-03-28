package splib.util;

import splib.util.Pair;
import java.util.ArrayList;
import java.lang.Math;
import java.lang.Comparable;


/**
 * A minimum heap, keyed with integers, holding elements of type E.
 */
public class MinHeap<E> {


  public ArrayList<Pair<E, Integer>> elements;


  /**
   * Initialize a minimum heap with an array of comparable elements.
   * @param elements The list of element and key two-tuples, from which to initialize the heap with.
   */
  public MinHeap(ArrayList<Pair<E, Integer>> elements) {
    this.elements = elements;
    for (int i = (int)Math.floor((double)(elements.size() - 1) / 2.0); i >= 0; i--) {
      this.heapify(i);
    }
  }


  /**
   * Initialize an empty minimum heap
   */
  public MinHeap() {
    this(new ArrayList<Pair<E, Integer>>());
  }


  /**
   * Print the array formatted.
   */
  public void printArray() {
    System.out.print("{");
    for (int i = 0; i < this.elements.size(); i++) {
      System.out.print(Integer.toString(i) + ": ");
      Pair<E, Integer> e = this.elements.get(i);
      System.out.print("(key: "+ Integer.toString(e.getItem2())+", elem: "+e.getItem1().toString()+")");
      if (i < this.elements.size() - 1) {
        System.out.print(", ");
      }
    }
    System.out.println("}");
  }


  protected void swap(int i, int j) {
    if (i == j) {
      return;
    }
    Pair<E, Integer> tmp = this.elements.get(i);
    this.elements.set(i, this.elements.get(j));
    this.elements.set(j, tmp);
  }


  /**
   * Maintain the heap property on the given subtree rooted at the ith element.
   * @param i The index of element whose subtree will have its heap property maintained.
   */
  public void heapify(int i) {
    while (true) {
      int l = this.left(i);
      int r = this.right(i);
      int least = 0;

      if (l < this.elements.size()
          && this.elements.get(l).getItem2() < this.elements.get(i).getItem2()) {
        least = l;
      } else {
        least = i;
      }

      if (r < this.elements.size()
          && this.elements.get(r).getItem2() < this.elements.get(least).getItem2()) {
        least = r;
      }

      if (i != least) {
        this.swap(i, least);
        i = least;
      } else {
        break;
      }
    }
  }


  /**
   * Get the index of the first occurence of the given element.
   * @param element The element to find the index of.
   * @return The index of the element, or -1 if no such element exists.
   */
  public int indexOf(E element) {
    for (int i = 0; i < this.elements.size(); i++) {
      if (element == this.elements.get(i)) {
        return i;
      }
    }
    return -1;
  }


  /**
   * Return the parent index of the ith element.
   * @param i The index of the element whose parent will be returned.
   * @return The parent index of the ith element.
   */
  protected int parent(int i) {
    return (int)Math.floor((double)i/2.0);
  }


  /**
   * Return the left child index of the ith element.
   * @param i The index of the element whose left child index will be returned.
   * @return The right child index of the ith element.
   */
  protected int left(int i) {
    return 2 * i + 1;
  }


  /**
   * Return the right child index of the ith element.
   * @param i The index of the element whose right child index will be returned.
   * @return The right child index of the ith element.
   */
  protected int right(int i) {
    return 2 * i + 2;
  }


}
