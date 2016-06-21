package splib.util;


import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.awt.Color;
import java.io.PrintWriter;

import splib.data.Vertex;
import splib.data.Graph;
import splib.util.Pair;
import splib.util.Triple;


public class GraphDrawer {


  public static class SVGPath {
    private Color stroke;
    private double strokeWidth;
    private String linecap;
    private double opacity;


    public SVGPath(Color stroke, double opacity, double strokeWidth, String linecap) {
      this.stroke = stroke;
      this.strokeWidth = strokeWidth;
      this.linecap = linecap;
      this.opacity = opacity;
    }


    public String getSVG(Double x1, Double y1, Double x2, Double y2) {
      final String format = "<path d=\"M %f %f L %f %f\" fill=\"none\" stroke-opacity=\"%f\" stroke=\"%s\" stroke-width=\"%f\" />";

      String stroke = "none";
      if (this.stroke != null) {
        stroke = String.format("rgb(%d, %d, %d)", this.stroke.getRed(), this.stroke.getGreen(), this.stroke.getBlue());
      }

      return String.format(format, x1, y1, x2, y2, this.opacity, stroke, this.strokeWidth);
    }
  }


  public static class SVGElement {
    private String tag;
    private Color stroke;
    private Color fill;
    private double strokeOpacity;
    private double fillOpacity;
    private double strokeWidth;
    private double radius;


    public SVGElement(String tag, Color stroke, double strokeOpacity, Color fill,
                      double fillOpacity, double strokeWidth, double radius) {
      this.tag = tag;
      this.fill = fill;
      this.stroke = stroke;
      this.strokeOpacity = strokeOpacity;
      this.fillOpacity = fillOpacity;
      this.strokeWidth = strokeWidth;
      this.radius = radius;
    }


    public String getSVG(Double x, Double y) {
      final String format = "<%s %s stroke-width=\"%f\" fill=\"%s\" fill-opacity=\"%f\" stroke=\"%s\" stroke-opacity=\"%f\"/>";

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

      String fill = "none";
      if (this.fill != null) {
        fill = String.format("rgb(%d, %d, %d)", this.fill.getRed(), this.fill.getGreen(), this.fill.getBlue());
      }
      String stroke = "none";
      if (this.stroke != null) {
        stroke = String.format("rgb(%d, %d, %d)", this.stroke.getRed(), this.stroke.getGreen(), this.stroke.getBlue());
      }

      return String.format(format, this.tag, position, this.strokeWidth, fill, this.fillOpacity, stroke, this.strokeOpacity);
    }
  }


  // public static <V extends Vertex> void graphSVG(double scale, String path, Graph<V> G,
  //     List<Pair<Double, Double>> positions, SVGElement vertexElement,
  //     List<Pair<SVGElement, List<V>>> vertexMarkLists,
  //     List<Triple<SVGPath, Integer, List<V>>> vertexPathLists) {

  //   ArrayList<Pair<SVGElement, ArrayList<Integer>>> indexMarks
  //     = new ArrayList<Pair<SVGElement, ArrayList<Integer>>>();
  //   for (Pair<SVGElement, List<V>> marks : vertexMarkLists) {
  //     ArrayList<Integer> indices = new ArrayList<Integer>();
  //     for (V mark : marks.getItem2()) {
  //       indices.add(G.getVertices().indexOf(mark));
  //     }
  //     indexMarks.add(new Pair(marks.getItem1(), indices));
  //   }

  //   ArrayList<Triple<SVGPath, Integer, ArrayList<Integer>>> indexPaths
  //     = new ArrayList<Pair<SVGPath, ArrayList<Integer>>>();
  //   for (Pair<SVGPath, List<V>> paths : vertexPathLists) {
  //     ArrayList<Integer> indices = new ArrayList<Integer>();
  //     for (V target : paths.getItem2()) {
  //       indices.add(G.getVertices().indexOf(target));
  //     }
  //     indexPaths.add(new Triple(paths.getItem1(), paths.getItem2(), indices));
  //   }

  //   graphSVG(scale, path, G, positions, indexMarks, indexPaths);
  // }


  public static void graphSVG(double scale, String path, Graph<?> G,
      List<Pair<Double, Double>> positions, SVGElement vertexElement,
      List<Pair<SVGElement, List<Integer>>> vertexMarkLists,
      List<Triple<SVGPath, Integer, List<Integer>>> vertexPathLists) {
    graphSVG(scale, 1000d, path, G, positions, vertexElement, vertexMarkLists, vertexPathLists);
  }

  public static void graphSVG(double scale, double size, String path, Graph<?> G,
      List<Pair<Double, Double>> positions, SVGElement vertexElement,
      List<Pair<SVGElement, List<Integer>>> vertexMarkLists,
      List<Triple<SVGPath, Integer, List<Integer>>> vertexPathLists) {

    double ratio = size / scale;
    HashSet<Integer> vertexEdgesDrawn = new HashSet<Integer>();

    try {
      PrintWriter writer = new PrintWriter(path, "UTF-8");
      writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
      writer.println("<svg xmlns:svg=\"http://www.w3.org/2000/svg\"");
      writer.println("xmlns=\"http://www.w3.org/2000/svg\"");
      writer.println("width=\"" + size + "\" height=\"" + size + "\" version=\"1.1\">");

      for (int i = 0; i < G.getVertexCount(); i++) {
        vertexEdgesDrawn.add(i);
        for (Pair<Integer, Double> adj : G.getAdjacency(i)) {
          if (!vertexEdgesDrawn.contains(adj.getItem1())) {
            int j = adj.getItem1();
            writer.println(String.format("<path d=\"M %1$.3f %2$.3f L %3$.3f %4$.3f\" stroke=\"black\" stroke-width=\"0.1\" fill=\"none\" />",
                  positions.get(j).getItem1() * ratio,
                  positions.get(j).getItem2() * ratio,
                  positions.get(i).getItem1() * ratio,
                  positions.get(i).getItem2() * ratio));
          }
        }
        if (vertexElement != null)
          writer.println(vertexElement.getSVG(positions.get(i).getItem1() * ratio,
                                              positions.get(i).getItem2() * ratio));
      }

      for (Pair<SVGElement, List<Integer>> marks : vertexMarkLists) {
        for (Integer v : marks.getItem2()) {
          if (v == null)
            continue;
          Pair<Double, Double> position = positions.get(v);
          writer.println(marks.getItem1().getSVG(position.getItem1() * ratio,
                                                 position.getItem2() * ratio));
        }
      }

      for (Triple<SVGPath, Integer, List<Integer>> vertexPaths : vertexPathLists) {
        if (vertexPaths.getItem2() == null)
          continue;
        int i = vertexPaths.getItem2();
        Pair<Double, Double> start = positions.get(i);
        for (Integer j : vertexPaths.getItem3()) {
          Pair<Double, Double> end = positions.get(j);
          writer.println(vertexPaths.getItem1().getSVG(start.getItem1() * ratio, start.getItem2() * ratio,
                end.getItem1() * ratio, end.getItem2() * ratio));
        }
      }

      writer.println("</svg>");
      writer.close();
    } catch (Exception e) {
      System.out.println("Failed saving graph: " + e.toString());
      e.printStackTrace();
    }
  }


}
