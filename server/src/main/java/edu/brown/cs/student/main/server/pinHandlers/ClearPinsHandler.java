package edu.brown.cs.student.main.server.pinHandlers;

import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearPinsHandler implements Route {
  private final StorageInterface storage;

  public ClearPinsHandler(StorageInterface storage) {
    this.storage = storage;
  }

  @Override
  public Object handle(Request req, Response res) {
    Map<String, Object> responseMap = new HashMap<>();
    try {
      String uid = req.queryParams("uid");
      if (uid == null || uid.isEmpty()) {
        res.status(400);
        responseMap.put("status", "failure");
        responseMap.put("message", "User ID (uid) is required.");
      } else {
        storage.clearPins(uid);
        responseMap.put("status", "success");
        responseMap.put("message", "All pins cleared successfully.");
      }
    } catch (Exception e) {
      e.printStackTrace();
      res.status(500);
      responseMap.put("status", "failure");
      responseMap.put("message", "Error clearing pins: " + e.getMessage());
    }
    return Utils.toMoshiJson(responseMap);
  }
}
