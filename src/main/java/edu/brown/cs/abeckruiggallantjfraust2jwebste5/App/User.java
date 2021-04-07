package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;

import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.GraphVertex;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.Ingredient;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.RecipeGraph;

import java.util.ArrayList;

public class User {

    private ArrayList<String> ingredients;
    private ArrayList<GraphVertex> localVertices;
    private String name;

    public User(String username) {
      this.name = username;
    }

    public void loadUserGraph(RecipeGraph graph) {
      for (String ing: ingredients) {
        Ingredient ingr = graph.getIngredientObjects().get(ing);


      }
    }
}

