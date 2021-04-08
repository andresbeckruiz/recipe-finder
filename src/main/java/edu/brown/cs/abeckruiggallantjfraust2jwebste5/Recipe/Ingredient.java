package edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe;

import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Graph.Vertex;

import java.util.HashMap;
import java.util.HashSet;

public class Ingredient implements Vertex<Recipe> {

  private String name;
  private double similarityScore = 0;
  private HashSet<Recipe> adjRecipes = new HashSet<>();

  public Ingredient(String name) {
    this.name = name;
  }

  @Override
  public HashSet<Recipe> getAdjacentVertices(HashMap<String, Recipe> recipesAlreadyAdded) {
    if (adjRecipes.size() == 0) {
      String recipesString = Database.getRecipesWithIngredient(name);
      String[] rec = recipesString.trim().split("\\s*,\\s*");
      for (String recipeName : rec) {
        if (recipesAlreadyAdded.containsKey(recipeName)) {
          adjRecipes.add(recipesAlreadyAdded.get(recipeName));
        } else {
          adjRecipes.add(Database.getRecipeObject(recipeName));
        }
      }
    }
    return adjRecipes;
  }

  public String getName() {
    return name;
  }


  @Override
  public void setSimilarityScore(double score) {
    similarityScore = score;
  }

  @Override
  public double getSimilarityScore() {
    return similarityScore;
  }
}
