package edu.brown.cs.abeckruiggallantjfraust2jwebste5.main;

import edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.User;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.RecipeObjects.Recipe;

import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database.*;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.UserDatabase.*;

import java.util.HashSet;
import java.sql.SQLException;

import java.util.ArrayList;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * MethodTesting of Ingredient and Recipe Classes
 */
public class UserSearchTest {
  private User testUser;

  /**
   * Create new User object for creating ingredients and recipes
   */
  public void setUp() {
    HashSet<String> testIngredients = new HashSet<>();
    try {
      initialize("data/newdb.sqlite3");
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
  public void tearDown() {
    testUser = null;
    try {
      deleteUser("test@gmail.com");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Tests cook() method
   */
  @Test
  public void searchTest() {
    this.setUp();
    try {
      this.testUser.addIngredient("tomato");
      this.testUser.addIngredient("egg");
      this.testUser.addIngredient("spinach");
      this.testUser.addIngredientRating("tomato",3.0);
      this.testUser.removeIngredient("egg");
    } catch(SQLException e) {
      System.out.println(e.getMessage());
    }
    ArrayList<Recipe> recipeSuggestions = this.testUser.cook();
    assertTrue(recipeSuggestions.size() == 10);

    try {
      this.testUser.addIngredientRating("tomato",3.0);
      this.testUser.removeIngredient("tomato");
    } catch(SQLException e){
      System.out.println(e.getMessage());
    }
    recipeSuggestions = this.testUser.cook();
    assertTrue(recipeSuggestions.size() != 0);

    String s = "";
    try {
      this.testUser.addIngredientRating("tomato",3.0);
      this.testUser.addIngredientRating("tomato",5.0);
      s = getUserIngredientRatings("test@gmail.com");
    } catch(SQLException e){
      System.out.println(e.getMessage());
    }
    assertTrue(!(s.contains("tomato:3.0")));
    assertTrue(s.contains("tomato:5.0"));
    tearDown();
  }
}