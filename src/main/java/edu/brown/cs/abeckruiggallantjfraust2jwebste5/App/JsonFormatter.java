package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;

import java.util.HashMap;
import java.util.Map;

public final class JsonFormatter {
  private  JsonFormatter() {
  }

  public static Map<String, String> ratingMapToJson(String ratings) {
    Map<String, String> ratingMap = new HashMap<>();
    String[] parsed = ratings.trim().split(",");
    if (parsed.length != 1) {
      for (String parse : parsed) {
        String[] rating = parse.trim().split(":");
        if (rating.length != 1) {
          ratingMap.put(rating[0], rating[1]);
        }
      }
    }
    System.out.println(ratingMap);
    return ratingMap;
  }
}
