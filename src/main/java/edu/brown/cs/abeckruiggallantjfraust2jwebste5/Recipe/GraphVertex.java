package edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe;
import java.util.ArrayList;

public class GraphVertex {
  private Double score;
  private ArrayList<GraphEdge> edges;
  private double rating;

  public GraphVertex() {
    this.score = 0.0;
    this.edges = new ArrayList<GraphEdge>();
    this.rating = 2.5;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  public Double getScore() {
    return score;
  }

  public void setRating(double newRating) {this.rating = newRating;}

  public Double getRating() {return this.rating;}

  public ArrayList<GraphEdge> getEdges() {
    return edges;
  }
}