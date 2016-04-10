package splib.util;

import splib.util.Heap;
import splib.util.Pair;
import java.util.ArrayList;
import java.lang.Math;


/**
 * A minimum three-heap, keyed with integers, holding elements of type E.
 */
public class MinThreeHeap<E extends IndexKeeper> extends Heap<E> {

  /**
   * Maintain the heap property on the given subtree rooted at the ith element.
   * @param i The index of element whose subtree will have its heap property
   * maintained.
   */
  @Override
  public void heapify(int i) {
    while (true) {
      int l = this.left(i);
      int m = this.middle(i);
      int r = this.right(i);
      int least = 0;

      if (l < this.elements.size()
          && this.elements.get(l).getItem2()
          <  this.elements.get(i).getItem2()) {
        least = l;
      } else {
        least = i;
      }

      if (m < this.elements.size()
          && this.elements.get(m).getItem2()
          <  this.elements.get(least).getItem2()) {
        least = m;
      }

      if (r < this.elements.size()
          && this.elements.get(r).getItem2()
          <  this.elements.get(least).getItem2()) {
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

  protected void swap(int i, int j) {
    if (i == j) {
      return;
    }
    Pair<E, Integer> tmp = this.elements.get(i);
    this.elements.set(i, this.elements.get(j));
    this.elements.set(j, tmp);

    this.elements.get(i).getItem1().setIndex(i);
    this.elements.get(j).getItem1().setIndex(j);
  }


  /**
   * Decrease the key to the value of key of the ith element, and maintain the
   * heap property, by letting the element float up the tree.
   * @param i The index of the element
   * @param key The new key value
   */
  @Override
  public void changeKey(int i, int key) {
    if (key > this.elements.get(i).getItem2()) {
      return;
    }

    this.elements.set(i, new Pair<E, Integer>(this.elements.get(i).getItem1(), key));

    while (i != 0 && key < this.parent(i)) {
      this.swap(i, this.parent(i));
      i = this.parent(i);
    }
  }

  /**
   * Return the parent index of the ith element.
   * @param i The index of the element whose parent will be returned.
   * @return The parent index of the ith element.
   */
	public int parent(int i) {
		return (int)Math.floor(i/3);
	}


  /**
   * Return the left child index of the ith element.
   * @param i The index of the element whose left child index will be returned.
   * @return The right child index of the ith element.
   */
	protected int left(int i) {
		return 3 * i + 1;
	}


  /**
   * Return the middle child index of the ith element.
   * @param i The index of the element whose middle child index will be returned.
   * @return The middle child index of the ith element.
   */
	protected int middle(int i){
		return 3 * i + 2;
	}


  /**
   * Return the right child index of the ith element.
   * @param i The index of the element whose right child index will be returned.
   * @return The right child index of the ith element.
   */
	protected int right(int i) {
		return 3 * i + 3;
	}


}