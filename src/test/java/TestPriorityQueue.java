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
import splib.util.IndexKeeper;
import splib.util.MinBinaryHeap;


public class TestPriorityQueue {

  @Test
  public void test_emptyConstructor() {
    MinBinaryHeap<String> h = new MinBinaryHeap<>();
    PriorityQueue<String> Q = new PriorityQueue<String>(h);
    Q.insert("Second string", 10);
    Q.insert("Fifth string", 9999);
    Q.insert("Third string", 100);
    Q.insert("Fourth string", 500);
    Q.insert("First string", 1);

    String s = Q.extract();
    assertEquals("First string", s);
    s = Q.extract();
    assertEquals("Second string", s);
    s = Q.extract();
    assertEquals("Third string", s);
    s = Q.extract();
    assertEquals("Fourth string", s);
    s = Q.extract();
    assertEquals("Fifth string", s);
  }


  @Test
  public void test_arrayConstructor() {
    ArrayList<Pair<String, Integer>> A = new ArrayList<Pair<String, Integer>>();
    A.add(new Pair<String, Integer>("Second string", 10));
    A.add(new Pair<String, Integer>("Fifth string", 9999));
    A.add(new Pair<String, Integer>("Third string", 100));
    A.add(new Pair<String, Integer>("Fourth string", 500));
    A.add(new Pair<String, Integer>("First string", 1));
    PriorityQueue<String> Q = new PriorityQueue<String>(new MinBinaryHeap(A));

    String s = Q.extract();
    assertEquals("First string", s);
    s = Q.extract();
    assertEquals("Second string", s);
    s = Q.extract();
    assertEquals("Third string", s);
    s = Q.extract();
    assertEquals("Fourth string", s);
    s = Q.extract();
    assertEquals("Fifth string", s);
  }


}
