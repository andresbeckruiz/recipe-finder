package edu.brown.cs.abeckruiggallantjfraust2jwebste5.main;

import edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.User;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.Recipe;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.Ingredient;

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
public class RecipeIngredientTest {
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
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Tests Methods of Ingredient Class
   */
  @Test
  public void testIngredient() {
    this.setUp();
    String name = "tomato";
    Ingredient ingredient = new Ingredient(name, this.testUser);
    HashMap<String, Recipe> recipesAlreadyAdded = new HashMap<>();
    HashSet<Recipe> adjacentVertices = ingredient.getAdjacentVertices(recipesAlreadyAdded);

    assertTrue(ingredient.getName().equals("tomato"));
    ingredient.setValue(5.0);
    ingredient.setSimilarityScore(4.0);
    assertTrue(ingredient.getValue() == 5.0);
    assertTrue(ingredient.getSimilarityScore() == 4.0);
    assertTrue(adjacentVertices.size() > 0);
    tearDown();
  }

  /**
   * Tests Methods of Ingredient Class
   */
  @Test
  public void testRecipe() {
    this.setUp();
    ArrayList<String> params = new ArrayList<String>();
    params.add("recipeName");
    params.add("description");
    params.add("tomato");
    params.add("detailed");
    params.add("chef");
    params.add("instructions");
    params.add("cookingTime");
    params.add("prepTime");
    params.add("photourl");
    params.add("serves");
    params.add("url");
    params.add("owner");

    Recipe recipe = new Recipe(params, this.testUser);
    assertTrue(recipe.getChef().equals("chef"));
    tearDown();
  }
}