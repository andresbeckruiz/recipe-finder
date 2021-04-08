package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.GraphVertex;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.Recipe;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.RecipeGraph;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.SimilaritySearch.search;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.Database.initialize;

import java.sql.SQLException;
import java.util.ArrayList;

public class RecipeApp {

  private RecipeGraph recipeGraph;

  public RecipeApp() throws SQLException, ClassNotFoundException {
    // Create a Database and Load Tables
    initialize("filename");

    // Create Recipe Graph and Initialize
  }

  public ArrayList<GraphVertex> findSimilarRecipes(Recipe recipe) {
    return search(recipe, this.recipeGraph);
  }

}