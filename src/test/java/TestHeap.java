import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import splib.util.Pair;
import splib.util.Heap;
import splib.data.Graph;
import splib.data.SPVertex;
import splib.data.Vertex;


public class TestHeap {


  @Test
  public void test_emptyConstructor() {
    Heap<String> h = new Heap<String>((s1, s2) ->
        s1.compareTo(s2), 2);
    h.insert("B Second string");
    h.insert("E Fifth string");
    h.insert("C Third string");
    h.insert("D Fourth string");
    h.insert("A First string");

    String s = h.extract();
    assertEquals("A First string", s);
    s = h.extract();
    assertEquals("B Second string", s);
    s = h.extract();
    assertEquals("C Third string", s);
    s = h.extract();
    assertEquals("D Fourth string", s);
    s = h.extract();
    assertEquals("E Fifth string", s);
  }


  @Test
  public void test_arrayConstructor() {
    ArrayList<String> A = new ArrayList<String>();
    A.add("B Second string");
    A.add("E Fifth string");
    A.add("C Third string");
    A.add("D Fourth string");
    A.add("A First string");
    Heap<String> h = new Heap<String>((s1, s2) ->
        s1.compareTo(s2), A, 2);

    String s = h.extract();
    assertEquals("A First string", s);
    s = h.extract();
    assertEquals("B Second string", s);
    s = h.extract();
    assertEquals("C Third string", s);
    s = h.extract();
    assertEquals("D Fourth string", s);
    s = h.extract();
    assertEquals("E Fifth string", s);
  }


}
