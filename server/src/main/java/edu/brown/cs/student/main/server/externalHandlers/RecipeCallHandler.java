package edu.brown.cs.student.main.server.externalHandlers;

import edu.brown.cs.student.main.server.ingredientHandlers.Utils;
import edu.brown.cs.student.main.server.parseFilterHelpers.Caller;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class RecipeCallHandler implements Route {

  @SuppressWarnings("unused")
  @Override
  public Object handle(Request req, Response res) {
    String ingredients = req.queryParams("ingredients");
    // splits comma delineated ingredient list
    String[] ingredientArray = ingredients.split(",");

    if (ingredients == null) {
      res.status(400);
      return Utils.toMoshiJson(Map.of("status", "failure", "message", "Missing parameters"));
    }

    try {
      // GET THE INITIAL JSON FROM CALL TO ALL RECIPES AND PASS THAT JSON INTO
      // parseMealIDFromMulti
      String url = "https://www.themealdb.com/api/json/v2/9973533/filter.php?i=" + ingredients;
      String json = Utils.fullApiResponseString(url);

      String[] idArr = Caller.parseMealIDFromMulti(json);
      String[][] infoArr = Caller.parse(idArr, ingredientArray);

      // Serialize the 2D array
      String jsonData = Utils.toJson2DArray(infoArr); // TODO switch to new strat
      String recipeListJson = Utils.parseRecipe(infoArr);

      // TODO confirm correct
      return Utils.toMoshiJson(Map.of("status", "success", "data", recipeListJson));
    } catch (Exception e) {
      res.status(500);
      return Utils.toMoshiJson(Map.of("status", "failure", "message", e.getMessage()));
    }
  }
}
