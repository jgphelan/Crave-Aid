package edu.brown.cs.student.main.server.ingredientHandlers;

import edu.brown.cs.student.main.server.storage.FirebaseUtilities;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * RemoveIngredientHandler class for handling removing an ingredient from a user's collection
 *
 * @param firebaseUtilities: FirebaseUtilities object for handling Firebase related tasks
 */
public class RemoveIngredientHandler implements Route {
  private FirebaseUtilities firebaseUtilities;

  /**
   * Constructor for the RemoveIngredientHandler class
   *
   * @param firebaseUtilities: FirebaseUtilities dependency for doing Firebase operations such as
   *     removing an ingredient from a user's collection in the database
   */
  public RemoveIngredientHandler(FirebaseUtilities firebaseUtilities) {
    this.firebaseUtilities = firebaseUtilities;
  }

  /**
   * Called when a request is made to '/remove_ingredient' it removes an ingredient from a user's
   * collection in the firebase database based on the uid and ingredient
   *
   * @param req The request object providing information about the HTTP request
   * @param res The response object with functionality on working with the response
   * @return The content to be set in the response which is either success or failure with a fail
   *     message
   */
  @Override
  public Object handle(Request req, Response res) {
    // Get the uid, ingredient, and collection from the request
    String uid = req.queryParams("uid");
    String ingredient = req.queryParams("ingredient");
    String collection = req.queryParams("collection");

    // Check if any of the parameters are missing
    if (uid == null || ingredient == null || collection == null) {
      res.status(400);
      return UtilsIngredients.toMoshiJson(
          Map.of("status", "failure", "message", "Missing parameters"));
    }

    try {
      // Remove the ingredient from the user's collection using the FirebaseUtilities object
      firebaseUtilities.removeIngredient(uid, collection, ingredient);
      // Return only a success message since the ingredient was removed
      return UtilsIngredients.toMoshiJson(Map.of("status", "success"));
    } catch (Exception e) {
      // If an error occurs, return a failure message
      res.status(500);
      return UtilsIngredients.toMoshiJson(Map.of("status", "failure", "message", e.getMessage()));
    }
  }
}
