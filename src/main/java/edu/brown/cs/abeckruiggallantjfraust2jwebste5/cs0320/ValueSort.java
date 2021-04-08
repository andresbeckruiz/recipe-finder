package edu.brown.cs.abeckruiggallantjfraust2jwebste5.cs0320;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public final class ValueSort {

  public static <K, V extends Comparable<V>> TreeMap<K, V> sort(final Map<K, V> map)
  {
    Comparator<K> comparator = new Comparator<K>() {
      public int compare(K key1, K key2)
      {
        int compare = map.get(key1).compareTo(map.get(key2));
        if (compare == 0) {
          return 1;
        }
        else {
          return compare;
        }
      }
    };
    TreeMap<K, V> sortedMap = new TreeMap<K, V>(comparator);
    sortedMap.putAll(map);
    return sortedMap;
  }
}

