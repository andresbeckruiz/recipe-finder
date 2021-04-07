package edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe;

public class GraphEdge {

  private Ingredient ingredient;
  private GraphVertex graphVertex;

  public GraphEdge(Ingredient ingredient, GraphVertex graphVertex) {
    this.ingredient = ingredient;
    this.graphVertex = graphVertex;
  }

  public GraphVertex getGraphVertex() {
    return graphVertex;
  }

  public Ingredient getIngredient() {
    return ingredient;
  }
}