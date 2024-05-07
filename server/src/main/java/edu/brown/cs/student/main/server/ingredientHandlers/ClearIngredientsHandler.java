package edu.brown.cs.student.main.server.ingredientHandlers;

import edu.brown.cs.student.main.server.storage.FirebaseUtilities;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearIngredientsHandler implements Route {
  private FirebaseUtilities firebaseUtilities;

  public ClearIngredientsHandler(FirebaseUtilities firebaseUtilities) {
    this.firebaseUtilities = firebaseUtilities;
  }

  @Override
  public Object handle(Request req, Response res) {
    String uid = req.queryParams("uid");
    String collection = req.queryParams("collection");

    if (uid == null || collection == null) {
      res.status(400);
      return UtilsIngredients.toMoshiJson(
          Map.of("status", "failure", "message", "Missing parameters"));
    }

    try {
      firebaseUtilities.clearAllIngredients(uid, collection);
      return UtilsIngredients.toMoshiJson(Map.of("status", "success"));
    } catch (Exception e) {
      res.status(500);
      return UtilsIngredients.toMoshiJson(Map.of("status", "failure", "message", e.getMessage()));
    }
  }
}
