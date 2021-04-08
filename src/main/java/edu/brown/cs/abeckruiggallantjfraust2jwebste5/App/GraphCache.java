package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.GraphEdge;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.GraphVertex;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * The MapCache creates a cache that stores Recipe Graph Nodes with their
 * corresponding outgoing edges.
 * This reduces queries when doing similarity search.
 */
public class GraphCache {
  //The cache has a maximum capacity of 100000.
  private static final int SIZE = 100000;
  private static final int DURATION = 30;
  private final LoadingCache<GraphVertex, ArrayList<GraphEdge>> cache;

  /**
   * The constructor creates a new cache.
   * The CacheLoader's load method will return a key's corresponding value.
   */
  public GraphCache() {
    cache = CacheBuilder.newBuilder()
            .maximumSize(SIZE)
            .expireAfterWrite(DURATION, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
              @Override
              public ArrayList<GraphEdge> load(GraphVertex key) {
                return key.getEdges();
              }
            });
  }

  /**
   * This method accesses the cache.
   * @return cache.
   */
  public LoadingCache<GraphVertex, ArrayList<GraphEdge>> access() {
    return cache;
  }
}
