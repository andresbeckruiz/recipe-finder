package edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe;

import edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.User;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Graph.Vertex;

import java.util.HashMap;
import java.util.HashSet;

import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.ConstantHyperparameters.DEFAULT_RATING;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.ConstantHyperparameters.TOTAL_RATING;

public class Ingredient implements Vertex<Recipe> {
  private String name;
  private User owner;
  private Double rating;
  private double similarityScore = 0;
  private HashSet<Recipe> adjRecipes = new HashSet<>();

  public Ingredient(String name, User user) {
    this.name = name;
    this.owner = user;
    if (user.getIngredientRatings().keySet().contains(name)) {
      this.rating = user.getIngredientRatings().get(name) / TOTAL_RATING;
    } else {
      this.rating = DEFAULT_RATING;
    }
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
          adjRecipes.add(Database.getRecipeObject(recipeName, this.owner));
        }
      }
    }
    HashSet<Recipe> adjRecipesToReturn = new HashSet<>();
    adjRecipesToReturn.addAll(adjRecipes);
    return adjRecipesToReturn;
  }

  public String getName() {
    return name;
  }

  @Override
  public Double getValue() {
    return rating;
  }

  @Override
  public void setValue(double value) {
    this.rating = value;
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
