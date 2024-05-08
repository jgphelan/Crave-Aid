package edu.brown.cs.student.main.server.ingredientHandlers;

import edu.brown.cs.student.main.server.storage.FirebaseUtilities;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * GetAllIngredientsHandler class for handling getting all ingredients from a user's collection
 *
 * @param firebaseUtilities: FirebaseUtilities object for handling Firebase related tasks
 */
public class GetAllIngredientsHandler implements Route {
  private FirebaseUtilities firebaseUtilities;

  /**
   * Constructor for the GetAllIngredientsHandler class
   *
   * @param firebaseUtilities: FirebaseUtilities dependency for doing Firebase operations such as
   *     getting all data in a collection in the database
   */
  public GetAllIngredientsHandler(FirebaseUtilities firebaseUtilities) {
    this.firebaseUtilities = firebaseUtilities;
  }

  /**
   * Called when a request is made to '/get_all_ingredients' it gets all ingredients from a user's
   * collection in the firebase database based on the uid and returns them as a list in the json
   * response
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

    // Check if the uid and collection are null
    if (uid == null || collection == null) {
      res.status(400);
      return UtilsIngredients.toMoshiJson(
          // Return a failure message if the uid or collection is null
          Map.of("status", "failure", "message", "Missing parameters" + uid + " + " + collection));
    }

    try {
      // Get all ingredients from the user's collection in a list of strings that is jsoned
      List<String> ingredients = firebaseUtilities.getAllIngredients(uid, collection);
      return UtilsIngredients.toMoshiJson(Map.of("status", "success", "data", ingredients));
      // Catch any exceptions and return a failure message
    } catch (Exception e) {
      res.status(500);
      return UtilsIngredients.toMoshiJson(Map.of("status", "failure", "message", e.getMessage()));
    }
  }
}
