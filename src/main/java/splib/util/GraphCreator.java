package splib.util;

import splib.data.Graph;
import splib.data.Vertex;
import splib.data.SPVertex;
import splib.data.EuclidianSPVertex;
import splib.util.Pair;
import splib.util.Triple;

import javax.xml.parsers.SAXParser;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.awt.Color;

import java.lang.Math;
import java.io.File;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.Random;
import java.lang.RuntimeException;

import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

/**
 *
 */
public class GraphCreator {

public static <V extends SPVertex, U extends SPVertex> Graph<V> copy(Class<V>
    vClass, Graph<U> graph, ArrayList<Pair<Double, Double>> positions)  throws
  InstantiationException, IllegalAccessException {

  Graph<V> G = new Graph<V>();

  for (int i = 0; i < graph.getVertexCount(); i++) {
    G.addVertex(vClass.newInstance());
  }

  for (int i = 0; i < graph.getVertexCount(); i++) {
    for (Pair<Integer, Double> e : graph.getAdjacency(i)) {
      boolean existsInG = false;
      for (Pair<Integer, ?> f : G.getAdjacency(e.getItem1())) {
        existsInG = existsInG || f.getItem1().equals(i);
      }
      if (!existsInG) {
        G.addEdge(i, e.getItem1(), e.getItem2());
      }
    }


    // for (Pair<Integer, Double> edge : graph.getAdjacency(i)) {
    //   boolean exists = false;
    //   int j = edge.getItem1();
    //   for (Pair<Integer, ?> edge2 : G.getAdjacency(j)) {
    //     if (edge2.getItem1() == i) {
    //       exists = true;
    //     }
    //   }
    //   if (!exists) {
    //     G.addEdge(i, j, edge.getItem2());
    //   }
    // }
  }

  if (EuclidianSPVertex.class.isAssignableFrom(vClass))  {
    for (int i = 0; i < G.getVertexCount(); i++) {
      Pair<Double, Double> position = positions.get(i);
      ((EuclidianSPVertex)G.getVertex(i)).setPosition(position.getItem1(), position.getItem2());
    }
  }

  return G;
}


 /**
  * Generate a complete graph, with the given number of vertices.
  * @param n The number of vertices in the graph.
  * @return The graph.
  */
 public static <V extends SPVertex> Graph<V> complete(Class<V> vClass,
     int n) throws InstantiationException, IllegalAccessException {
   Graph<V> G = new Graph();
   Random random = new Random();

   for (int i = 0; i < n; i++) {
     G.addVertex(vClass.newInstance());
   }

   for (int i = 0; i < n; i++) {
     for (int j = i+1; j < n; j++) {
       G.addEdge(i, j, random.nextInt(9) + 1);
     }
   }

   return G;
 }


 /**
  * Generate a graph according to the Erdős–Rényi random graph model.
  * @param n The number of vertices.
  * @param p The probability of including a given edge.
  */
  public static <V extends SPVertex> Graph<V> erdosrenyi(Class<V> vClass, int n,
      float p) throws InstantiationException, IllegalAccessException {
   if (p < 0.0f || p > 1.0f) {
     throw new RuntimeException();
   }

   Graph<V> G = new Graph();
   Random random = new Random();

   for (int i = 0; i < n; i++) {
     G.addVertex(vClass.newInstance());
   }

   for (int i = 0; i < n; i++) {
     for (int j = i+1; j < n; j++) {
       if (random.nextFloat() < p) {
         G.addEdge(i, j, random.nextInt(9) + 1);
       }
     }
   }

   return G;
 }


 /**
  * Build a euclidian graph.
  * @param V The number of vertices.
  * @return The generated graph.
  */
  public static <V extends SPVertex> Pair<Graph<V>, ArrayList<Pair<Double, Double>>> euclidian(Class<V> vClass, double scale, int n, int degree)
    throws InstantiationException, IllegalAccessException {
    Graph<V> G = new Graph<V>();
    ArrayList<Pair<Double, Double>> positions = new ArrayList();
    Random random = new Random();

    for (int i = 0; i < n; i++) {
      V v = vClass.newInstance();
      Pair<Double, Double> position = new Pair(random.nextDouble() * scale, random.nextDouble() * scale);
      positions.add(position);
      if (EuclidianSPVertex.class.isAssignableFrom(vClass))  {
        ((EuclidianSPVertex)v).setPosition(position.getItem1(), position.getItem2());
      }
      G.addVertex(v);
    }

    for (int i = 0; i < G.getVertexCount(); i++) {
      Pair<Double, Double> vPos = positions.get(i);

      // We work with squared distances for simplicity
      Heap<Pair<Integer, Double>> sqdists =
        new Heap<Pair<Integer, Double>>((p1, p2) ->
          p1.getItem2().compareTo(p2.getItem2()), 2);

      for (int j = 0; j < G.getVertexCount(); j++) {
        Pair<Double, Double> uPos = positions.get(j);
        double sqd = Math.pow(vPos.getItem1()
                            - uPos.getItem1(), 2)
                   + Math.pow(vPos.getItem2()
                            - uPos.getItem2(), 2);
        sqdists.insert(new Pair(j, Math.sqrt(sqd)));
      }

      for (int j = G.getAdjacency(i).size(); j < degree; j++) {
        Pair<Integer, Double> d;

        // add only new edges
        boolean exists = true;
        while (exists) {
          d = sqdists.extract();
          if (d.getItem1().intValue() == i) {
            continue;
          }
          exists = false;
          for (Pair<Integer, Double> adj : G.getAdjacency(i)) {
            exists = exists || adj.getItem1().equals(d.getItem1());
          }
          for (Pair<Integer, Double> adj : G.getAdjacency(d.getItem1())) {
            exists = exists || adj.getItem1().intValue() == i;
          }
          if (!exists) {
            G.addEdge(i, d.getItem1(), d.getItem2());
          }
        }
      }
    }

    return new Pair(G, positions);
  }

