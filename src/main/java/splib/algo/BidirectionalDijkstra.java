package splib.algo;


import java.util.ArrayList;
import splib.data.*;
import splib.util.Heap;
import splib.util.PriorityQueue;
import splib.util.Pair;


public class BidirectionalDijkstra {


//  private static <E extends Heap<Pair<SPVertex, SPVertex>>> double singlePair(Graph<SPVertex> G, SPVertex s, SPVertex t) {
//    BidirectionalDijkstra.initializeSinglePair(G, s, t);
//    PriorityQueue Qs = new PriorityQueue(new H());
//    PriorityQueue Qt = new PriorityQueue(new H());
//    ArrayList<SPVertex> Ss = new ArrayList<SPVertex>();
//    ArrayList<SPVertex> St = new ArrayList<SPVertex>();
//    double minPath = Double.MAX_VALUE;

//    while (Qs.top() != Qt.top()) {
//      SPVertex u = Qs.extract();
//      Ss.add(u);
//      for (Pair<Vertex, Integer> v : u.getAdjacency()) {
//        Dijkstra.relax(Q, u, (SPVertex)v.getItem1(), v.getItem2());
//      }
//      if (u == t){
//        //return Qs.getMinNode().getTentativeDistances().get(0);
//      }

//      u = Qt.extract();
//      St.add(u);
//      for (Pair<Vertex, Integer> v : u.getAdjacency()) {
//        Dijkstra.relax(Q, u, (SPVertex)v.getItem1(), v.getItem2());
//      }
//      if (u == s){
//        //return Qt.top().getTentativeDistances().get(1);
//      }
//    }
//    // Check if the shortest path found, is in the best shortest path
//    double bestPath = checkShortestPath(S, Qs, Qt);
//    return Qs.getMinNode().getTentativeDistances().get(0) + Qt.getMinNode().getTentativeDistances().get(1);
//  }


//  private static void initializeSinglePair(Graph<SPVertex> G, SPVertex s, SPVertex t){
//    for (SPVertex v : G.getVertices()){
//      graph.nodes.get(i).getTentativeDistances().set(0, Double.MAX_VALUE);
//      graph.nodes.get(i).getTentativeDistances().set(1, Double.MAX_VALUE);
//      graph.nodes.get(i).setPredecessor(null);
//    }
//    graph.nodes.get(s).getTentativeDistances().set(0,0.0);
//    graph.nodes.get(s).setPredecessor(null);
//    graph.nodes.get(t).getTentativeDistances().set(1,0.0);
//    graph.nodes.get(t).setPredecessor(null);
//  }


//  public static void relax(PriorityQueue Q, SPVertex u, SPVertex v, int weight) {
//    int newEstimate = u.getEstimate() + weight;
//    if (v.getEstimate() > newEstimate) {
//      v.setPredecessor(u);
//      v.setEstimate(newEstimate);
//      if (v.getIndex() != null) {
//        Q.changeKey(v.getIndex(), newEstimate);
//      }
//    }
//  }

//	public static void Relax(Node firstNode, Node secondNode, double weight, ThreeHeapForBDD Q, int direction, double minPath){
//		double distance = firstNode.getTentativeDistances().get(direction) + weight;
//		if (secondNode.getTentativeDistances().get(direction) > distance){
//			secondNode.getTentativeDistances().set(direction, distance);
//			secondNode.setTentativeDistance(secondNode.getTentativeDistances().get(0) + secondNode.getTentativeDistances().get(1));
//			secondNode.setPredecessor(firstNode);
//			Q.increaseKey(secondNode, direction);
//		}
//	}
//	private static double checkShortestPath(ArrayList<Node> S, ThreeHeapForBDD Qs, ThreeHeapForBDD Qt) {
//		double bestPath = Qs.getMinNode().getTentativeDistances().get(0) + Qt.getMinNode().getTentativeDistances().get(1);
//		S.removeAll(Collections.singleton(null));
//		for (int i = 0; i < S.size(); i++){
//			if (S.get(i).getTentativeDistance() < bestPath){
//				bestPath = S.get(i).getTentativeDistance();
//			}
//		}
//		return bestPath;
//	}
}
