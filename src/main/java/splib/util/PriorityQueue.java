package splib.util;


import splib.util.Pair;
import splib.util.MinHeap;
import java.util.ArrayList;


/**
 * A minimum priority queue.
 */
public class PriorityQueue<E> extends MinHeap<E> {


  public PriorityQueue(ArrayList<Pair<E, Integer>> A) {
    super(A);
  }

  public PriorityQueue() {
    super();
  }


  /**
   * Find the element with the minimum key.
   * @return The element with the minimum key.
   */
  public E minimum() {
    if (this.elements.size() > 0) {
      return this.elements.get(0).getItem1();
    } else {
      return null;
    }
  }


  /**
   * Insert an element with the specified key.
   * @param element The element to be inserted.
   * @param key The key value for the element.
   */
  public void insert(E element, int key) {
    this.elements.add(new Pair<E, Integer>(element, Integer.MAX_VALUE));
    this.decreaseKey(this.elements.size() - 1, key);
  }


  /**
   * Decrease the key to the value of key of the ith element.
   * @param i The index of the element
   * @param key The new key value
   */
  public void decreaseKey(int i, int key) {
    if (key > this.elements.get(i).getItem2()) {
      return; // new key value is not a decrease TODO: may throw exception?
    }

    this.elements.set(i, new Pair<E, Integer>(this.elements.get(i).getItem1(), key));

    while (i > 0 && this.elements.get(this.parent(i)).getItem2() > this.elements.get(i).getItem2()) {
      // Swap i and parent of i
      this.swap(i, this.parent(i));
      i = this.parent(i);
    }
  }


  /**
   * Extract the minimum element from the queue.
   * @return The element with the minimum key.
   */
  public E extractMinimum() {
    if (this.elements.size() > 0) {
      E minimum = this.minimum();
      this.elements.set(0, this.elements.get(this.elements.size() - 1));
      this.elements.remove(this.elements.size() - 1);
      this.heapify(0);
      return minimum;
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
    return this.elements.get(i).getItem1();
  }


  /**
   * Get the index of the first occurence of the given element.
   * @param element The element to find the index of.
   * @return The index of the element, or -1 if no such element exists.
   */
  public int indexOf(E element) {
    for (int i = 0; i < this.elements.size(); i++) {
      if (element == this.elements.get(i).getItem1()) {
        return i;
      }
    }
    return -1;
  }


}
