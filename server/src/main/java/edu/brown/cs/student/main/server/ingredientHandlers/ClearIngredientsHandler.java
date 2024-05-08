package edu.brown.cs.student.main.server.ingredientHandlers;

import edu.brown.cs.student.main.server.storage.FirebaseUtilities;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * ClearIngredientsHandler class for handling clearing all ingredients from a user's collection
 *
 * @param firebaseUtilities: FirebaseUtilities dependency for doing Firebase operations such as
 *     clearing all database data
 */
public class ClearIngredientsHandler implements Route {
  private FirebaseUtilities firebaseUtilities;

  /**
   * Constructor for the ClearIngredientsHandler class
   *
   * @param firebaseUtilities: FirebaseUtilities object for handling Firebase related tasks
   */
  public ClearIngredientsHandler(FirebaseUtilities firebaseUtilities) {
    this.firebaseUtilities = firebaseUtilities;
  }

  /**
   * Called when a request is made on this routes path '/clear_ingredients' Clears all ingredients
   * from a user's collection in the firebase database based on the uid
   *
   * @param req The request object providing information about the HTTP request
   * @param res The response object with functionality on working with the response
   * @return The content to be set in the response which is either success or failure with a fail
   *     message
   */
  @Override
  public Object handle(Request req, Response res) {
    // Get the uid and collection from the request
    String uid = req.queryParams("uid");
    String collection = req.queryParams("collection");

    // Check if any of the parameters are missing
    if (uid == null || collection == null) {
      res.status(400);
      return UtilsIngredients.toMoshiJson(
          Map.of("status", "failure", "message", "Missing parameters"));
    }

    try {
      // uses the FirebaseUtilities object to clear all ingredients from the user's collection
      firebaseUtilities.clearAllIngredients(uid, collection);
      // Return a success message
      return UtilsIngredients.toMoshiJson(Map.of("status", "success"));
      // If an error occurs, return a failure message
    } catch (Exception e) {
      res.status(500);
      // Return a failure message
      return UtilsIngredients.toMoshiJson(Map.of("status", "failure", "message", e.getMessage()));
    }
  }
}
