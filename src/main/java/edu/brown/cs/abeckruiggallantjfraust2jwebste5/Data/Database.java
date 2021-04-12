package edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data;

import edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.User;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.Recipe;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public final class Database {
  private Database() { }
  private static Connection conn;
  /**
   * Initializes connection to database.
   * @param filename From which to retrieve data
   * @throws ClassNotFoundException thrown if org.sqlite.JDBC is not found
   * @throws SQLException when querying and encounters an unexptected error
   */
  public static void initialize(String filename) throws ClassNotFoundException, SQLException {
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filename;
    conn = DriverManager.getConnection(urlToDB);
    // these two lines tell the database to enforce foreign
    // keys during operations
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");
  }


  public static void createIngredientDatabase() throws SQLException {
    try {
      PreparedStatement prep = conn.prepareStatement("DROP TABLE IF EXISTS ingredientMap");
      prep.execute();
      prep = conn.prepareStatement(
              "CREATE TABLE IF NOT EXISTS ingredientMap("
                      + "ingredient TEXT PRIMARY KEY, "
                      + "recipes TEXT);");
      prep.execute();
      prep.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public static void addIngredient(String ingredient, String recipes) {
    try {
      if (conn != null) {
        PreparedStatement prep = conn.prepareStatement(
                "INSERT INTO ingredientMap "
                        + "VALUES (?, ?)");
        prep.setString(1, ingredient);
        prep.setString(2, recipes);
        prep.execute();
        prep.close();
      }
    } catch (Exception e) {
      if (e.getMessage().contains("constraint violation")) {
        return;
      } else {
        System.out.println(e.getMessage());
      }
    }

  }
  public static void createRecipeDatabase() throws SQLException {
    try {
      PreparedStatement prep = conn.prepareStatement("DROP TABLE IF EXISTS recipes");
      prep.execute();
      prep = conn.prepareStatement(
              "CREATE TABLE IF NOT EXISTS recipes("
                      + "title TEXT PRIMARY KEY, "
                      + "description TEXT, "
                      + "ingredients TEXT, "
                      + "ingredients_detailed TEXT, "
                      + "chef TEXT, "
                      + "instructions TEXT, "
                      + "cooktime TEXT, "
                      + "preptime TEXT, "
                      + "photo TEXT, "
                      + "serves TEXT, "
                      + "url TEXT);");
      prep.execute();
      prep.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public static String getRecipesWithIngredient(String ingredient) {
    try {
      PreparedStatement prep = conn.prepareStatement("SELECT recipes FROM ingredientMap "
              + "WHERE ingredient IS ?");
      prep.setString(1, ingredient);
      ResultSet rs = prep.executeQuery();
      if (!rs.isBeforeFirst()) {
        return null;
      }
      String recipes = rs.getString(1);
      rs.close();
      prep.close();
      return recipes;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public static String getIngredientForRecipe(String recipeName) {
    try {
      PreparedStatement prep = conn.prepareStatement("SELECT ingredients FROM recipes "
              + "WHERE title IS ?");
      prep.setString(1, recipeName);
      ResultSet rs = prep.executeQuery();
      if (!rs.isBeforeFirst()) {
        return null;
      }
      String ingredients = rs.getString(1);
      return ingredients;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public static HashMap<String, Double> userRecipeRatings(String username) {
    try {
      PreparedStatement prep = conn.prepareStatement("SELECT ratedRecipes FROM users "
              + "WHERE name IS ?");
      prep.setString(1, username);
      ResultSet rs = prep.executeQuery();
      if (!rs.isBeforeFirst()) {
        return null;
      }
      HashMap<String, Double> ratingMap = new HashMap<>();
      String[] recipes = rs.getString(1).trim().split("\\s*,\\s*");
      for (String rec : recipes) {
        String[] tuple = rec.split(":");
        if (tuple.length != 2) {
          continue;
        }
        ratingMap.put(tuple[0], Double.parseDouble(tuple[1]));
      }
      rs.close();
      prep.close();
      return ratingMap;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public static HashMap<String, Double> userIngredientRatings(String username) {
    try {
      PreparedStatement prep = conn.prepareStatement("SELECT ratedIngredients FROM users "
              + "WHERE name IS ?");
      prep.setString(1, username);
      ResultSet rs = prep.executeQuery();
      if (!rs.isBeforeFirst()) {
        return null;
      }
      HashMap<String, Double> ratingMap = new HashMap<>();
      String[] ingredients = rs.getString(1).trim().split("\\s*,\\s*");
      for (String ing : ingredients) {
        String[] tuple = ing.split(":");
        if (tuple.length != 2) {
          continue;
        }
        ratingMap.put(tuple[0], Double.parseDouble(tuple[1]));
      }
      rs.close();
      prep.close();
      return ratingMap;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }
  public static Recipe getRecipeObject(String recipeName, User user) {
    try {
      PreparedStatement prep = conn.prepareStatement("SELECT * FROM recipes "
              + "WHERE title IS ?");
      prep.setString(1, recipeName);
      ResultSet rs = prep.executeQuery();
      if (!rs.isBeforeFirst()) {
        return null;
      }
      final int numParams = 12;
      ArrayList<String> params = new ArrayList<>() {
        {
          for (int i = 1; i < numParams; i++) {
            add(rs.getString(i));
          }
        }
      };
      rs.close();
      prep.close();
      return new Recipe(params, user);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }
  public static void addToRecipeDatabase(ArrayList<String> params)
          throws SQLException {
    try {
      if (conn != null) {
        PreparedStatement prep = conn.prepareStatement(
                "INSERT INTO recipes "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        for (int i = 0; i < params.size(); i++) {
          prep.setString(i + 1, params.get(i));
        }
        prep.executeUpdate();
        prep.close();
      }
    } catch (Exception e) {
      if (e.getMessage().contains("constraint violation")) {
        return;
      } else {
        System.out.println(e.getMessage());
      }
    }
  }

  public static void addUserToDatabase(String user) throws SQLException {
    if (conn != null) {
      PreparedStatement prep;
      prep = conn.prepareStatement(
              "INSERT INTO users VALUES (?,?,?,?);");
      prep.setString(1, user);
      prep.setString(2, "");
      prep.setString(3, "");
      prep.setString(4, "");
      prep.execute();
    }
  }


  public static void addUserIngredient(String user, String ingredient) throws SQLException {
    String currentInventory = getUserInventory(user);
    // if ingredient is at end
    String lastValue = currentInventory.substring(currentInventory.lastIndexOf(","));
    // if ingredient is at beginning
    String firstValue = currentInventory.substring(0, currentInventory.indexOf(","));


    if (!(currentInventory.contains("," + ingredient + ","))
            && !lastValue.equals(ingredient)
            && !firstValue.equals(ingredient)) {
      if (currentInventory.length() != 0) {
        currentInventory = currentInventory + ("," + ingredient);
      } else {
        currentInventory = ingredient;
      }
      try {
        if (conn != null) {
          PreparedStatement prep;
          prep = conn.prepareStatement(
                  "UPDATE users SET inventory = ? WHERE name = ?;");
          prep.setString(1, currentInventory);
          prep.setString(2, user);
          prep.execute();
          prep.close();
        }
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }

  public static void removeUserIngredient(String user, String ingredient) throws SQLException {
    String currentInventory = getUserInventory(user);
    // if ingredient is in middle
    currentInventory = currentInventory.replace("," + ingredient + ",", ",");

    // if ingredient is at end
    String lastValue = currentInventory.substring(currentInventory.lastIndexOf(","));
    if (lastValue.equals(ingredient)) {
      currentInventory = currentInventory.substring(0, currentInventory.lastIndexOf(","));
    }

    // if ingredient is at beginning
    String firstValue = currentInventory.substring(0, currentInventory.indexOf(","));
    if (firstValue.equals(ingredient)) {
      currentInventory = currentInventory.substring(currentInventory.indexOf(","));
    }

    if (currentInventory.startsWith(",")) {
      currentInventory = currentInventory.substring(1);
    }
    try {
      if (conn != null) {
        PreparedStatement prep;
        prep = conn.prepareStatement(
                "UPDATE users SET inventory = ? WHERE name = ?;");
        prep.setString(1, currentInventory);
        prep.setString(2, user);
        prep.execute();
        prep.close();
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println("SQL ERROR: Removing Ingredient");
    }
  }

  public static void addUserIngredientRating(User user,
                                             String ingredient, Double rating) throws SQLException {
    String currentRatings = getUserIngredientRatings(user.getName());

    if (currentRatings.contains(ingredient)) {
      currentRatings = currentRatings.replace(ingredient + ":"
              + user.getIngredientRatings().get(ingredient) + ",", "");
    }
    if (currentRatings.length() != 0) {
      currentRatings = currentRatings + ("," + ingredient + ":" + rating.toString());
    } else {
      currentRatings = ingredient + ":" + rating.toString();
    }
    try {
      if (conn != null) {
        PreparedStatement prep;
        prep = conn.prepareStatement(
                "UPDATE users SET ratedIngredients = ? WHERE name = ?;");
        prep.setString(1, currentRatings);
        prep.setString(2, user.getName());
        prep.execute();
        System.out.println("Connected?");
      }
    } catch (Exception e) {
      System.out.println("SQL ERROR");
    }
  }

  public static String getUserIngredientRatings(String user) throws SQLException {
    try {
      if (conn != null) {
        PreparedStatement prep;
        prep = conn.prepareStatement(
                "SELECT ratedIngredients FROM users WHERE name = ?;");
        prep.setString(1, user);
        ResultSet rs = prep.executeQuery();
        while (rs.next()) {
          String ratings = rs.getString(1);
          return ratings;
        }
      }
    } catch (Exception e) {
      System.out.println("SQL ERROR");
    }
    return "";
  }

  public static void addUserRecipeRating(User user,
                                         String recipe, Double rating) throws SQLException {
    String currentRatings = getUserRecipeRatings(user.getName());
    if (currentRatings.contains(recipe)) {
      currentRatings = currentRatings.replace(recipe + ":"
              + user.getRecipeRatings().get(recipe) + ",", "");
    }
    currentRatings = currentRatings + (recipe + ":" + rating.toString() + ",");

    //ToDO: update current ratings string and entry in table
    try {
      if (conn != null) {
        PreparedStatement prep;
        prep = conn.prepareStatement(
                "UPDATE users SET ratedRecipes = ? WHERE name = ?;");
        prep.setString(1, currentRatings);
        prep.setString(2, user.getName());
        prep.execute();
        System.out.println("Connected?");
      }
    } catch (Exception e) {
      System.out.println("SQL ERROR");
    }
  }

  public static String getUserRecipeRatings(String user) throws SQLException {
    try {
      if (conn != null) {
        PreparedStatement prep;
        prep = conn.prepareStatement(
                "SELECT ratedRecipes FROM users WHERE name = ?;");
        prep.setString(1, user);
        ResultSet rs = prep.executeQuery();
        while (rs.next()) {
          String ratings = rs.getString(1);
          return ratings;
        }
      }
    } catch (Exception e) {
      System.out.println("SQL ERROR");
    }
    return "";
  }

  public static String getUserInventory(String user) throws SQLException {
    if (conn != null) {
      PreparedStatement prep;
      prep = conn.prepareStatement(
              "SELECT inventory FROM users WHERE name = ?;");
      prep.setString(1, user);
      ResultSet rs = prep.executeQuery();
      String inventory = "";
      while (rs.next()) {
        inventory = rs.getString(1);
      }
      rs.close();
      prep.close();
      return inventory;
    }
    return "";
  }
}
