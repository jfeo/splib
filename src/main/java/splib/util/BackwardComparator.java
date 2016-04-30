package splib.util;

import java.util.Comparator;
import splib.data.BDDVertex;
import splib.data.SPVertex;
import java.lang.RuntimeException;


public class BackwardComparator<V extends BDDVertex> implements Comparator<V> {


  public int compare(V v, V u) {
    if (v.getSuccessorEstimate() < u.getSuccessorEstimate()) {
      return -1;
    } else if (v.getSuccessorEstimate() > u.getSuccessorEstimate()) {
      return 1;
    } else {
      return 0;
    }
  }


}
