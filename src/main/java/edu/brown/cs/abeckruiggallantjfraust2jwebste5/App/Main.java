package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.Arrays;

import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database.addUserToDatabase;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database.getUserInventory;

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
  private RecipeApp app;

  private Main(String[] args) {
    this.args = args;
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
    app = new RecipeApp();
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

    Spark.post("/findSimilar", new FindSimilarRecipesHandler());
    Spark.post("/findSuggestions", new FindRecipeSuggestionsHandler());
    Spark.post("/enterIngredient", new EnterNewIngredientHandler());
    Spark.post("/newUser", new CreateNewUserHandler());
    Spark.post("/recipe", new GetRecipeHandler());
    Spark.post("/newUserSignup", new CreateNewUserHandlerSignup());
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
  /**
   * Handles returning values for a given recipe.
   */
  private class GetRecipeHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String currentRecipeName = data.getString("recipe");
      User currentUser = app.getCurUser();
      Recipe recipe = currentUser.findRecipe(currentRecipeName);
      return recipe.toJson();
    }
  }

  /**
   * Handles finding similar recipes when prompted in front end.
   */
  private class FindSimilarRecipesHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String currentRecipeName = data.getString("currentRecipe");

      User currentUser = app.getCurUser();
      TreeMap<Recipe, Double> map = currentUser.findSimilarRecipes(currentRecipeName);

      Map<String, Object> variables = ImmutableMap.of("firstSimilar", map.firstKey().toString());
      return GSON.toJson(variables);
    }
  }

  /**
   * Handles finding initial recipe suggestions.
   */
  private class FindRecipeSuggestionsHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      User currentUser = app.getCurUser();
      ArrayList<String> recipeSuggestions = currentUser.cook();
      Map<String, Object> variables = ImmutableMap.of("firstSuggestion", recipeSuggestions.get(0));
      return GSON.toJson(variables);
    }
  }
  /**
   * Handles entering in a new ingredient to the current users fridge.
   */
  private class EnterNewIngredientHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String ingredientName = data.getString("ingredientName");
      User currentUser = app.getCurUser();
      currentUser.addIngredient(ingredientName);
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
        String string = getUserInventory(username);
        //splitting to create hashset for user ingredients
        String[] values = string.split(",");
        HashSet<String> ingredients = new HashSet<>(Arrays.asList(values));
        User newUser = new User(username, ingredients);
        app.setCurUser(newUser);
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
        app.setCurUser(newUser);
        return "";
      } catch (SQLException e) {
        System.err.println("ERROR: Error connecting to database");
        return "error";
      }
    }
  }
}

