package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;

import java.util.Comparator;
import java.util.HashMap;

public final class NumIngredientsComparator implements Comparator<String> {
  private HashMap<String, Integer> numIngredientsMap;
  public NumIngredientsComparator(HashMap<String, Integer> map) {
    numIngredientsMap = map;
  }
  @Override
  public int compare(String r1, String r2) {
    int r1NumIngredients = numIngredientsMap.get(r1);
    int r2NumIngredients = numIngredientsMap.get(r2);
    if (r1NumIngredients > r2NumIngredients) {
      return 1;
    }
    if (r1NumIngredients < r2NumIngredients) {
      return -1;
    }
    return 0;
  }
}
