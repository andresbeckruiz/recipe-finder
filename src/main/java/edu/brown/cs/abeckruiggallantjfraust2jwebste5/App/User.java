package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;

import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Graph.Graph;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.Ingredient;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.Recipe;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.RecipeFinder.findRecipesWithIngredients;

public class User {
  private HashSet<String> ingredients;
  private String name;
  private Graph<Recipe, Ingredient> recipeGraph;

  public User(String username, HashSet<String> ingredients) {
    this.name = username;
    this.ingredients = ingredients;
    recipeGraph = new Graph();
  }

  public Graph getUserGraph() {
    return recipeGraph;
  }

  public HashSet<String> getIngredients() {
    return ingredients;
  }

  public void setIngredients(HashSet<String> ingredients) {
    this.ingredients = ingredients;
  }

  public ArrayList<String> cook() {
    // TODO: change for different number of top recipes
    //map from number overlapping ingredients to recipe list
    final int numRecipesToReturn = 10;
    return findRecipesWithIngredients(ingredients, numRecipesToReturn);
  }
  public TreeMap<Recipe, Double> findSimilarRecipes(String recipe) {
    if (recipeGraph.getCentralNodeMap().containsKey(recipe)) {
      return recipeGraph.search(recipeGraph.getCentralNodeMap().get(recipe));
    }
    return recipeGraph.search(Database.getRecipeObject(recipe));
  }

}

