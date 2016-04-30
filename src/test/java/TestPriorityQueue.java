import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import splib.util.Pair;
import splib.util.PriorityQueue;
import splib.data.Graph;
import splib.data.SPVertex;
import splib.data.Vertex;
import splib.util.MinBinaryHeap;
import java.util.Comparator;


public class TestPriorityQueue {

  private class StringComparator implements Comparator<String> {
    public int compare(String s1, String s2) {
      return s1.compareTo(s2);
    }
  }

  @Test
  public void test_emptyConstructor() {
    StringComparator sCompare = new StringComparator();

    MinBinaryHeap<String> h = new MinBinaryHeap<String>(sCompare);
    PriorityQueue<String> Q = new PriorityQueue<String>(h);
    Q.insert("B Second string");
    Q.insert("E Fifth string");
    Q.insert("C Third string");
    Q.insert("D Fourth string");
    Q.insert("A First string");

    String s = Q.extract();
    assertEquals("A First string", s);
    s = Q.extract();
    assertEquals("B Second string", s);
    s = Q.extract();
    assertEquals("C Third string", s);
    s = Q.extract();
    assertEquals("D Fourth string", s);
    s = Q.extract();
    assertEquals("E Fifth string", s);
  }


  @Test
  public void test_arrayConstructor() {
    StringComparator sCompare = new StringComparator();
    ArrayList<String> A = new ArrayList<String>();
    A.add("B Second string");
    A.add("E Fifth string");
    A.add("C Third string");
    A.add("D Fourth string");
    A.add("A First string");
    PriorityQueue<String> Q = new PriorityQueue<String>(new MinBinaryHeap(sCompare, A));

    String s = Q.extract();
    assertEquals("A First string", s);
    s = Q.extract();
    assertEquals("B Second string", s);
    s = Q.extract();
    assertEquals("C Third string", s);
    s = Q.extract();
    assertEquals("D Fourth string", s);
    s = Q.extract();
    assertEquals("E Fifth string", s);
  }


}
