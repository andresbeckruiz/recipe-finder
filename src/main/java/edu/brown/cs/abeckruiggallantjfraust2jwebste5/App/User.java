package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;

import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.GraphVertex;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.Ingredient;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.RecipeGraph;

import java.util.ArrayList;

public class User {

    private ArrayList<String> ingredients;
    private ArrayList<GraphVertex> localVertices;
    private String name;
    private RecipeGraph recipeGraph;

    public User(String username) {
      this.name = username;
      this.recipeGraph = new RecipeGraph();
      this.loadUserGraph(recipeGraph);
    }

    public void loadUserGraph(RecipeGraph graph) {
      graph.initializeGraph(this);
//      for (String ing: ingredients) {
//        Ingredient ingredient = graph.getIngredientObjects().get(ing);
//        ingredients.add(ingredient);
//      }
    }

    public ArrayList<String> getIngredients() {return ingredients;}
}

