package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.GraphVertex;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.Recipe;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.RecipeGraph;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.SimilaritySearch.search;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.cs0320.Database.initializeDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

public class RecipeApp {

  private RecipeGraph recipeGraph;

  public RecipeApp() throws SQLException, ClassNotFoundException {
    // Create a Database and Load Tables
    initializeDatabase("filename");

    // Create Recipe Graph and Initialize
    recipeGraph = new RecipeGraph();
    recipeGraph.initializeGraph();
  }

  public ArrayList<GraphVertex> findSimilarRecipes(Recipe recipe) {
    return search(recipe, this.recipeGraph);
  }

}