package edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data;

import edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDatabase {

  private static Connection conn;

  public UserDatabase(Connection conn) {
    this.conn = conn;
  }

  public static void createUserDatabase() throws SQLException {
    try {
      PreparedStatement prep = conn.prepareStatement("DROP TABLE IF EXISTS users");
      prep.execute();
      prep = conn.prepareStatement(
              "CREATE TABLE IF NOT EXISTS users("
                      + "name TEXT, "
                      + "email TEXT PRIMARY KEY,"
                      + "ratedRecipes TEXT,"
                      + "ratedIngredients TEXT,"
                      + "inventory);");
      prep.execute();
      prep.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public static void addUserToDatabase(String name, String email) throws SQLException {
    if (conn != null) {
      System.out.println("ADDING");
      PreparedStatement prep;
      prep = conn.prepareStatement(
              "INSERT INTO users VALUES (?,?,?,?,?);");
      prep.setString(1, name);
      prep.setString(2, email);
      prep.setString(3, "");
      prep.setString(4, "");
      prep.setString(5, "");
      prep.execute();
    }
  }

  public static void addUserIngredient(String user, String ingredient) throws SQLException {
    String currentInventory = getUserInventory(user);
    String lastValue = "";
    String firstValue = "";
    if (currentInventory.lastIndexOf(",") != -1) {
      // if ingredient is at end
      lastValue = currentInventory.substring(currentInventory.lastIndexOf(","));
    }
    if (currentInventory.indexOf(",") != -1) {
      // if ingredient is at beginning
      firstValue = currentInventory.substring(0, currentInventory.indexOf(","));
    }

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
                  "UPDATE users SET inventory = ? WHERE email = ?;");
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

    currentInventory = currentInventory.replace(ingredient, "");

    if (currentInventory.startsWith(",")) {
      currentInventory = currentInventory.substring(1);
    }

    try {
      if (conn != null) {
        PreparedStatement prep;
        prep = conn.prepareStatement(
                "UPDATE users SET inventory = ? WHERE email = ?;");
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

      currentRatings = currentRatings.replace("," + ingredient + ":"
              + user.getIngredientRatings().get(ingredient), "");

      currentRatings = currentRatings.replace(ingredient + ":"
              + user.getIngredientRatings().get(ingredient), "");
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
                "UPDATE users SET ratedIngredients = ? WHERE email = ?;");
        prep.setString(1, currentRatings);
        prep.setString(2, user.getName());
        prep.execute();
        prep.close();
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
                "SELECT ratedIngredients FROM users WHERE email = ?;");
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

      currentRatings = currentRatings.replace("," + recipe + ":"
              + user.getRecipeRatings().get(recipe), "");

      currentRatings = currentRatings.replace(recipe + ":"
              + user.getRecipeRatings().get(recipe), "");
    }
    if (currentRatings.length() != 0) {
      currentRatings = currentRatings + ("," + recipe + ":" + rating.toString());
    } else {
      currentRatings = recipe + ":" + rating.toString();
    }

    //ToDO: update current ratings string and entry in table
    try {
      if (conn != null) {
        PreparedStatement prep;
        prep = conn.prepareStatement(
                "UPDATE users SET ratedRecipes = ? WHERE email = ?;");
        prep.setString(1, currentRatings);
        prep.setString(2, user.getName());
        prep.execute();
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
                "SELECT ratedRecipes FROM users WHERE email = ?;");
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
              "SELECT inventory FROM users WHERE email = ?;");
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

  public static String getName(String email) throws SQLException {
    if (conn != null) {
      PreparedStatement prep;
      prep = conn.prepareStatement(
              "SELECT name FROM users WHERE email = ?;");
      prep.setString(1, email);
      ResultSet rs = prep.executeQuery();
      String name = "";
      while (rs.next()) {
        name = rs.getString(1);
      }
      rs.close();
      prep.close();
      return name;
    }
    return "";
  }

  public static void deleteUser(String email) throws SQLException {
    if (conn != null) {
      PreparedStatement prep;
      prep = conn.prepareStatement(
              "DELETE FROM users WHERE email = ?;");
      prep.setString(1, email);
      prep.execute();
      prep.close();
    }
  }
}
