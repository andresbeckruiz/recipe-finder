package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;

import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database.getRecipesWithIngredient;
import static java.lang.Math.min;

public final class RecipeFinder {

  private RecipeFinder() {
  }

  public static ArrayList<String> findRecipesWithIngredients(
          HashSet<String> ingredientList, int numRecipesToReturn) {
    LinkedHashMap<String, Integer> recipeMap = new LinkedHashMap<>();
    // for every ingredient find recipes that correspond to that ingredient
    // count how many ingredients in ingredientList overlap with recipe ingredients
    for (String ingredient : ingredientList) {
      String recipesString = getRecipesWithIngredient(ingredient);
      String[] rec = recipesString.trim().split("\\s*,\\s*");
      for (String recipe : rec) {
        // get the value of the specified key
        Integer count = recipeMap.get(recipe);
        // if the map contains no mapping for the key,
        // map the key with a value of 1
        if (count == null) {
          recipeMap.put(recipe, 1);
        } else {  // else increment the found value by 1
          recipeMap.put(recipe, count + 1);
        }
      }
    }

    //create hashmap where keys are count and value is arraylist of recipes
    Map<Integer, ArrayList<String>> invertedHashMap = new HashMap<>();
    for (Map.Entry<String, Integer> entry : recipeMap.entrySet()) {
      ArrayList<String> recipe = invertedHashMap.get(entry.getValue());
      if (recipe == null) {
        ArrayList<String> recipeList = new ArrayList<>();
        recipeList.add(entry.getKey());
        invertedHashMap.put(entry.getValue(), recipeList);
      } else {
        recipe.add(entry.getKey());
        invertedHashMap.put(entry.getValue(), recipe);
      }
    }

    ArrayList<String> topSortedRecipes = new ArrayList<>();
    int numToAdd = numRecipesToReturn;
    for (int count = invertedHashMap.keySet().size(); count > 0; count--) {
      ArrayList<String> rep = invertedHashMap.get(count);
      topSortedRecipes.addAll(rep.subList(0, min(numToAdd, rep.size())));
      numToAdd = numToAdd - rep.size();
      if (numToAdd < 0) {
        return topSortedRecipes;
      }
    }
    return topSortedRecipes;
  }
}
