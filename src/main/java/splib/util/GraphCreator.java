package splib.util;

import splib.data.Graph;
import splib.data.Vertex;
import splib.data.SPVertex;
import splib.data.PlanarSPVertex;
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

 /**
  * Generate a complete graph, with the given number of vertices.
  * @param n The number of vertices in the graph.
  * @return The graph.
  */
 public static <V extends SPVertex> Graph<SPVertex> complete(Class<V> vClass,
     int n) throws InstantiationException, IllegalAccessException {
   Graph<SPVertex> G = new Graph();
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


  public static class SVGElement {
    private String tag;
    private Color stroke;
    private Color fill;
    private double strokeWidth;
    private double radius;

    public SVGElement(String tag, Color stroke, Color fill, double strokeWidth, double radius) {
      this.tag = tag;
      this.fill = fill;
      this.stroke = stroke;
      this.strokeWidth = strokeWidth;
      this.radius = radius;
    }

    public String getElement(Double x, Double y) {
      final String element = "<%s %s %s fill=\"%s\" stroke=\"%s\"/>";

      String position = "";
      if (this.tag == "rect") {
        x = x - this.radius;
        y = y - this.radius;
        position = String.format("width=\"%f\" height=\"%f\" x=\"%f\" y=\"%f\"", this.radius*2, this.radius*2, x, y);
      } else if (this.tag == "circle") {
        position = String.format("r=\"%f\" cx=\"%f\" cy=\"%f\"", this.radius, x, y);
      } else {
        return "";
      }

      String strokeWidth = String.format("stroke-width=\"%f\"", this.strokeWidth);

      String fill = "none";
      if (this.fill != null) {
        fill = String.format("rgb(%d, %d, %d)", this.fill.getRed(), this.fill.getGreen(), this.fill.getBlue());
      }
      String stroke = "none";
      if (this.stroke != null) {
        stroke = String.format("rgb(%d, %d, %d)", this.stroke.getRed(), this.stroke.getGreen(), this.stroke.getBlue());
      }

      return String.format(element, this.tag, position, strokeWidth, fill, stroke);
    }
  }


  public static <V extends SPVertex> void graphSVG(double scale, String path, Graph<V> G,
      List<Pair<Double, Double>> positions, SVGElement vertexElement, Pair<SVGElement, List<V>>... vertexSets) {

    double ratio = 1000d / scale;
    HashSet<SPVertex> vertexEdgesDrawn = new HashSet<SPVertex>();

    try {
      PrintWriter writer = new PrintWriter(path, "UTF-8");
      writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
      writer.println("<svg xmlns:svg=\"http://www.w3.org/2000/svg\"");
      writer.println("xmlns=\"http://www.w3.org/2000/svg\"");
      writer.println("width=\"1000px\" height=\"1000px\" version=\"1.1\">");

      for (int i = 0; i < G.getVertices().size(); i++) {
        V v = G.getVertices().get(i);
        vertexEdgesDrawn.add(v);
        for (Pair<Vertex, Double> adj : v.getAdjacency()) {
          if (!vertexEdgesDrawn.contains(adj.getItem1())) {
            int j = G.getVertices().indexOf((V)adj.getItem1());
            writer.println(String.format("<path d=\"M %1$.3f %2$.3f L %3$.3f %4$.3f\" stroke=\"black\" stroke-width=\"0.1\" fill=\"none\" />",
                  positions.get(j).getItem1() * ratio,
                  positions.get(j).getItem2() * ratio,
                  positions.get(i).getItem1() * ratio,
                  positions.get(i).getItem2() * ratio));
          }
        }
        writer.println(vertexElement.getElement(positions.get(i).getItem1() * ratio,
                                                positions.get(i).getItem2() * ratio));
      }

      for (Pair<SVGElement, List<V>> vSet : vertexSets) {
        for (V v : vSet.getItem2()) {
          int i = G.getVertices().indexOf(v);
            Pair<Double, Double> position = positions.get(i);
            writer.println(vSet.getItem1().getElement(position.getItem1() * ratio,
                                                      position.getItem2() * ratio));
        }
      }

      writer.println("</svg>");
      writer.close();
    } catch (Exception e) {
      System.out.println("Failed saving graph: " + e.toString());
      e.printStackTrace();
    }
  }


 /**
  * Build a planar graph.
  * @param V The number of vertices.
  * @return The generated graph.
  */
  public static <V extends SPVertex> Pair<Graph<V>, ArrayList<Pair<Double, Double>>> planar(Class<V> vClass, double scale, int n, int degree)
    throws InstantiationException, IllegalAccessException {
    Graph<V> G = new Graph<V>();
    ArrayList<Pair<Double, Double>> positions = new ArrayList();
    Random random = new Random();

    for (int i = 0; i < n; i++) {
      V v = vClass.newInstance();
      Pair<Double, Double> position = new Pair(random.nextDouble() * scale, random.nextDouble() * scale);
      positions.add(position);
      if (vClass.isAssignableFrom(PlanarSPVertex.class)) {
        ((PlanarSPVertex)v).setPosition(position.getItem1(), position.getItem2());
      }
      G.addVertex(v);
    }

    for (int i = 0; i < G.getVertices().size(); i++) {
      V v = (V)G.getVertices().get(i);
      Pair<Double, Double> vPos = positions.get(i);
      // We work with squared distances for simplicity
      Heap<Pair<V, Double>> sqdists =
        new Heap<Pair<V, Double>>((p1, p2) ->
          p1.getItem2().compareTo(p2.getItem2()), 2);

      for (int j = 0; j < G.getVertices().size(); j++) {
        V u = (V)G.getVertices().get(j);
        Pair<Double, Double> uPos = positions.get(j);
        double sqd = Math.pow(vPos.getItem1()
                            - uPos.getItem1(), 2)
                   + Math.pow(vPos.getItem2()
                            - uPos.getItem2(), 2);
        sqdists.insert(new Pair(u, Math.sqrt(sqd)));
      }

      for (int j = v.getAdjacency().size(); j < degree; j++) {
        Pair<V, Double> d;

        // add only new edges
        boolean exists = true;
        while (exists) {
          d = sqdists.extract();
          if (d.getItem1() == v) {
            continue;
          }
          exists = false;
          for (Pair<Vertex, Double> adj : v.getAdjacency()) {
            exists = exists || (V)adj.getItem1() == d.getItem1();
          }
          if (!exists) {
            G.addEdge(v, d.getItem1(), d.getItem2());
          }
        }
      }
    }

    return new Pair(G, positions);
  }


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
