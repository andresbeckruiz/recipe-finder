package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;

import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.Ingredient;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database.getRecipeObject;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database.getRecipesWithIngredient;

public final class CreateUserMap {
  private CreateUserMap() {
  }
  public static Map<Ingredient, ArrayList<Recipe>> createRecipeMap(HashSet<String> ingredients) {
    return recursiveMapCreationHelper(null, ingredients, true);
  }

  private static Map<Ingredient, ArrayList<Recipe>> recursiveMapCreationHelper(Map<Ingredient,
          ArrayList<Recipe>> map,
          HashSet<String> ingredients, boolean firstTime) {
    Map<Ingredient, ArrayList<Recipe>> nodeMap;
    if (map == null) {
      nodeMap = new HashMap<>();
    } else {
      nodeMap = map;
    }
    HashSet<String> additionalIngredients = new HashSet<>();
    for (String ing : ingredients) {
      Ingredient newIngredient = new Ingredient(ing);
      ArrayList<Recipe> recipesPerIngredient = new ArrayList<>();
      // for recipe in new Ingredient
      // create new recipe object if necessary
      // add newIngredient to recipe's list of ingredients
      // add recipe to recipe objects
      String[] recipes = getRecipesWithIngredient(ing).split("\\s*,\\s*");
      for (String recipe : recipes) {
        Recipe rec = getRecipeObject(recipe);
        recipesPerIngredient.add(rec);
        additionalIngredients.addAll(rec.getIngredients());
      }
      nodeMap.put(newIngredient, recipesPerIngredient);
    }
    if (firstTime) {
      nodeMap = recursiveMapCreationHelper(nodeMap, additionalIngredients, false);
    }
    return nodeMap;
  }
}
