package edu.brown.cs.abeckruiggallantjfraust2jwebste5.cs0320;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.cs0320.Database.createRecipeDatabase;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.cs0320.Database.initialize;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.cs0320.Database.createIngredientDatabase;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.cs0320.Database.addToRecipeDatabase;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.cs0320.Database.addIngredient;

public final class JsonToSql {

  private JsonToSql() { }
  public static void parseJson() throws FileNotFoundException, JSONException {
    ListMultimap<String, Object> ingredientSet = ArrayListMultimap.create();
    try {
      initialize("data/newdb.sqlite3");
      createRecipeDatabase();
      createIngredientDatabase();
      JSONParser parser = new JSONParser();
      BufferedReader bufferedReader = new BufferedReader(new FileReader("data/recipes2.json"));
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        JSONObject jsonObject = (JSONObject) parser.parse(line);
        ArrayList<String> parameters = new ArrayList<>();

        //parsing json fields to send to sql database
        String title = jsonObject.get("title").toString().trim().replace(",", "");
        title = title.toLowerCase();
        parameters.add(title);
        parameters.add(jsonObject.get("description").toString());
        parameters.add(jsonObject.get("ingredients").toString());
        parameters.add(jsonObject.get("chef").toString());
        parameters.add(jsonObject.get("instructions").toString());
        parameters.add(jsonObject.get("cooking_time_minutes").toString());
        parameters.add(jsonObject.get("preparation_time_minutes").toString());
        parameters.add(jsonObject.get("photo_url") == null ? null
                : jsonObject.get("photo_url").toString());
        parameters.add(jsonObject.get("serves").toString());
        parameters.add(jsonObject.get("url").toString());
        JSONArray ingredientList = (JSONArray) jsonObject.get("instructions_detailed");

        String ingredients = "";
        //cycle through ingredient list
        for (int i = 0; i < ingredientList.size(); i++) {
          JSONObject ingredientObj = (JSONObject) ingredientList.get(i);
          String ingredient = ingredientObj.get("ingredient") == null ? null
                  : ingredientObj.get("ingredient").toString().toLowerCase();
          if (!ingredientSet.containsEntry(ingredient, title)) {
            ingredientSet.put(ingredient, title);
            ingredients += ingredient + ",";
          }
        }
        if (ingredients.length() > 0) {
          ingredients = ingredients.substring(0, ingredients.length()-1);
        } else {
          ingredients = "";
        }
        parameters.add(2, ingredients);
        //add to recipe database
        addToRecipeDatabase(parameters);
      }
      //add to ingredient database
      for (String ingredient: ingredientSet.keySet()) {
        String value = ingredientSet.get(ingredient).toString();
        value = value.strip();
        value = value.substring(1, value.length() - 1);
        addIngredient(ingredient, value);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
