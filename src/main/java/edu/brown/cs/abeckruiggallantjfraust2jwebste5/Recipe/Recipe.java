package edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe;

import com.google.common.collect.ImmutableMap;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.User;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Graph.Vertex;

import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.ConstantHyperparameters.DEFAULT_RATING;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.ConstantHyperparameters.TOTAL_RATING;

public class Recipe implements Vertex<Ingredient> {

  private HashSet<Ingredient> adjIngredients = new HashSet<>();
  private String title;
  private String description;
  private HashSet<String> ingredients;
  private String ingredientsDetailed;
  private String chef;
  private String instructions;
  private String cookingTime;
  private String prepTime;
  private String photourl;
  private String serves;
  private String url;
  private Double rating;
  private boolean ratingSet = false;
  private double similarityScore = 0;
  private User owner;

  public Recipe(ArrayList<String> params, User user) {
    final int magicNum7 = 7;
    final int magicNum8 = 8;
    final int magicNum9 = 9;
    final int magicNum10 = 10;
    this.title = params.get(0);
    this.description = params.get(1);
    String[] ingredientArr = params.get(2).trim().split("\\s*,\\s*");
    this.ingredients =  new HashSet(Arrays.asList(ingredientArr));
    this.ingredientsDetailed = params.get(3);
    this.chef = params.get(4);
    this.instructions = params.get(5);
    this.cookingTime = params.get(6);
    this.prepTime = params.get(magicNum7);
    this.photourl = params.get(magicNum8);
    this.serves = params.get(magicNum9);
    this.url = params.get(magicNum10);
    this.owner = user;
    if (user.getRecipeRatings().keySet().contains(title)) {
      ratingSet = true;
      this.rating = user.getRecipeRatings().get(title);
    } else {
      this.rating = DEFAULT_RATING;
    }
  }

  public String getIngredientsDetailed() {
    return ingredientsDetailed;
  }

  public String getName() {
    return title;
  }

  @Override
  public HashSet<Ingredient> getAdjacentVertices(
          HashMap<String, Ingredient> ingredientsAlreadyAdded) {
    if (this.getName().equals("gin and lime truffles")) {
      System.out.println("here");
    }
    double newSim = 0;
    int numIngredients = 0;
    if (adjIngredients.size() == 0) {
      String ingredientString = Database.getIngredientForRecipe(title);
      String[] ingredientArray = ingredientString.trim().split("\\s*,\\s*");
      numIngredients = ingredientArray.length;
      for (String ingredientName : ingredientArray) {
        Ingredient ing;
        if (ingredientsAlreadyAdded.containsKey(ingredientName)) {
          ing = ingredientsAlreadyAdded.get(ingredientName);
        } else {
          ing = new Ingredient(ingredientName, this.owner);
        }
        newSim += ing.getValue() * TOTAL_RATING;
        adjIngredients.add(ing);
      }
      if (!ratingSet) {
        this.rating = newSim / (double) (TOTAL_RATING * numIngredients);
      }
    }

    HashSet<Ingredient> adjIngredientsToReturn = new HashSet<>();
    adjIngredientsToReturn.addAll(adjIngredients);
    return adjIngredientsToReturn;
  }

  @Override
  public void setSimilarityScore(double score) {
    similarityScore = score;
  }

  @Override
  public double getSimilarityScore() {
    return similarityScore;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public HashSet<String> getIngredients() {
    return ingredients;
  }

  public void setIngredients(HashSet<String> ingredients) {
    this.ingredients = ingredients;
  }

  public String getChef() {
    return chef;
  }

  public void setChef(String chef) {
    this.chef = chef;
  }

  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  public String getCookingTime() {
    return cookingTime;
  }

  public void setCookingTime(String cookingTime) {
    this.cookingTime = cookingTime;
  }

  public String getPrepTime() {
    return prepTime;
  }

  public void setPrepTime(String prepTime) {
    this.prepTime = prepTime;
  }

  public String getPhotourl() {
    return photourl;
  }

  public void setPhotourl(String photourl) {
    this.photourl = photourl;
  }

  public String getServes() {
    return serves;
  }

  public void setServes(String serves) {
    this.serves = serves;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return "  -" + title + " : " + url;
  }

  public ImmutableMap toSmallMap() {
    Map<String, String> mutableMap = new HashMap<>();
    mutableMap.put("recipeName", this.title);
    if (this.photourl == null) {
      mutableMap.put("src", "https://assets.bonappetit.com/photos/5a8749c98e5ab504d767b208/16:9/w_2048,c_limit/no-fail-roast-chicken-with-lemon-and-garlic.jpg");
    } else {
      mutableMap.put("src", this.photourl);
    }
    mutableMap.put("chef", this.chef);
    ImmutableMap<String, String> immutableMap = ImmutableMap.<String, String>builder()
            .putAll(mutableMap)
            .build();
    return immutableMap;
  }

  public ImmutableMap toBigMap() {
    Map<String, String> map = new HashMap<>() {{
        put("title", checkForNull(title));
        put("description", checkForNull(description));
        put("instructions", checkForNull(instructions));
        put("url", checkForNull(url));
        put("chefName", checkForNull(chef));
      }
    };
    HashSet<String> recipeIngredients = ingredients;
    int ingredeintNum = 0;
    for (String ingredient : recipeIngredients) {
      map.put("ingredient" + ingredeintNum, ingredient);
      ingredeintNum++;
    }
    ImmutableMap<String, String> immutableMap = ImmutableMap.<String, String>builder()
            .putAll(map)
            .build();
    return immutableMap;
  }
  @Override
  public Double getValue() {
    return this.rating;
  }

  @Override
  public void setValue(double value) {
    this.rating = value;
  }

  private String checkForNull(String toCheck) {
    if (toCheck != null) {
      return toCheck;
    } else {
      return "";
    }
  }
}
