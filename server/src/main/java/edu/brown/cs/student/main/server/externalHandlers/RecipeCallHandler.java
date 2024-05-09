package edu.brown.cs.student.main.server.externalHandlers;

import edu.brown.cs.student.main.server.ingredientHandlers.UtilsIngredients;
import edu.brown.cs.student.main.server.parseFilterHelpers.Caller;
import edu.brown.cs.student.main.server.storage.FirebaseUtilities;
import java.util.List;
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

  /*
   * handle is called by another function when the frontend makes a call to the
   * backend. It takes in a string of ingredients delimited by commas and returns
   * a list of recipes and their respective info as a json
   *
   * @param req the request object to handle
   * @param res the response object to handle
   * @return a json string containing the status of the call and the recipe data
   */

  @Override
  public Object handle(Request req, Response res) {
    String ingredients = req.queryParams("ingredients");
    String uid = req.queryParams("uid");
    String[] ingredientArray = ingredients.split(",");

    // check if the parameters are missing
    if (ingredients == null) {
      res.status(400);
      return UtilsIngredients.toMoshiJson(
          Map.of("status", "failure", "message", "Missing parameters"));
    }

    try {
      // GET THE INITIAL JSON FROM CALL TO ALL RECIPES AND PASS THAT JSON INTO
      // parseMealIDFromMulti
      String url = "https://www.themealdb.com/api/json/v2/9973533/filter.php?i=" + ingredients;
      String json = UtilsIngredients.fullApiResponseString(url);

      String[] idArr = Caller.parseMealIDFromMulti(json);
      List<String> listIngredients = firebaseUtilities.getAllIngredients(uid, "pantry");
      // System.out.println(listIngredients);
      // List<String> listIngredients = new ArrayList();
      // listIngredients.add("Black Pepper");
      // listIngredients.add("Chicken");
      // listIngredients.add("Ginger Cordial");
      // listIngredients.add("Green Onions");
      String[][] infoArr = Caller.parse(uid, idArr, ingredientArray, listIngredients);

      // Serialize the 2D array
      // String jsonData = UtilsIngredients.toJson2DArray(infoArr); // alt strategy
      String recipeListJson = UtilsIngredients.parseRecipe(infoArr);

      // return the json string with the status and the recipe data
      return UtilsIngredients.toMoshiJson(Map.of("status", "success", "data", recipeListJson));
    } catch (Exception e) {
      // return a failure message if the call fails
      res.status(500);
      return UtilsIngredients.toMoshiJson(Map.of("status", "failure", "message", e.getMessage()));
    }
  }
}
