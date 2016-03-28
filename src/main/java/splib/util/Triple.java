package splib.util;


/**
 * Represents an immutable generic three-tuple.
 */
public class Triple<E, F, G> {
  private final E item1;
  private final F item2;
  private final G item3;


  /**
   * Construct the pair.
   * @param item1 The first item.
   * @param item2 The second item.
   * @param item3 The third item.
   */
  public Triple(E item1, F item2, G item3)
  {
    this.item1 = item1;
    this.item2 = item2;
    this.item3 = item3;
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


  /**
   * Get the second item.
   * @return the second item.
   */
  public G getItem3() {
    return item3;
  }
}
