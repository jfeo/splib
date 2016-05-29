package splib.util;

import splib.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;
import java.lang.Math;
import java.lang.RuntimeException;


public class Heap<E> {


  protected HashMap<E, Integer> indexMap;
  protected ArrayList<E> elements;
  protected Comparator<E> comparator;
  protected int arity;


  public Heap(Comparator<E> c, int arity) {
    if (arity < 2) {
      throw new RuntimeException("Arity must be 2 or above.");
    }

    this.arity = arity;
    this.comparator = c;
    this.elements = new ArrayList<E>();
    this.indexMap = new HashMap<E, Integer>();
  }


  public Heap(Comparator<E> c, ArrayList<E> elements, int arity) {
    if (arity < 2) {
      throw new RuntimeException("Arity must be 2 or above.");
    }

    this.arity = arity;
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


  protected void swap(int i, int j) {
    if (i == j) {
      return;
    }
    E tmp = this.elements.get(i);
    this.elements.set(i, this.elements.get(j));
    this.elements.set(j, tmp);

    this.indexMap.put(this.elements.get(i), i);
    this.indexMap.put(this.elements.get(j), j);
  }


  /**
   * Find the element at the top of the heap.
   * @return The element at the top of the queue.
   */
  public E top() {
    if (this.elements.size() > 0) {
      return this.elements.get(0);
    } else {
      return null;
    }
  }


  /**
   * Insert an element with the specified key.
   * @param element The element to be inserted.
   * @param key The key value for the element.
   */
  public void insert(E element) {
    this.elements.add(element);
    this.indexMap.put(element, this.elements.size() - 1);
    this.changeKey(element);
  }

  /**
   * Extract an element from the queue.
   * @return The element top element of the heap.
   */
  public E extract() {
    if (this.elements.size() > 0) {
      E top = this.top();
      this.indexMap.remove(top);
      this.elements.set(0, this.elements.get(this.elements.size() - 1));
      this.indexMap.put(this.elements.get(0), 0);
      this.elements.remove(this.elements.size() - 1);
      this.heapify(0);
      return top;
    } else {
      return null;
    }
  }


  /**
   * Check whether the priority queue is empty.
   * @return True if the queue is empty, false otherwise.
   */
  public boolean isEmpty() {
    return this.elements.isEmpty();
  }


  /**
   * Get the ith element of the priority queue.
   * @param i The index from which to get the element.
   * @return The element at the ith index of the queue.
   */
  public E get(int i) {
    return this.elements.get(i);
  }


  /**
   * Get the index of the first occurence of the given element.
   * @param element The element to find the index of.
   * @return The index of the element, or -1 if no such element exists.
   */
  public Integer indexOf(E element) {
    return this.indexMap.get(element);
  }


  public ArrayList<E> getElements() {
    return this.elements;
  }


  public HashMap<E, Integer> getIndexMap() {
    return this.indexMap;
  }


  /**
   * Maintain the heap property on the given subtree rooted at the ith element.
   * @param i The index of element whose subtree will have its heap property
   * maintained.
   */
  public void heapify(int i) {
    while (true) {
      int top = i;
      int[] children = new int[this.arity];
      for (int j = 0; j < this.arity; j++) {
        children[j] = this.child(i, j+1);
      }

      for (int j = 0; j < arity; j++) {
        int c = children[j];
        if (c < this.elements.size()
            &&  this.comparator.compare(this.elements.get(c),
            this.elements.get(top)) < 0) {
          top = c;
        }
      }

      if (i != top) {
        this.swap(i, top);
        i = top;
      } else {
        break; //
      }
    }
  }


  /**
   * Returns the index of the j'th child, of the i'th element..
   * @param i The index for the element whose child index should be returned.
   * @param j The child number.
   */
  private int child(int i, int j) {
    return this.arity * i + j;
  }

  /**
   * Return the parent index of the ith element.
   * @param i The index of the element whose parent will be returned.
   * @return The parent index of the ith element.
   */
  public int parent(int i) {
    return (int)Math.floor(i/this.arity);
  }


  /**
   * Change the key to the value of key of the ith element, and maintain the
   * heap property, by letting the element float up the tree.
   * @param i The index of the element
   * @param key The new key value
   */
  public void changeKey(int i) {
    while (i != 0 && this.comparator.compare(this.elements.get(i),
        this.elements.get(this.parent(i))) < 0) {
      this.swap(i, this.parent(i));
      i = this.parent(i);
    }
  }

  public void changeKey(E elem) {
    this.changeKey(this.indexMap.get(elem));
  }

}
