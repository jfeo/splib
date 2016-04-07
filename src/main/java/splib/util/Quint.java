package splib.util;


/**
 * Represents an immutable generic five-tuple.
 */
public class Quint<E, F, G, H, I> {
  private final E item1;
  private final F item2;
  private final G item3;
  private final H item4;
  private final I item5;


  /**
   * Construct the pair.
   * @param item1 The first item.
   * @param item2 The second item.
   * @param item3 The third item.
   * @param item4 The fourth item.
   * @param item5 The fifth item.
   */
  public Quint(E item1, F item2, G item3, H item4, I item5)  {
    this.item1 = item1;
    this.item2 = item2;
    this.item3 = item3;
    this.item4 = item4;
    this.item5 = item5;
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
   * Get the third item.
   * @return the third item.
   */
  public G getItem3() {
    return item3;
  }


  /**
   * Get the fourth item.
   * @return the fourth item.
   */
  public H getItem4() {
    return item4;
  }


  /**
   * Get the fifth item.
   * @return the fifth item.
   */
  public I getItem5() {
    return item5;
  }


}
