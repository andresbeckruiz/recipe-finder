package edu.brown.cs.abeckruiggallantjfraust2jwebste5;

import edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.User;

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
import java.util.TreeMap;

import static org.junit.Assert.*;

/**
 * MethodTesting of Ingredient and Recipe Classes
 */
public class UserDataBaseTest {
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
   * Tests Methods of UserDatabase Class
   */
  @Test
  public void testDatabase() {
    this.setUp();

    String result = "";

    try {
      addUserIngredient("test@gmail.com", "milk");
      addUserIngredient("test@gmail.com", "flour");
      addUserIngredient("test@gmail.com", "egg");
      addUserIngredient("test@gmail.com", "lettuce");
      result = getUserInventory("test@gmail.com");
    } catch(SQLException e) {
      System.out.println(e.getMessage());
    }

    assertTrue(result.equals("milk,flour,egg,lettuce"));

    HashMap<String, Double> recipeRatings;
    HashMap<String, Double> ingredientRatings;

    recipeRatings = userRecipeRatings("test@gmail.com");
    ingredientRatings = userIngredientRatings("test@gmail.com");

    assertTrue(recipeRatings.size() == 0);
    assertTrue(ingredientRatings.size() == 0);

    this.tearDown();
  }
}