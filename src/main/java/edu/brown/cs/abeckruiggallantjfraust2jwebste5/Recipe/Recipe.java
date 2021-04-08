package edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe;

import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Graph.Vertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

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
  private double similarityScore = 0;

  public Recipe(ArrayList<String> params) {
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
  }

  public String getIngredientsDetailed() {
    return ingredientsDetailed;
  }

  public String getName() {
    return title;
  }

  @Override
  public HashSet<Ingredient> getAdjacentVertices(HashMap<String, Ingredient> ingredientsAlreadyAdded) {
    if (adjIngredients.size() == 0) {
      String ingredientString = Database.getIngredientForRecipe(title);
      String[] ingredientArray = ingredientString.trim().split("\\s*,\\s*");
      for (String ingredientName : ingredientArray) {
        if (ingredientsAlreadyAdded.containsKey(ingredientName)) {
          adjIngredients.add(ingredientsAlreadyAdded.get(ingredientName));
        } else {
          adjIngredients.add(new Ingredient(ingredientName));
        }
      }
    }

    return adjIngredients;
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

  public Double getScore() {
    return rating;
  }

  public void setRating(double newRating) {
    this.rating = newRating;
  }


}
