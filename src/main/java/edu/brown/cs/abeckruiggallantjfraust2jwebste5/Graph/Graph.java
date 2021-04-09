package edu.brown.cs.abeckruiggallantjfraust2jwebste5.Graph;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.VertexComparator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class Graph<centralVertex extends Vertex, nonCentralVertex extends Vertex> {

  private HashMap<String, nonCentralVertex> nonCentralNodeMap = new HashMap<>();
  private HashMap<String, centralVertex> centralNodeMap = new HashMap<>();

  public Graph() {
  }

  public HashMap<String, nonCentralVertex> getNonCentralNodes() {
    return nonCentralNodeMap;
  }
  public HashMap<String, centralVertex> getCentralNodeMap() {
    return centralNodeMap;
  }

  private void addNonCentralHashSetToHashMap(HashSet<nonCentralVertex> set) {
    HashMap<String, nonCentralVertex> map = nonCentralNodeMap;
    for (nonCentralVertex vertex : set) {
      map.put(vertex.getName(), vertex);
    }
  }

  private void addCentralHashSetToHashMap(HashSet<centralVertex> set) {
    HashMap<String, centralVertex> map = centralNodeMap;
    for (centralVertex vertex : set) {
      map.put(vertex.getName(), vertex);
    }
  }

  //central = recipe non-central = ingredient
  public TreeMap<centralVertex, Double> search(centralVertex searchStart) {
    centralNodeMap.put(searchStart.getName(), searchStart);
    TreeMap<centralVertex, Double> mostSimilarContenders = new TreeMap<>(new VertexComparator());
    HashSet<nonCentralVertex> adjacentVertices = searchStart.getAdjacentVertices(nonCentralNodeMap);
    addNonCentralHashSetToHashMap(adjacentVertices);

    // get adjacent to adjacent vertices, and for each one compute similarity
    for (nonCentralVertex nonCentralAdj : adjacentVertices) {
      System.out.println("name: " + nonCentralAdj.getName());
      if (nonCentralAdj == null || nonCentralAdj.getName().equals("null")) {
        System.out.println("exiting");
        continue;
      }
      HashSet<centralVertex> adjToAdjVertices = nonCentralAdj.getAdjacentVertices(centralNodeMap);
      for (centralVertex doubleAdj : adjToAdjVertices) {
        if (doubleAdj == null || doubleAdj.equals("null")
                || searchStart.getName().contains(doubleAdj.getName())) {
          continue;
        }
        double similarity = computeSimilarity(doubleAdj, adjacentVertices);
        if (doubleAdj.getSimilarityScore() <= similarity) {
          doubleAdj.setSimilarityScore(similarity);
          mostSimilarContenders.put(doubleAdj, similarity);
        }
      }
      addCentralHashSetToHashMap(adjToAdjVertices);
    }

    return mostSimilarContenders;
  }

  public double computeSimilarity(centralVertex adjToAdj,
                                  HashSet<nonCentralVertex> setTwo) {
    HashSet<nonCentralVertex> setOne = adjToAdj.getAdjacentVertices(nonCentralNodeMap);
    addNonCentralHashSetToHashMap(setOne);
    int initialSetOneSize = setOne.size();
    addNonCentralHashSetToHashMap(setTwo);
    setOne.retainAll(setTwo);
    int intersectionSize = setOne.size();
    int totalNumSharedAdjNodes = initialSetOneSize + setTwo.size();
    return adjToAdj.getValue() * (double) intersectionSize
            / (double) totalNumSharedAdjNodes;
  }
}
