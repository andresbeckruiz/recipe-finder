package edu.brown.cs.abeckruiggallantjfraust2jwebste5.cs0320;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Recipe {
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

  public Recipe(ArrayList<String> params) {
    this.title = params.get(0);
    this.description = params.get(1);
    String ingredients[] = params.get(2).trim().split("\\s*,\\s*");
    this.ingredients =  new HashSet(Arrays.asList(ingredients));
    this.ingredientsDetailed = params.get(3);
    this.chef = params.get(4);
    this.instructions = params.get(5);
    this.cookingTime = params.get(6);
    this.prepTime = params.get(7);
    this.photourl = params.get(8);
    this.serves = params.get(9);
    this.url = params.get(10);
  }

  public String getIngredientsDetailed() {
    return ingredientsDetailed;
  }

  public void setIngredientsDetailed(String ingredientsDetailed) {
    this.ingredientsDetailed = ingredientsDetailed;
  }

  public String getTitle() {
    return title;
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
}
