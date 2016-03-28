import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  TestGraph.class,
  TestDijkstra.class,
  TestPriorityQueue.class,
  TestGraphCreator.class
})

public class TestSuite {}
