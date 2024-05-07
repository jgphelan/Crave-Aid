package edu.brown.cs.student.main.server.ingredientHandlers;

import edu.brown.cs.student.main.server.storage.FirebaseUtilities;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class GetAllIngredientsHandler implements Route {
  private FirebaseUtilities firebaseUtilities;

  public GetAllIngredientsHandler(FirebaseUtilities firebaseUtilities) {
    this.firebaseUtilities = firebaseUtilities;
  }

  @Override
  public Object handle(Request req, Response res) {
    String uid = req.queryParams("uid");
    String collection = req.queryParams("collection");

    if (uid == null || collection == null) {
      res.status(400);
      return UtilsIngredients.toMoshiJson(
          Map.of("status", "failure", "message", "Missing parameters" + uid + " + " + collection));
    }

    try {
      List<String> ingredients = firebaseUtilities.getAllIngredients(uid, collection);
      return UtilsIngredients.toMoshiJson(Map.of("status", "success", "data", ingredients));
    } catch (Exception e) {
      res.status(500);
      return UtilsIngredients.toMoshiJson(Map.of("status", "failure", "message", e.getMessage()));
    }
  }
}
