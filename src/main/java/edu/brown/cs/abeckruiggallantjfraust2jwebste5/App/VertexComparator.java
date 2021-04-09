package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Graph.Vertex;
import java.util.Comparator;

public class VertexComparator implements Comparator<Vertex> {
  @Override
  public int compare(Vertex e1, Vertex e2) {
    if (e1.getSimilarityScore() < e2.getSimilarityScore()) {
      return 1;
    }
    if (e1.getSimilarityScore() > e2.getSimilarityScore()) {
      return -1;
    }
    return 0;
  }
}
