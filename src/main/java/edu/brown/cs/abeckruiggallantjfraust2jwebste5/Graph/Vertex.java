package edu.brown.cs.abeckruiggallantjfraust2jwebste5.Graph;

import java.util.HashMap;
import java.util.HashSet;

public interface Vertex<V extends Vertex> {
  HashSet<V> getAdjacentVertices(HashMap<String, V> nodesAlreadyAdded);
  String getName();
  Double getValue();
  void setValue(double value);
  void setSimilarityScore(double score);
  double getSimilarityScore();
}
