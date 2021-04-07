package edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe;
import java.util.ArrayList;

public class GraphVertex {
  private Double score;
  private ArrayList<GraphEdge> edges;

  public GraphVertex() {
    this.score = 0.0;
    this.edges = new ArrayList<GraphEdge>();
  }

  public void setScore(Double score) {
    this.score = score;
  }

  public Double getScore() {
    return score;
  }

  public ArrayList<GraphEdge> getEdges() {
    return edges;
  }
}