  // private static boolean insideCircumcircle(double x, double y,
  //             Triple<Triple<V, Double, Double>,
  //                    Triple<V, Double, Double>,
  //                    Triple<V, Double, Double>> triangle) {

  // }

  // public static <V extends SPVertex> bowyerwatson(Class<V> vClass, int vCount, double scale)
  //   Graph<V> G = new Graph<V>();
  //   Random random = new Random();

  //   // triangulation := empty triangle mesh data structure
  //   ArrayList<Triple<Triple<V, Double, Double>,
  //                    Triple<V, Double, Double>,
  //                    Triple<V, Double, Double>>> triangulation
  //       = new ArrayList<Triple<Triple<V, Double, Double>,
  //                       Triple<V, Double, Double>,
  //                       Triple<V, Double, Double>>>();

  //   // add super-triangle to triangulation
  //   // must be large enough to completely contain all the points in pointList
  //   V t1 = vClass.newInstance();
  //   V t2 = vClass.newInstance();
  //   V t3 = vClass.newInstance();
  //   G.addVertex(t1); G.addVertex(t2); G.addVertex(t3);
  //   G.addEdge(0, 1); G.addEdge(1, 2); G.addEdge(2, 0);
  //   triangulation.add(Triple(new Triple(t1, scale * 0.50, scale * 1.50),
  //                            new Triple(t2, - (1 / (1.0 - 1.50)) * 0.50 * scale, 0.0),
  //                            new Triple(t3, 0.5 + (1 / (1.0 - 1.50)) * 0.50 * scale, 0.0)));

  //   for (int i = 0; i < vCount; i++) { // add all the points one at a time to the triangulation
  //     Pair<Double, Double> position = new Pair(random.nextDouble() * scale,
  //         random.nextDouble() * scale);
  //     V v = vClass.newInstance();

  //     ArrayList<Triple<Triple<V, Double, Double>,
  //                      Triple<V, Double, Double>,
  //                      Triple<V, Double, Double>>> badTriangles
  //         = new ArrayList<Triple<Triple<V, Double, Double>,
  //                         Triple<V, Double, Double>,
  //                         Triple<V, Double, Double>>>();
  //     // first find all the triangles that are no longer valid due to the insertion
  //     for (Triple<?, ?, ?> triangle : triangulation) {
  //       if (insideCircumcircle(position.getItem1(), position.getItem2(), triangle)) {
  //         badTriangles.add(triangle);
  //       }
  //     }

  //     ArrayList<Pair<Triple<V, Double, Double>, Triple<V, Double, Double>>> polygon =
  //       new ArrayList<Pair<Triple<V, Double, Double>, Triple<V, Double, Double>>>();

  //     // find the boundary of the polygonal hole
  //     for (int i = 0; i < badTriangles.size(); i++) {
  //       Triple<?, ?, ?> triangle = badTriangles.get(i);
  //       // edges are not shared by any other triangles in badTriangles
  //       boolean shared = false;
  //       for (int j = 0; j < badTriangles.size(); j++) {
  //         if (j == i)
  //           continue;
  //         Triple<?, ?, ?> otherTriangle = badTriangles.get(j);
  //         if (triangle.getItem1() == otherTriangle.getItem1() && triangle.getItem2() == otherTriangle.getItem2()
  //          || triangle.getItem1() == otherTriangle.getItem2() && triangle.getItem2() && otherTriangle.getItem2()
  //          || triangle.getItem1() == otherTriangle.getItem3() && triangle.getItem2() && otherTriangle.getItem2()
  //          || triangle.getItem2() == otherTriangle.getItem1() && triangle.getItem2() && otherTriangle.getItem2()
  //          || triangle.getItem2() == otherTriangle.getItem2() && triangle.getItem2() && otherTriangle.getItem2()
  //          || triangle.getItem2() == otherTriangle.getItem3() && triangle.getItem2() && otherTriangle.getItem2()
  //          || triangle.getItem3() == otherTriangle.getItem1() && triangle.getItem2() && otherTriangle.getItem2()
  //          || triangle.getItem3() == otherTriangle.getItem2() && triangle.getItem2() && otherTriangle.getItem2()
  //          || triangle.getItem3() == otherTriangle.getItem3() && triangle.getItem2() && otherTriangle.getItem2()
  //          )
  //           1, 1 && 2, 2
  //           1, 2 && 2, 3
  //           1, 3 && 2, 1

