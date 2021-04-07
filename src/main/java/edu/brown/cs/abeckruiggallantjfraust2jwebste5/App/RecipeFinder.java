package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.Database.*;

public final class RecipeFinder {

  public static void findRecipeWithIngredients(ArrayList<String> ingredientList) throws SQLException, ClassNotFoundException {
    initialize("data/newdb.sqlite3");
    HashSet<String> recipeList = new HashSet();
    for(String ingredient : ingredientList) {
      String recipes = getRecipesWithIngredient(ingredient);
      String rec[] = recipes.trim().split("\\s*,\\s*");
      HashSet<String> newRecipes = new HashSet(Arrays.asList(rec));
      if (!recipeList.isEmpty()) {
        recipeList.retainAll(newRecipes);
      } else {
        recipeList = newRecipes;
      }
    }
    System.out.println("recipes with: " + ingredientList.toString());
    for (String s : recipeList) {
      System.out.println(getRecipe(s));
      //System.out.println(s);
    }
  }
}
