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

  public class StringWrapper implements IndexKeeper {
    private String s;

    public StringWrapper(String s) {
      this.s = s;
    }

    public String get() {
      return this.s;
    }

    public void setIndex(Integer i) {}
    public Integer getIndex() { return 0; }
  }


  @Test
  public void test_emptyConstructor() {
    MinBinaryHeap<StringWrapper> h = new MinBinaryHeap<>();
    PriorityQueue<StringWrapper> Q = new PriorityQueue<StringWrapper>(h);
    Q.insert(new StringWrapper("Second string"), 10);
    Q.insert(new StringWrapper("Fifth string"), 9999);
    Q.insert(new StringWrapper("Third string"), 100);
    Q.insert(new StringWrapper("Fourth string"), 500);
    Q.insert(new StringWrapper("First string"), 1);

    StringWrapper s = Q.extract();
    assertEquals("First string", s.get());
    s = Q.extract();
    assertEquals("Second string", s.get());
    s = Q.extract();
    assertEquals("Third string", s.get());
    s = Q.extract();
    assertEquals("Fourth string", s.get());
    s = Q.extract();
    assertEquals("Fifth string", s.get());
  }


  @Test
  public void test_arrayConstructor() {
    ArrayList<Pair<StringWrapper, Integer>> A = new ArrayList<Pair<StringWrapper, Integer>>();
    A.add(new Pair<StringWrapper, Integer>(new StringWrapper("Second string"), 10));
    A.add(new Pair<StringWrapper, Integer>(new StringWrapper("Fifth string"), 9999));
    A.add(new Pair<StringWrapper, Integer>(new StringWrapper("Third string"), 100));
    A.add(new Pair<StringWrapper, Integer>(new StringWrapper("Fourth string"), 500));
    A.add(new Pair<StringWrapper, Integer>(new StringWrapper("First string"), 1));
    PriorityQueue<StringWrapper> Q = new PriorityQueue<StringWrapper>(new MinBinaryHeap(A));

    StringWrapper s = Q.extract();
    assertEquals("First string", s.get());
    s = Q.extract();
    assertEquals("Second string", s.get());
    s = Q.extract();
    assertEquals("Third string", s.get());
    s = Q.extract();
    assertEquals("Fourth string", s.get());
    s = Q.extract();
    assertEquals("Fifth string", s.get());
  }


}
