package edu.brown.cs.student.main.server.ingredientHandlers;

import edu.brown.cs.student.main.server.storage.FirebaseUtilities;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * AddIngredientHandler class for handling adding an ingredient to a user's collection
 *
 * @param firebaseUtilities: FirebaseUtilities object for handling Firebase related tasks
 */
public class AddIngredientHandler implements Route {
  private FirebaseUtilities firebaseUtilities;

  public AddIngredientHandler(FirebaseUtilities firebaseUtilities) {
    this.firebaseUtilities = firebaseUtilities;
  }

  @Override
  public Object handle(Request req, Response res) {
    String uid = req.queryParams("uid");
    String ingredient = req.queryParams("ingredient");
    String collection = req.queryParams("collection");

    if (uid == null || ingredient == null || collection == null) {
      res.status(400);
      return UtilsIngredients.toMoshiJson(
          Map.of("status", "failure", "message", "Missing parameters"));
    }

    try {
      firebaseUtilities.addIngredient(uid, collection, ingredient);
      return UtilsIngredients.toMoshiJson(Map.of("status", "success"));
    } catch (Exception e) {
      res.status(500);
      return UtilsIngredients.toMoshiJson(Map.of("status", "failure", "message", e.getMessage()));
    }
  }
}
