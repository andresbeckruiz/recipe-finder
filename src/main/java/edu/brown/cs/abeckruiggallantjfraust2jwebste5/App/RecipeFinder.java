package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;

import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.Recipe;

import java.util.*;

import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.ConstantHyperparameters.SCORE_WEIGHT;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.ConstantHyperparameters.SIMILARITY_WEIGHT;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database.getRecipeObject;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database.getRecipesWithIngredient;
import static java.lang.Math.min;

public final class RecipeFinder {

  private RecipeFinder() {
  }

  public static ArrayList<String> findRecipesWithIngredients(
          HashSet<String> ingredientList, int numRecipesToReturn, User curUser) {
    LinkedHashMap<String, Integer> recipeMap = new LinkedHashMap<>();
    // for every ingredient find recipes that correspond to that ingredient
    // count how many ingredients in ingredientList overlap with recipe ingredients
    for (String ingredient : ingredientList) {
      String recipesString = getRecipesWithIngredient(ingredient);
      if (recipesString == null) {
        continue;
      }
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

    TreeMap<Integer, ArrayList<String>> top = treeMapOfTop(invertedHashMap, numRecipesToReturn);
    TreeMap<Double, ArrayList<String>> ratedMap = factorInRatings(top, curUser);
    ArrayList<String> topSortedRecipes = new ArrayList<>();
    int numToAdd = numRecipesToReturn;
    for (double key : ratedMap.keySet()) {
      ArrayList<String> rep = ratedMap.get(key);

      topSortedRecipes.addAll(rep.subList(0, min(numToAdd, rep.size())));
      numToAdd = numToAdd - rep.size();
      if (numToAdd < 0) {
        return topSortedRecipes;
      }
    }
    return topSortedRecipes;
  }

  private static TreeMap<Integer, ArrayList<String>> treeMapOfTop(Map<Integer, ArrayList<String>> allMap, int numToReturn) {
    int numToAdd = numToReturn * 2;
    TreeMap<Integer, ArrayList<String>> top = new TreeMap<>();
    for (int key : allMap.keySet()) {
      ArrayList<String> rep = allMap.get(key);
      rep = new ArrayList<>(rep.subList(0, min(numToAdd, rep.size())));
      top.put(key, rep);
      numToAdd = numToAdd - rep.size();
      if (numToAdd < 0) {
        return top;
      }
    }
    return top;
  }

  private static TreeMap<Double, ArrayList<String>> factorInRatings(Map<Integer, ArrayList<String>> map, User user) {
    TreeMap<Double, ArrayList<String>> mapWithRatings = new TreeMap<>(Collections.reverseOrder());
    for (int count = map.keySet().size(); count > 0; count--) {
      ArrayList<String> rep = map.get(count);
      for (String recipe : rep) {

        Recipe recipeObj = getRecipeObject(recipe, user);
        if (recipeObj == null) {
          continue;
        }
        Double metric = count * SCORE_WEIGHT + SIMILARITY_WEIGHT * recipeObj.getValue();
        ArrayList<String> newRating = new ArrayList<>();
        if (mapWithRatings.get(metric) == null) {
          newRating = new ArrayList<>();
          newRating.add(recipe);
          mapWithRatings.put(metric, newRating);
        } else {
          newRating = mapWithRatings.get(metric);
          newRating.add(recipe);
          mapWithRatings.put(metric, newRating);
        }
      }
    }
    return mapWithRatings;
  }
}
