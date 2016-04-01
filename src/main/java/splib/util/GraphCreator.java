package splib.util;

import splib.data.Graph;
import splib.data.SPVertex;
import splib.util.Pair;
import splib.util.Triple;
import javax.xml.parsers.SAXParser;
import java.util.HashMap;
import java.util.ArrayList;

import java.lang.Math;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

/**
 *
 */
public class GraphCreator {


  /**
   * Build a graph from an Open Street Maps export.
   * @param path The path to the osm-file.
   * @return The generated graph.
   */
  public static Graph<SPVertex> importOSM(String path) throws IOException,
         SAXException, ParserConfigurationException {
    Graph G = new Graph<SPVertex>();
    File file = new File(path);
    DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
      .newDocumentBuilder();
    Document doc = dBuilder.parse(file);

    // Create and populate HashMaps with nodes and ways
    HashMap<Integer, Triple<Integer, Float, Float>> nodes
      = new HashMap<Integer, Triple<Integer, Float, Float>>();
    ArrayList<ArrayList<Integer>> ways
      = new ArrayList<ArrayList<Integer>>();

    NodeList documentNodes = doc.getDocumentElement().getChildNodes();
    for (int i = 0; i < documentNodes.getLength(); i++) {
      Node n = documentNodes.item(i);
      NamedNodeMap attr = n.getAttributes();

      // Ignore node, if it is not an element, or if it has no attributes
      if (n.getNodeType() != Node.ELEMENT_NODE || attr == null) {
        continue;
      }

      //
      if (n.getNodeName() == "node") {
        Node idNode = attr.getNamedItem("id");
        Node lonNode = attr.getNamedItem("lon");
        Node latNode = attr.getNamedItem("lat");
        if (idNode == null || lonNode == null || latNode == null) {
          continue;
        }
        Float lon = Float.parseFloat(lonNode.getNodeValue());
        Float lat = Float.parseFloat(latNode.getNodeValue());
        Triple<Integer, Float, Float> nodeAttr = new Triple<Integer, Float,
          Float>(G.addVertex(new SPVertex()), lon, lat);
        Integer id = Integer.parseInt(idNode.getNodeValue());
        nodes.put(id, nodeAttr);
      } else if (n.getNodeName() == "way") {
        Node idNode = attr.getNamedItem("id");
        if (idNode == null) {
          continue;
        }
        // Figure out, if the way is a road
        boolean isRoad = false;
        NodeList nodeChildren = n.getChildNodes();
        for (int j = 0; j < nodeChildren.getLength(); j++) {
          Node t = nodeChildren.item(j);
          if (t.getNodeName() == "tag") {
            NamedNodeMap tagAttr = t.getAttributes();
            Node keyNode = tagAttr.getNamedItem("k");
            if (keyNode != null && keyNode.getNodeValue() == "highway") {
              isRoad = true;
            }
          } else {
            continue;
          }
        }
        if (!isRoad) {
          continue;
        }
        // get nodes
        ArrayList<Integer> nodeIds = new ArrayList<Integer>();
        for (int j = 0; j < nodeChildren.getLength(); j++) {
          Node t = nodeChildren.item(j);
          if (t.getNodeName() == "nd") {
            NamedNodeMap tagAttr = t.getAttributes();
            Node nodeIdNode = attr.getNamedItem("id");
            Integer nodeId = Integer.parseInt(nodeIdNode.getNodeValue());
            nodeIds.add(nodeId);
          }
        }
        Integer id = Integer.parseInt(idNode.getNodeValue());
        ways.add(nodeIds);
      }
    }

    Integer lastNodeId = null;
    for (ArrayList<Integer> way : ways) {
      for (Integer nodeId : way) {
        if (lastNodeId != null) {
          Triple<Integer, Float, Float> lastNode = nodes.get(lastNodeId);
          Triple<Integer, Float, Float> thisNode = nodes.get(nodeId);
          int weight = (int)Math.sqrt(Math.pow(lastNode.getItem2() - thisNode.getItem2(), 2)
                                    + Math.pow(lastNode.getItem3() - thisNode.getItem3(), 2)) * 10000000;
          G.addEdge(lastNode.getItem1(), thisNode.getItem1(), weight);
        }
        lastNodeId = nodeId;
      }
    }

    return G;
  }


}