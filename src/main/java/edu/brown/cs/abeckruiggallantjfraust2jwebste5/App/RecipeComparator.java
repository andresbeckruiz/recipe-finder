package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.GraphVertex;
import java.util.Comparator;

public class RecipeComparator implements Comparator<GraphVertex> {
  @Override
  public int compare(GraphVertex e1, GraphVertex e2) {
    if (e1.getScore() > e2.getScore()) {
      return 1;
    }
    if (e1.getScore() < e2.getScore()) {
      return -1;
    }
    return 0;
  }
}