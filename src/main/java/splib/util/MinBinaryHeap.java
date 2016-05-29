package splib.util;

import splib.util.Heap;
import splib.util.Pair;
import java.util.ArrayList;
import java.util.Comparator;
import java.lang.Math;


/**
 * A minimum binary heap, keyed with integers, holding elements of type E.
 */
// public class MinBinaryHeap<E> extends Heap<E> {


//   /**
//    * Initialize a minimum heap with an array of comparable elements.
//    * @param elements The list of element and key two-tuples, from which to
//    * initialize the heap with.
//    */
//   public MinBinaryHeap(Comparator<E> c, ArrayList<E> elements) {
//     super(c, elements);
//   }


//   /**
//    * Initialize an empty minimum heap
//    */
//   public MinBinaryHeap(Comparator<E> c) {
//     super(c);
//   }


//   protected void swap(int i, int j) {
//     if (i == j) {
//       return;
//     }
//     E tmp = this.elements.get(i);
//     this.elements.set(i, this.elements.get(j));
//     this.elements.set(j, tmp);

//     this.indexMap.put(this.elements.get(i), i);
//     this.indexMap.put(this.elements.get(j), j);
//   }


//   /**
//    * Maintain the heap property on the given subtree rooted at the ith element.
//    * @param i The index of element whose subtree will have its heap property
//    * maintained.
//    */
//   @Override
//   public void heapify(int i) {
//     while (true) {
//       int l = this.left(i);
//       int r = this.right(i);
//       int least = 0;

//       if (l < this.elements.size()
//           &&  this.comparator.compare(this.elements.get(l),
//           this.elements.get(i)) < 0) {
//         least = l;
//       } else {
//         least = i;
//       }

//       if (r < this.elements.size()
//           &&  this.comparator.compare(this.elements.get(r),
//           this.elements.get(least)) < 0) {
//         least = r;
//       }

//       if (i != least) {
//         this.swap(i, least);
//         i = least;
//       } else {
//         break;
//       }
//     }
//   }


//   /**
//    * Decrease the key to the value of key of the ith element, and maintain the
//    * heap property, by letting the element float up the tree.
//    * @param i The index of the element
//    * @param key The new key value
//    */
//   @Override
//   public void changeKey(int i) {
//     while (i != 0 &&  this.comparator.compare(this.elements.get(i),
//         this.elements.get(this.parent(i))) < 0) {
//       this.swap(i, this.parent(i));
//       i = this.parent(i);
//     }
//   }


//   /**
//    * Get the index of the first occurence of the given element.
//    * @param element The element to find the index of.
//    * @return The index of the element, or -1 if no such element exists.
//    */
//   public int indexOf(E element) {
//     return this.indexMap.get(element);
//   }


//   /**
//    * Return the parent index of the ith element.
//    * @param i The index of the element whose parent will be returned.
//    * @return The parent index of the ith element.
//    */
//   protected int parent(int i) {
//     return (int)Math.floor((double)i/2.0);
//   }


//   /**
//    * Return the left child index of the ith element.
//    * @param i The index of the element whose left child index will be returned.
//    * @return The right child index of the ith element.
//    */
//   protected int left(int i) {
//     return 2 * i + 1;
//   }


//   /**
//    * Return the right child index of the ith element.
//    * @param i The index of the element whose right child index will be returned.
//    * @return The right child index of the ith element.
//    */
//   protected int right(int i) {
//     return 2 * i + 2;
//   }


// }
