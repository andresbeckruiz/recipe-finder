package edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe;

import java.util.ArrayList;

public class Recipe {

  private GraphVertex vertex;
  private String title;
  private String description;
  private String ingredients;
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
    this.ingredients = params.get(2);
    this.chef = params.get(3);
    this.instructions = params.get(4);
    this.cookingTime = params.get(5);
    this.prepTime = params.get(6);
    this.photourl = params.get(7);
    this.serves = params.get(8);
    this.url = params.get(9);
    this.vertex = new GraphVertex();
  }

  public GraphVertex getVertex() {
    return vertex;
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

  public String getIngredients() {
    return ingredients;
  }

  public void setIngredients(String ingredients) {
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

  public void addIngredient(Ingredient ingredient) {
    vertex.getEdges().add(new GraphEdge(ingredient, this.vertex));
  }
}