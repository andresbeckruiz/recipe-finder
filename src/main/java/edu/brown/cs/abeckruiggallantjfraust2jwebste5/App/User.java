package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;

import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Graph.Graph;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.Recipe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.CreateUserMap.createRecipeMap;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.RecipeFinder.findRecipesWithIngredients;

public class User {
  private HashSet<String> ingredients;
  private String name;
  private Graph recipeGraph;

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

  public void cook() {
    try {
      // TODO: change for different number of top recipes
      final int topXRecipes = 10;
      Map<Integer, ArrayList<String>> map = findRecipesWithIngredients(ingredients);
      int numRecipesQueried = 0;
      for (Integer count: map.keySet()) {
        for (int i = 0; i < map.get(count).size(); i++) {
          Recipe startSearch = Database.getRecipeObject(map.get(count).get(i));
          System.out.println(recipeGraph.search(startSearch));
          numRecipesQueried++;
          if (numRecipesQueried == topXRecipes) {
            return; //TODO: maybe don't return?
          }
        }

      }
    } catch (Exception e) { //TODO: something here!
      System.out.println(e.getMessage());
    }
  }

}

