package edu.brown.cs.student.main.server.handlers;

import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/*
 * A class that handles the clearing of all words within the databse for a user.
 * specifically in the '/clear_user' endpoint.
 * This class is used in the Spark server to handle the '/clear_user' endpoint.
 *
 * @param storageHandler: StorageInterface object for handling database related tasks
 * @return Object: JSON object containing the response type
 */
public class ClearUserHandler implements Route {

  public StorageInterface storageHandler;

  public ClearUserHandler(StorageInterface storageHandler) {
    this.storageHandler = storageHandler;
  }

  /**
   * Invoked when a request is made on this route's corresponding path e.g. '/hello'
   *
   * @param request The request object providing information about the HTTP request
   * @param response The response object providing functionality for modifying the response
   * @return The content to be set in the response as failure or success
   */
  @Override
  public Object handle(Request request, Response response) {
    // a map to store the response
    Map<String, Object> responseMap = new HashMap<>();
    try {
      // Get the uid from the request
      String uid = request.queryParams("uid");

      // Remove the user from the database
      // System.out.println("clearing words for user: " + uid);
      this.storageHandler.clearUser(uid);

      responseMap.put("response_type", "success");

      // If an error occurs, return a failure message
    } catch (Exception e) {
      // error likely occurred in the storage handler
      e.printStackTrace();
      responseMap.put("response_type", "failure");
      responseMap.put("error", e.getMessage());
    }

    return Utils.toMoshiJson(responseMap);
  }
}
