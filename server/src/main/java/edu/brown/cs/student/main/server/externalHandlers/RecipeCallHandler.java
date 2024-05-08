package edu.brown.cs.student.main.server.externalHandlers;

import edu.brown.cs.student.main.server.ingredientHandlers.UtilsIngredients;
import edu.brown.cs.student.main.server.parseFilterHelpers.Caller;
import edu.brown.cs.student.main.server.storage.FirebaseUtilities;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/*
 * RecipeCallHandler class - this class is a handler for the call
 *  made by the frontend. It takes in a string of ingredients delimited by
 *  commas and returns a list of recipes and their respective info as a json
 */
public class RecipeCallHandler implements Route {

  private FirebaseUtilities firebaseUtilities;

  public RecipeCallHandler(FirebaseUtilities firebaseUtilities) {
    this.firebaseUtilities = firebaseUtilities;
  }

  @Override
  public Object handle(Request req, Response res) {
    String ingredients = req.queryParams("ingredients");
    String uid = req.queryParams("uid");
    System.out.println(uid);

    if (ingredients == null) {
      res.status(400);
      return UtilsIngredients.toMoshiJson(
          Map.of("status", "failure", "message", "Missing parameters"));
    }

    // splits comma delineated ingredient list
    String[] ingredientArray = ingredients.split(",");

    try {
      // GET THE INITIAL JSON FROM CALL TO ALL RECIPES AND PASS THAT JSON INTO
      // parseMealIDFromMulti
      String url = "https://www.themealdb.com/api/json/v2/9973533/filter.php?i=" + ingredients;
      String json = UtilsIngredients.fullApiResponseString(url);

      String[] idArr = Caller.parseMealIDFromMulti(json);
      String[][] infoArr = Caller.parse(uid, idArr, ingredientArray, firebaseUtilities);

      // Serialize the 2D array
      // String jsonData = UtilsIngredients.toJson2DArray(infoArr); // alt strategy
      String recipeListJson = UtilsIngredients.parseRecipe(infoArr);

      return UtilsIngredients.toMoshiJson(Map.of("status", "success", "data", recipeListJson));
    } catch (Exception e) {
      res.status(500);
      return UtilsIngredients.toMoshiJson(Map.of("status", "failure", "message", e.getMessage()));
    }
  }
}
