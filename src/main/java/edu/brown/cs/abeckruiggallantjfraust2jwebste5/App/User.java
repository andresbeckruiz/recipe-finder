package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;

import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Graph.Graph;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.Ingredient;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.Recipe;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.ConstantHyperparameters.DEFAULT_RATING;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.ConstantHyperparameters.NUM_RECOMMENDATIONS;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.RecipeFinder.findRecipesWithIngredients;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database.*;

public class User {
  private HashSet<String> ingredients;
  private HashMap<String, Double> recipeRatings;
  private HashMap<String, Double> ingredientRatings;
  private String name;
  private Graph<Recipe, Ingredient> recipeGraph;

  public User(String username, HashSet<String> ingredients) {
    this.name = username;
    this.ingredients = ingredients;
    recipeGraph = new Graph();
    recipeRatings = userRecipeRatings(name);
    ingredientRatings = userIngredientRatings(name);
  }

  public HashMap<String, Double> getIngredientRatings() {
    return ingredientRatings;
  }

  public HashMap<String, Double> getRecipeRatings() {
    return recipeRatings;
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
  public String getName() {
    return name;
  }

  public void addIngredient(String newIngredient) throws SQLException {
    if (this.ingredients == null) {
      this.ingredients = new HashSet<String>();
    }
    this.ingredients.add(newIngredient);
    addUserIngredient(this.name, newIngredient);
    this.addIngredientRating(newIngredient, DEFAULT_RATING);
  }

  public void removeIngredient(String ingredient) throws SQLException {
    this.ingredients.remove(ingredient);
    removeUserIngredient(this.name, ingredient);
  }

  public void addIngredientRating(String ingredient, Double rating) throws SQLException {
    addUserIngredientRating(this, ingredient, rating);
    ingredientRatings.put(ingredient, rating);
  }

  public void addRecipeRating(String recipe, Double rating) throws SQLException {
    addUserRecipeRating(this, recipe, rating);
    recipeRatings.put(recipe, rating);
  }

  public ArrayList<Recipe> cook() {
    ArrayList<String> recipeNames = findRecipesWithIngredients(ingredients, NUM_RECOMMENDATIONS);
    ArrayList<Recipe> recipes = new ArrayList<>();
    for (String recipe : recipeNames) {
      recipes.add(getRecipeObject(recipe, this));
    }
    return recipes;
  }

  public TreeMap<Recipe, Double> findSimilarRecipes(String recipe) {
    TreeMap<Recipe, Double> map;
    if (recipeGraph.getCentralNodeMap().containsKey(recipe)) {
      map = recipeGraph.search(recipeGraph.getCentralNodeMap().get(recipe));

      return map;
    }
    map = recipeGraph.search(Database.getRecipeObject(recipe, this));
    return map;
  }

  public Recipe findRecipe(String recipeName) {
    Recipe recipe = recipeGraph.getCentralNodeMap().get(recipeName);
    if (recipe != null) {
      return recipe;
    } else {
      return Database.getRecipeObject(recipeName, this);
    }
  }
}

