package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;

import edu.brown.cs.abeckruiggallantjfraust2jwebste5.RecipeObjects.Recipe;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.DatabaseHelpers.Database.*;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.DatabaseHelpers.UserDatabase.*;
import static org.junit.Assert.assertTrue;

public class RandomInputTesting1 {
  private User testUser;

  /**
   * Create new User object for creating ingredients and recipes
   */
  @Before
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
    addUserToDatabase("testUser", "test1@gmail.com");
    testUser = new User("test1@gmail.com", testIngredients);
  }

  /**
   * Clear variables for future tests.
   */
  @After
  public void tearDown() {
    testUser = null;
    try {
      deleteUser("test1@gmail.com");
      closeConn();
      closeUserConn();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   *
   */
  @Test
  public void randomInputTest() {
    //  List<String> allIngredients = new ArrayList<>();
    // System.out.println("HEY" + allIngredients);

//
//    try {
//      allIngredients = Files.readAllLines(Paths.get("ingredients.txt"));
//    } catch (IOException e) {
//      System.out.println(e.getMessage());
//    }

//    this.testUser.addIngredient("tomato");
//    this.testUser.addIngredient("egg");
//    this.testUser.addIngredient("spinach");
    ArrayList<Recipe> recipeSuggestions = this.testUser.cook();
    assertTrue(recipeSuggestions.get(0).getName().equals("tuna niçoise wrap"));
    assertTrue(recipeSuggestions.get(1).getName().equals("spinach orange and pine nut salad"));
  }
}
