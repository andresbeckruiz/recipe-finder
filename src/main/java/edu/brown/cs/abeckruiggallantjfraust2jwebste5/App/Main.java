package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.*;

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

import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.App.ConstantHyperparameters.DEFAULT_RATING;
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
      return "";
    }
  }

  private class DeleteIngredientHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
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
              recipeSuggestions.get(0).toSmallMap(),
              "secondSuggestion", recipeSuggestions.get(1).toSmallMap(),
              "thirdSuggestion", recipeSuggestions.get(2).toSmallMap());
      String json = GSON.toJson(variables);
      return json;
    }
  }

  /**
   * Handles finding similar recipes when prompted in front end.
   */
  private class FindSimilarRecipesHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      Gson gson = new Gson();
      JSONObject data = new JSONObject(request.body());
      String currentRecipeName = data.getString("recipe");
      // ToDo: create method to get all info about "CurrentRecipeName"
      Recipe curRecipe = getRecipeObject(currentRecipeName, recipeApp.getCurUser());

      //set ingredients to parsed string
      curRecipe.setInstructions(curRecipe.getInstructions().replaceAll("[\\[\\]()\\//{}\"]",
              "").replaceAll("[\b,]", "").replaceAll("[.]", ". "));

      ArrayList<Map<String, String>> similarRecipes = new ArrayList<>();
      TreeMap<Recipe, Double> map = recipeApp.getCurUser().findSimilarRecipes(currentRecipeName);
      for (Map.Entry<Recipe, Double> entry : map.entrySet()) {
        Recipe recipe = entry.getKey();
        similarRecipes.add(recipe.toSmallMap());
      }
      Map<String, Object> variables = ImmutableMap.of("recipe",
              curRecipe.toBigMap(), "similar1", similarRecipes.get(0),
              "similar2", similarRecipes.get(1), "similar3", similarRecipes.get(2));
      String json = GSON.toJson(variables);
      return json;
    }
  }

  private class RateRecipeHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String recipeName = data.getString("recipe");
      Double recipeRating = data.getDouble("rating");
      recipeApp.getCurUser().addRecipeRating(recipeName, recipeRating);
      return "";
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
      User newUser;
      try {
        //get inventory
        String inventoryString = getUserInventory(username);
        //splitting to create hashset for user ingredients
        if (inventoryString.length() > 0) {
          String[] inventoryArr = inventoryString.split(",");
          HashSet<String> ingredients = new HashSet<>(Arrays.asList(inventoryArr));
          //get rating
          String ratingString = getUserIngredientRatings(username);
          newUser = new User(username, ingredients);

          //splitting to create hashset for user ingredients
          if (ratingString.length() > 0) {
            String[] ratingArr = ratingString.split(",");
            for (String review : ratingArr) {
              String[] rating = review.split(":");
              newUser.addIngredientRating(rating[0], Double.parseDouble(rating[1]));
            }
          }
        } else {
          newUser = new User(username, null);
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
      Map<String, Object> map = new HashMap<>();
      String username = data.getString("name");
      try {
        String string = getUserInventory(username);

        //splitting to create hashset for user ingredients
        if (string.length() > 0) {
          String[] values = string.split(",");
          HashSet<String> ingredients = new HashSet<>(Arrays.asList(values));
          //get ratings for ingredients

          User user = recipeApp.getCurUser();
          HashMap<String, Double> preRated = user.getIngredientRatings();
          HashMap<String, String> ingRatings = new HashMap<>();

          for (String ingredient : ingredients) {
            boolean rated = preRated.containsKey(ingredient);
            if (rated) {
              ingRatings.put(ingredient, Double.toString(preRated.get(ingredient)));
            } else {
              ingRatings.put(ingredient, Double.toString(DEFAULT_RATING));
            }
            map = ImmutableMap.of("inventory", ingRatings);
          }
        } else {
          map = ImmutableMap.of("inventory", "");
        }
        return GSON.toJson(map);

      } catch (SQLException e) {
        System.err.println("ERROR: Error connecting to database");
        return "error";
      }
    }
  }
}

