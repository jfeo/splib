package splib.util;


import splib.util.Pair;
import splib.util.Heap;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * A minimum priority queue.
 */
public class PriorityQueue<E> {


  protected Heap<E> heap;


  public PriorityQueue(Heap<E> h) {
    this.heap = h;
  }

  public PriorityQueue(Heap<E> h, ArrayList<E> A) {
    this.heap = h;
    for (E a : A) {
      this.insert(a);
    }
  }


  public Heap<E> getHeap() {
    return this.heap;
  }


  /**
   * Find the element at the top of the heap.
   * @return The element at the top of the queue.
   */
  public E top() {
    if (this.heap.getElements().size() > 0) {
      return this.heap.getElements().get(0);
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
    this.heap.getElements().add(element);
    this.heap.getIndexMap().put(element, this.heap.getElements().size() - 1);
    this.changeKey(element);
  }

  /**
   * Extract an element from the queue.
   * @return The element top element of the heap.
   */
  public E extract() {
    if (this.heap.getElements().size() > 0) {
      E top = this.top();
      this.heap.getIndexMap().remove(top);
      this.heap.getElements().set(0, this.heap.getElements().get(this.heap.getElements().size() - 1));
      this.heap.getIndexMap().put(this.heap.getElements().get(0), 0);
      this.heap.getElements().remove(this.heap.getElements().size() - 1);
      this.heap.heapify(0);
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
    return this.heap.getElements().isEmpty();
  }


  /**
   * Get the ith element of the priority queue.
   * @param i The index from which to get the element.
   * @return The element at the ith index of the queue.
   */
  public E get(int i) {
    return this.heap.getElements().get(i);
  }


  /**
   * Get the index of the first occurence of the given element.
   * @param element The element to find the index of.
   * @return The index of the element, or -1 if no such element exists.
   */
  public Integer indexOf(E element) {
    HashMap<E, Integer> imap = this.heap.getIndexMap();
    Integer res = imap.get(element);
    return res;
  }


  public void changeKey(E elem) {
    this.heap.changeKey(this.indexOf(elem));
  }


}
