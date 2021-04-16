package edu.brown.cs.abeckruiggallantjfraust2jwebste5.DataPreprocess;

import java.util.HashMap;
import java.util.Map;

public final class JsonFormatter {
  private  JsonFormatter() {
  }

  public static Map<String, String> ratingMapToJson(String ratings) {
    Map<String, String> ratingMap = new HashMap<>();
    String[] parsed = ratings.trim().split(",");
    if (parsed.length > 0) {
      for (String parse : parsed) {
        String[] rating = parse.trim().split(":");
        if (rating.length > 0) {
          ratingMap.put(rating[0], rating[1]);
        }
      }
    }
    return ratingMap;
  }
}
