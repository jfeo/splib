package splib.util;


/**
 * Represents an immutable generic two-tuple.
 */
public class Pair<E, F> {
  private final E item1;
  private final F item2;


  /**
   * Construct the pair.
   * @param item1 The first item.
   * @param item2 The second item.
   */
  public Pair(E item1, F item2) {
    this.item1 = item1;
    this.item2 = item2;
  }

  /**
   * Get the first item.
   * @return the first item.
   */
  public E getItem1() {
    return item1;
  }

  /**
   * Get the second item.
   * @return the second item.
   */
  public F getItem2() {
    return item2;
  }
}
