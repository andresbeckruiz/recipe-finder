package edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.User;

import java.util.HashMap;
import java.util.ArrayList;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.Database.*;

public final class RecipeGraph {

  private HashMap<String, Ingredient> ingredientObjects;
  private HashMap<String, Recipe> recipeObjects;
  private ArrayList<GraphVertex> graphVertices;

  public RecipeGraph() {}

  public ArrayList<GraphVertex> getGraphVertices() { return graphVertices; }
  public HashMap<String, Ingredient> getIngredientObjects() { return ingredientObjects; }
  public HashMap<String, Recipe> getRecipeObjects() { return recipeObjects; }

  public void initializeGraph(User user) {
    graphVertices = new ArrayList<>();
    ingredientObjects = new HashMap<>();
    recipeObjects = new HashMap<>();

    //For every ingredient in database, create ingredient object
    ArrayList<String> ingredients = user.getIngredients();

    for (String ing : ingredients) {
      Ingredient newIngredient = new Ingredient(ing);
      ingredientObjects.put(newIngredient.getName(), newIngredient);
      // for recipe in new Ingredient
      // create new recipe object if necessary
      // add newIngredient to recipe's list of ingredients
      // add recipe to recipe objects
      String[] recipes = getRecipesWithIngredient(ing).split(",");

      for (int i = 0; i < recipes.length; i ++) {
        if (!(recipeObjects.containsKey(recipes[i]))) {
          Recipe newRecipe = getRecipe(recipes[i]);
          newRecipe.addIngredient(newIngredient);
          recipeObjects.put(newRecipe.getTitle(), newRecipe);
        } else {
          recipeObjects.get(recipes[i]).addIngredient(newIngredient);
        }
      }
    }
  }
}
