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

    Heap<String> Q = new Heap<String>((s1, s2) -> s1.compareTo(s2), 4);
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
    ArrayList<String> A = new ArrayList<String>();
    A.add("B Second string");
    A.add("E Fifth string");
    A.add("C Third string");
    A.add("D Fourth string");
    A.add("A First string");
    Heap<String> Q = new Heap<String>((s1, s2) -> s1.compareTo(s2), A, 4);

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
