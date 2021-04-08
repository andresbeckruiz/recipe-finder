package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.Database.*;

public final class RecipeFinder {

  public static void findRecipesWithIngredients(HashSet<String> ingredientList) throws SQLException, ClassNotFoundException {
    initialize("data/newdb.sqlite3");
    Map<Double, Recipe> rankedSimilarRecipes = new HashMap<Double, Recipe>();
    HashMap<String, Integer> recipeMap = new HashMap<>();
    // for every ingredient find recipes that correspond to that ingredient
    // count how many ingredients in ingredientList overlap with recipe ingredients
    for(String ingredient : ingredientList) {
      System.out.println(ingredient);
      String recipesString = getRecipesWithIngredient(ingredient);
      String rec[] = recipesString.trim().split("\\s*,\\s*");
      for (String recipe : rec) {
        // get the value of the specified key
        Integer count = recipeMap.get(recipe);
        // if the map contains no mapping for the key,
        // map the key with a value of 1
        if (count == null) {
          recipeMap.put(recipe, 1);
        }
        // else increment the found value by 1
        else {
          recipeMap.put(recipe, count + 1);
        }
      }
    }
    TreeMap<String, Integer> sortedMap = new TreeMap<>(recipeMap);

    //create hashmap where keys are count and value is arraylist of recipes
    Map<Integer, ArrayList<String>> myNewHashMap = new HashMap<>();
    for(Map.Entry<String, Integer> entry : sortedMap.entrySet()){
      ArrayList<String> recipe = myNewHashMap.get(entry.getValue());
      if (recipe == null) {
        ArrayList<String> recipeList = new ArrayList<>();
        recipeList.add(entry.getKey());
        myNewHashMap.put(entry.getValue(), recipeList);
      } else {
        recipe.add(entry.getKey());
        myNewHashMap.put(entry.getValue(), recipe);
      }
    }
  }

}
