package edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe;

import java.util.ArrayList;

public class Recipe {

  private String recipeName;
  private GraphVertex vertex;

  public Recipe(ArrayList<String> params) {
    this.recipeName = params.get(0);
    this.vertex = new GraphVertex();
  }

  public GraphVertex getVertex() {
    return vertex;
  }

  public String getName() {
    return recipeName;
  }

  public void addIngredient(Ingredient ingredient) {
    vertex.getEdges().add(new GraphEdge(ingredient, this.vertex));
  }
}