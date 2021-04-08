package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;

import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.Recipe;
import org.checkerframework.checker.units.qual.A;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
                      + "ingredient TEXT, "
                      + "recipes TEXT);");
      prep.execute();
      prep.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public static void addIngredient(String ingredient, String recipes) throws SQLException {
    if (conn != null) {
      PreparedStatement prep = conn.prepareStatement(
              "INSERT INTO ingredientMap "
                      + "VALUES (?, ?)");
      prep.setString(1, ingredient);
      prep.setString(2, recipes);
      prep.execute();
      prep.close();
    }
  }
  public static void createRecipeDatabase() throws SQLException {
    try {
      PreparedStatement prep = conn.prepareStatement("DROP TABLE IF EXISTS recipes");
      prep.execute();
      prep = conn.prepareStatement(
              "CREATE TABLE IF NOT EXISTS recipes("
                      + "title TEXT, "
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
      String recipes = rs.getString(1);
      rs.close();
      prep.close();
      return recipes;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public static Recipe getRecipe(String recipeName) {
    try {
      PreparedStatement prep = conn.prepareStatement("SELECT * FROM recipes "
              + "WHERE title IS ?");
      prep.setString(1, recipeName);
      ResultSet rs = prep.executeQuery();
      ArrayList<String> params = new ArrayList<>() {
        {
          for(int i = 1; i < 12; i++) {
            add(rs.getString(i));
          }
        }
      };
      rs.close();
      prep.close();
      return new Recipe(params);
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
      System.out.println(e.getMessage());
    }
  }
}
