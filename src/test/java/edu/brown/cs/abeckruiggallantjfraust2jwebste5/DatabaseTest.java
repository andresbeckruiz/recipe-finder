package edu.brown.cs.abeckruiggallantjfraust2jwebste5;

import edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.User;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.RecipeObjects.Recipe;

import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database.*;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.UserDatabase.*;

import java.util.HashSet;
import java.sql.SQLException;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * MethodTesting of Ingredient and Recipe Classes
 */
public class DatabaseTest {
  private User testUser;

  /**
   * Create new User object for creating ingredients and recipes
   */
  public void setUp() {
    HashSet<String> testIngredients = new HashSet<>();
    try {
      initialize("data/newdb.sqlite3");
      initializeConn(getConn());
    } catch (SQLException e) {
      System.out.println("ERROR");
    } catch (ClassNotFoundException e) {
      System.out.println("ERROR");
    }
    try {
      addUserToDatabase("testUser", "test@gmail.com");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    testUser = new User("test@gmail.com", testIngredients);
  }

  /**
   * Clear variables for future tests.
   */
  @After
  public void tearDown() {
    testUser = null;
    try {
      deleteUser("test@gmail.com");
      closeConn();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Tests Methods of Database Class
   */
  @Test
  public void testDatabase() {
    this.setUp();

    String testIngredient = "tomato";
    String testRecipe = "spinach mash";

    String tomatoRecipes = getRecipesWithIngredient(testIngredient);
    String spinachMashIngredients = getIngredientForRecipe(testRecipe);
    Recipe recipe = getRecipeObject("spinach mash", this.testUser);

    assertTrue(tomatoRecipes.contains("keralan crab with currimbhoy salad"));
    assertTrue(spinachMashIngredients.equals("olive oil,spinach,garlic,mashed potato"));

    assertTrue(recipe.getSimilarityScore() == 0.0);
    assertTrue(recipe.getIngredients() != null);
    assertTrue(recipe.getChef().equals("Paul Rankin"));
    assertTrue(recipe.getInstructions() != null);
    assertTrue(recipe.getCookingTime().equals("10"));
    assertTrue(recipe.getPrepTime().equals("30"));
    assertTrue(recipe.getPhotourl() != null);
    assertTrue(recipe.getServes().equals("1"));
    assertTrue(recipe.getUrl().equals("http://bbc.co.uk/food/recipes/cheeseandspinachmash_90409"));
    this.tearDown();
  }
}