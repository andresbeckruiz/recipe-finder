package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.GraphVertex;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.Recipe;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.RecipeGraph;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.GraphEdge;
import java.util.ArrayList;

public class SimilaritySearch {

  public static ArrayList<GraphVertex> search(Recipe recipe, RecipeGraph searchGraph) {
    ArrayList<GraphVertex> verticesCopy = searchGraph.getGraphVertices();
    GraphVertex searchVertex = recipe.getVertex();

    for (int i = 0; i < verticesCopy.size(); i++) {
      // For each vertex, get outgoing edges and compare with recipe outgoing edges
      GraphVertex localVertex = verticesCopy.get(i);
      double similarity = computeSimilarity(searchVertex.getEdges(), localVertex.getEdges());
      localVertex.setScore(similarity);
      //Additional Feature: each user has hashmap of strings to ratings, get rating and set score to weighted average
    }

    verticesCopy.sort(new RecipeComparator());

    return verticesCopy;
  }

  public static double computeSimilarity(ArrayList<GraphEdge> searchEdges, ArrayList<GraphEdge> localEdges) {
    double similarity = 0;
    for (int i = 0; i < localEdges.size(); i++) {
      for (int j = 0; j < searchEdges.size(); j++) {
        if (localEdges.get(i).getIngredient().equals(searchEdges.get(i).getIngredient())) {
          similarity++;
        }
      }
    }
    return similarity/searchEdges.size();
  }
}

