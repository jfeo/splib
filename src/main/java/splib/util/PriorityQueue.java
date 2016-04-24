package splib.util;


import splib.util.IndexKeeper;
import splib.util.Pair;
import splib.util.Heap;
import java.util.ArrayList;


/**
 * A minimum priority queue.
 */
public class PriorityQueue<E> {


  protected Heap<E> heap;


  public PriorityQueue(Heap<E> h) {
    this.heap = h;
  }

  public PriorityQueue(Heap<E> h, ArrayList<Pair<E, Integer>> A) {
    this.heap = h;
    for (Pair<E, Integer> a : A) {
      this.insert(a.getItem1(), a.getItem2());
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
      return this.heap.getElements().get(0).getItem1();
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
    this.heap.getElements().add(new Pair<E, Integer>(element, key));
    this.heap.getIndexMap().put(element, this.heap.getElements().size() - 1);
    this.heap.changeKey(element, key);
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
      this.heap.getIndexMap().put(this.heap.getElements().get(0).getItem1(), 0);
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
    return this.heap.getElements().get(i).getItem1();
  }


  /**
   * Get the index of the first occurence of the given element.
   * @param element The element to find the index of.
   * @return The index of the element, or -1 if no such element exists.
   */
  public int indexOf(E element) {
    return this.heap.getIndexMap().get(element);
  }


  public void changeKey(E elem, int key) {
    this.heap.changeKey(this.indexOf(elem), key);
  }


}