  //           2, 1 && 3, 2
  //           2, 2 && 3, 3
  //           2, 3 && 3, 1

  //           3, 1 && 1, 2
  //           3, 2 && 1, 3
  //           3, 3 && 1, 1

  //       }
  //     }

//     }

//       for each point in pointList do kk
//          for each triangle in badTriangles do
//             for each edge in triangle do
//                if kk
//                   add edge to polygon
//          for each triangle in badTriangles do // remove them from the data structure
//             remove triangle from triangulation
//          for each edge in polygon do // re-triangulate the polygonal hole
//             newTri := form a triangle from edge to point
//             add newTri to triangulation
//       for each triangle in triangulation // done inserting points, now clean up
//          if triangle contains a vertex from original super-triangle
//             remove triangle from triangulation
//       return triangulation


//  /**
//   * Build a graph from an Open Street Maps export.
//   * @param path The path to the osm-file.
//   * @return The generated graph.
//   */
//  public static Graph<SPVertex> importOSM(String path) throws IOException,
//         SAXException, ParserConfigurationException {
//    Graph G = new Graph<SPVertex>();
//    File file = new File(path);
//    DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
//      .newDocumentBuilder();
//    Document doc = dBuilder.parse(file);

//    // Create and populate HashMaps with nodes and ways
//    HashMap<Integer, Triple<Integer, Float, Float>> nodes
//      = new HashMap<Integer, Triple<Integer, Float, Float>>();
//    ArrayList<ArrayList<Integer>> ways
//      = new ArrayList<ArrayList<Integer>>();

//    NodeList documentNodes = doc.getDocumentElement().getChildNodes();
//    for (int i = 0; i < documentNodes.getLength(); i++) {
//      Node n = documentNodes.item(i);
//      NamedNodeMap attr = n.getAttributes();

//      // Ignore node, if it is not an element, or if it has no attributes
//      if (n.getNodeType() != Node.ELEMENT_NODE || attr == null) {
//        continue;
//      }

//      //
//      if (n.getNodeName() == "node") {
//        Node idNode = attr.getNamedItem("id");
//        Node lonNode = attr.getNamedItem("lon");
//        Node latNode = attr.getNamedItem("lat");
//        if (idNode == null || lonNode == null || latNode == null) {
//          continue;
//        }
//        Float lon = Float.parseFloat(lonNode.getNodeValue());
//        Float lat = Float.parseFloat(latNode.getNodeValue());
//        Triple<Integer, Float, Float> nodeAttr = new Triple<Integer, Float,
//          Float>(G.addVertex(new SPVertex()), lon, lat);
//        Integer id = Integer.parseInt(idNode.getNodeValue());
//        nodes.put(id, nodeAttr);
//      } else if (n.getNodeName() == "way") {
//        Node idNode = attr.getNamedItem("id");
//        if (idNode == null) {
//          continue;
//        }
//        // Figure out, if the way is a road
//        boolean isRoad = false;
//        NodeList nodeChildren = n.getChildNodes();
//        for (int j = 0; j < nodeChildren.getLength(); j++) {
//          Node t = nodeChildren.item(j);
//          if (t.getNodeName() == "tag") {
//            NamedNodeMap tagAttr = t.getAttributes();
//            Node keyNode = tagAttr.getNamedItem("k");
//            if (keyNode != null && keyNode.getNodeValue() == "highway") {
//              isRoad = true;
//            }
//          } else {
//            continue;
//          }
//        }
//        if (!isRoad) {
//          continue;
//        }
//        // get nodes
//        ArrayList<Integer> nodeIds = new ArrayList<Integer>();
//        for (int j = 0; j < nodeChildren.getLength(); j++) {
//          Node t = nodeChildren.item(j);
//          if (t.getNodeName() == "nd") {
//            NamedNodeMap tagAttr = t.getAttributes();
//            Node nodeIdNode = attr.getNamedItem("id");
//            Integer nodeId = Integer.parseInt(nodeIdNode.getNodeValue());
//            nodeIds.add(nodeId);
//          }
//        }
//        Integer id = Integer.parseInt(idNode.getNodeValue());
//        ways.add(nodeIds);
//      }
//    }

//    Integer lastNodeId = null;
//    for (ArrayList<Integer> way : ways) {
//      for (Integer nodeId : way) {
//        if (lastNodeId != null) {
//          Triple<Integer, Float, Float> lastNode = nodes.get(lastNodeId);
//          Triple<Integer, Float, Float> thisNode = nodes.get(nodeId);
//          int weight = (int)Math.sqrt(Math.pow(lastNode.getItem2() - thisNode.getItem2(), 2)
//                                    + Math.pow(lastNode.getItem3() - thisNode.getItem3(), 2)) * 10000000;
//          G.addEdge(lastNode.getItem1(), thisNode.getItem1(), weight);
//        }
//        lastNodeId = nodeId;
//      }
//    }

//    return G;
//  }

}
