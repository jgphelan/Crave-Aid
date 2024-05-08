package edu.brown.cs.student.main.server.ingredientHandlers;

import edu.brown.cs.student.main.server.storage.FirebaseUtilities;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

  /**
   * Invoked when a request is made on this route's corresponding path e.g. '/add_ingredient' Adds
   * an ingredient to a user's collection
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
    try {
      ingredient = URLDecoder.decode(ingredient, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    String collection = req.queryParams("collection");

    // Check if any of the parameters are missing
    if (uid == null || ingredient == null || collection == null) {
      res.status(400);
      return UtilsIngredients.toMoshiJson(
          Map.of("status", "failure", "message", "Missing parameters"));
    }

    try {
      // uses the FirebaseUtilities object to add the ingredient to the user's collection
      firebaseUtilities.addIngredient(uid, collection, ingredient);
      return UtilsIngredients.toMoshiJson(Map.of("status", "success"));

      // If an error occurs, return a failure message
    } catch (Exception e) {
      res.status(500);
      return UtilsIngredients.toMoshiJson(Map.of("status", "failure", "message", e.getMessage()));
    }
  }
}
