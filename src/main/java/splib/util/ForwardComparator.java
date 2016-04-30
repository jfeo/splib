package splib.util;

import java.util.Comparator;
import splib.data.SPVertex;


public class ForwardComparator<V extends SPVertex> implements Comparator<V> {


  public int compare(V v, V u) {
    if (v.getEstimate() < u.getEstimate()) {
      return -1;
    } else if (v.getEstimate() > u.getEstimate()) {
      return 1;
    } else {
      return 0;
    }
  }




}
