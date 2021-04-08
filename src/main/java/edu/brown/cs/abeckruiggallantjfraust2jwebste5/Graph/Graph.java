package edu.brown.cs.abeckruiggallantjfraust2jwebste5.Graph;

import edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.VertexComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Graph {

  private HashMap<String, Vertex> nonCentralNodeMap = new HashMap<>();
  private HashMap<String, Vertex> centralNodeMap = new HashMap<>();

  public Graph() {
  }

  public HashMap<String, Vertex> getNonCentralNodes() {
    return nonCentralNodeMap;
  }
  public HashMap<String, Vertex> getCentralNodeMap() {
    return centralNodeMap;
  }

  private void addHashSetToHashMap(HashSet<Vertex> set, boolean central) {
    HashMap<String, Vertex> map = nonCentralNodeMap;
    if (central) {
      map = centralNodeMap;
    }
    for (Vertex v : set) {
      map.put(v.getName(), v);
    }
  }
  //central = recipe non-central = ingredient
  public List<Vertex> search(Vertex searchStart) {
    centralNodeMap.put(searchStart.getName(), searchStart);
    HashSet<Vertex> mostSimilarContenders = new HashSet<>();
    HashSet<Vertex> adjacentVertices = searchStart.getAdjacentVertices(nonCentralNodeMap);
    addHashSetToHashMap(adjacentVertices, false);

    System.out.println(adjacentVertices.size());
    // get adjacent to adjacent vertices, and for each one compute similarity
    for (Vertex adj : adjacentVertices) {
      System.out.println("name: " + adj.getName());
      if (adj == null || adj.getName().equals("null")) {
        System.out.println("exiting");
        continue;
      }
      HashSet<Vertex> adjToAdjVertices = adj.getAdjacentVertices(centralNodeMap);
      addHashSetToHashMap(adjToAdjVertices, true);
      for (Vertex doubleAdj : adjToAdjVertices) {

        if (doubleAdj == null || doubleAdj.equals("null")
                || searchStart.getName().contains(doubleAdj.getName())) {
          continue;
        }
        double similarity = computeSimilarity(doubleAdj, searchStart);
        if (doubleAdj.getSimilarityScore() < similarity) {
          doubleAdj.setSimilarityScore(similarity);
          mostSimilarContenders.add(doubleAdj);
        }
      }
    }

    List<Vertex> sortedContenders = new ArrayList<Vertex>(mostSimilarContenders);
    sortedContenders.sort(new VertexComparator());
    return sortedContenders;
  }

  public double computeSimilarity(Vertex adjToAdj, Vertex searchStart) {
    HashSet<Vertex> setOne = adjToAdj.getAdjacentVertices(nonCentralNodeMap);
    addHashSetToHashMap(setOne, false);
    int initialSetOneSize = setOne.size();
    HashSet<Vertex> setTwo = searchStart.getAdjacentVertices(nonCentralNodeMap);
    addHashSetToHashMap(setTwo, false);
    setOne.retainAll(setTwo);
    int intersectionSize = setOne.size();
    int totalNumSharedAdjNodes = initialSetOneSize + setTwo.size();
    return (double) intersectionSize / (double) totalNumSharedAdjNodes;
  }
}
