package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database.getRecipesWithIngredient;


public final class RecipeFinder {

  private RecipeFinder() {
  }

  public static Map<Integer, ArrayList<String>> findRecipesWithIngredients(
          HashSet<String> ingredientList) {
    HashMap<String, Integer> recipeMap = new HashMap<>();
    // for every ingredient find recipes that correspond to that ingredient
    // count how many ingredients in ingredientList overlap with recipe ingredients
    for (String ingredient : ingredientList) {
      System.out.println(ingredient);
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
    TreeMap<String, Integer> sortedMap = new TreeMap<>(recipeMap);

    //create hashmap where keys are count and value is arraylist of recipes
    Map<Integer, ArrayList<String>> invertedHashMap = new HashMap<>();
    for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
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
    return invertedHashMap;
  }
}
