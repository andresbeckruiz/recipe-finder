package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import com.google.common.collect.ImmutableMap;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Recipe.Recipe;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.json.JSONObject;
import spark.*;
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
  private User currentUser;
  private boolean userSet;

  private Main(String[] args) {
    this.args = args;
    this.userSet = false;
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
    // used to rewrite sql database
//    try {
//      parseJson();
//    } catch (Exception e) {
//      System.out.println("ERROR");
//    }
    RecipeApp app = new RecipeApp();
    //this should eventually be user ingredients
    HashSet<String> ingredients = new HashSet<>() {
      {
        add("double cream");
        add("lemon curd");
        add("lemon");
      }
    };
    User user = new User("georgia", ingredients);
    ArrayList<String> possibleRecipes = user.cook();
    String recipeSelected = possibleRecipes.get(0);
    System.out.println("RECIPES SIMILAR TO " + recipeSelected + " : ");
    TreeMap<Recipe, Double> map = user.findSimilarRecipes(recipeSelected);
    for (Map.Entry<Recipe, Double> entry : map.entrySet()) {
      System.out.println("Key: " + entry.getKey() + ". Value: " + entry.getValue());
    }
    map = user.findSimilarRecipes(recipeSelected);
    for (Map.Entry<Recipe, Double> entry : map.entrySet()) {
      System.out.println("Key: " + entry.getKey() + ". Value: " + entry.getValue());
    }
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
   * Handles finding similar recipes when prompted in front end
   */
  private class FindSimilarRecipesHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String currentRecipeName = data.getString("currentRecipe");

      TreeMap<Recipe, Double> map = currentUser.findSimilarRecipes(currentRecipeName);

      Map<String, Object> variables = ImmutableMap.of("firstSimilar", map.firstKey().toString());
      return GSON.toJson(variables);
    }
  }

  /**
   * Handles finding initial recipe suggestions
   */
  private class FindRecipeSuggestionsHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      ArrayList<String> recipeSuggestions = currentUser.cook();
      Map<String, Object> variables = ImmutableMap.of("firstSuggestion", recipeSuggestions.get(0));
      return GSON.toJson(variables);
    }
  }

  /**
   * Handles entering in a new ingredient to the current users fridge
   */
  private class EnterNewIngredientHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String ingredientName = data.getString("ingredientName");
      currentUser.addIngredient(ingredientName);
      return null;
    }
  }

  /**
   * Handles creating a new user object and updating the currentUser object
   */
  private class CreateNewUserHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String username = data.getString("name");
      System.out.println("Username" + username);
      //check if user exists. If not, create new row in table
//      try {
//        Class.forName("org.sqlite.JDBC");
//        String urlToDB = "jdbc:sqlite:" + filename;
//        Connection conn = DriverManager.getConnection(urlToDB);
//      }
//      catch (SQLException e) {
//        System.err.println("ERROR: Error connecting to database");
//        return "error";
//      } catch (ClassNotFoundException e) {
//        System.err.println("ERROR: Invalid database class");
//        return "error";


      HashSet<String> ingredients = new HashSet<>();
      User newUser = new User(username, ingredients);
      currentUser = newUser;
      return "";
    }
  }
}
