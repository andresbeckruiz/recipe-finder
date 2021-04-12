package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Arrays;

import com.google.common.collect.ImmutableMap;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.Recipe;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.json.JSONObject;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;
import com.google.gson.Gson;
import freemarker.template.Configuration;

import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database.*;

//import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.JsonToSql.parseJson;

/**
 * The Main class of our project. This is where execution begins.
 */

public final class Main {
  private static final int DEFAULT_PORT = 4567;
  private static final Gson GSON = new Gson();

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private final String[] args;
  private RecipeApp recipeApp;

  private Main(String[] args) {
    this.args = args;
    this.recipeApp = new RecipeApp();
  }

  /**
   * This method runs the program, creating a new REPL
   * to take in user input.
   */
  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
            .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    System.out.println("Running");
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("Message: Unable use %s for template loading.%n",
              templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());
    FreeMarkerEngine freeMarker = createEngine();
    // Setup Spark Route
    Spark.options("/*", (request, response) -> {
      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }
      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }
      return "OK";
    });

    Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    Spark.exception(Exception.class, new ExceptionPrinter());

    Spark.post("/enter-ingredient", new AddIngredientHandler());
    Spark.post("/rate-ingredient", new RateIngredientHandler());
    Spark.post("/delete-ingredient", new DeleteIngredientHandler());

    Spark.post("/find-suggestions", new FindRecipeSuggestionsHandler());
    Spark.post("/recipe", new FindSimilarRecipesHandler());
    Spark.post("/rate-recipe", new RateRecipeHandler());

    Spark.post("/newUser", new CreateNewUserHandler());
    Spark.post("/newUserSignup", new CreateNewUserHandlerSignup());
    Spark.post("/inventory", new GetUserInventory());
  }

  /**
   * Display an Message page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

  private class AddIngredientHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String ingredientName = data.getString("ingredient");
      recipeApp.getCurUser().addIngredient(ingredientName);
      Map<String, Object> variables = ImmutableMap.of("rating",
              recipeApp.getCurUser().getIngredientRatings().get(ingredientName));
      return GSON.toJson(variables);
    }
  }

  private class RateIngredientHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String ingredientName = data.getString("ingredient");
      Double ingredientRating = data.getDouble("rating");
      recipeApp.getCurUser().addIngredientRating(ingredientName, ingredientRating);
      return null;
    }
  }

  private class DeleteIngredientHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      System.out.println(data);
      String ingredientName = data.getString("ingredient");
      recipeApp.getCurUser().removeIngredient(ingredientName);
      return "";
    }
  }

  /**
   * Handles finding initial recipe suggestions.
   */
  private class FindRecipeSuggestionsHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      ArrayList<Recipe> recipeSuggestions = recipeApp.getCurUser().cook();
      // ToDO: edit cook() to return correct info for front end
      Map<String, Object> variables = ImmutableMap.of("firstSuggestion",
              recipeSuggestions.get(0).toJson(),
              "secondSuggestion", recipeSuggestions.get(1).toJson(),
              "thirdSuggestion", recipeSuggestions.get(2).toJson());
      return GSON.toJson(variables);
    }
  }

  /**
   * Handles finding similar recipes when prompted in front end.
   */
  private class FindSimilarRecipesHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String currentRecipeName = data.getString("recipe");

      // ToDo: create method to get all info about "CurrentRecipeName"
      Recipe curRecipe = getRecipeObject(currentRecipeName, recipeApp.getCurUser());
      Map<String, Object> recipeVariables = ImmutableMap.of("title", curRecipe.getName(),
              "description,", curRecipe.getDescription(), "instructions",
              curRecipe.getInstructions(), "url", curRecipe.getUrl(), "chefName",
              curRecipe.getChef());

      for (int i = 0; i < curRecipe.getIngredients().size(); i++) {
        recipeVariables.put("ingredient" + i, curRecipe.getIngredients().toArray()[i]);
      }

      // ToDo: change FindSimilarRecipes to return correct info

      //TreeMap<Recipe, Double> map = recipeApp.getCurUser().findSimilarRecipes(currentRecipeName);

      Map<String, Object> variables = ImmutableMap.of("recipeInformation", recipeVariables);
      return GSON.toJson(recipeVariables);
    }
  }

  private class RateRecipeHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String recipeName = data.getString("recipe");
      Double recipeRating = data.getDouble("rating");
      recipeApp.getCurUser().addRecipeRating(recipeName, recipeRating);
      return null;
    }
  }

  /**
   * Handles creating a new user object and updating the currentUser object.
   */
  private class CreateNewUserHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String username = data.getString("name");

      System.out.println("Username" + username);
      try {
        //get inventory
        String inventoryString = getUserInventory(username);
        //splitting to create hashset for user ingredients
        String[] inventoryArr = inventoryString.split(",");
        HashSet<String> ingredients = new HashSet<>(Arrays.asList(inventoryArr));
        //get rating
        String ratingString = getUserIngredientRatings(username);
        System.out.println("rating string: " + ratingString);
        //splitting to create hashset for user ingredients
        String[] ratingArr = ratingString.split(",");
        System.out.println(" rating arr: " + ratingArr[0] + " : " + ratingArr[1]);
        User newUser = new User(username, ingredients);
        for (String review : ratingArr) {
          String[] rating = review.split(":");
          System.out.println("RATING: " + rating[0] + " : " + rating[1]);
          newUser.addIngredientRating(rating[0], Double.parseDouble(rating[1]));
        }
        recipeApp.setCurUser(newUser);
      } catch (SQLException e) {
        System.err.println("ERROR: Error connecting to database");
        return "error";
      }
      return "";
    }
  }

  /**
   * Front end handler for creating new user off of signup.
   */
  private class CreateNewUserHandlerSignup implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String username = data.getString("name");
      System.out.println("Username" + username);
      try {
        addUserToDatabase(username);
        HashSet<String> ingredients = new HashSet<>();
        User newUser = new User(username, ingredients);
        recipeApp.setCurUser(newUser);
        return "";
      } catch (SQLException e) {
        System.err.println("ERROR: Error connecting to database");
        return "error";
      }
    }
  }

  /**
   * Front end handler for returning user inventory when Fridge loads
   */
  private class GetUserInventory implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String username = data.getString("name");
      try {
        String string = getUserInventory(username);
        //splitting to create hashset for user ingredients
        String[] values = string.split(",");
        HashSet<String> ingredients = new HashSet<>(Arrays.asList(values));
        Map<String, Object> map = ImmutableMap.of("inventory", ingredients);
        System.out.println("user inventory working!");
        return GSON.toJson(map);
      } catch (SQLException e) {
        System.err.println("ERROR: Error connecting to database");
        return "error";
      }
    }
  }
}

