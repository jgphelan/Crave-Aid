// package edu.brown.cs.student.main.server.pinHandlers;

// import edu.brown.cs.student.main.server.storage.StorageInterface;
// import java.util.List;
// import java.util.Map;
// import spark.Request;
// import spark.Response;
// import spark.Route;

// public class GetPinsHandler implements Route {
//   private final StorageInterface storage;

//   public GetPinsHandler(StorageInterface storage) {
//     this.storage = storage;
//   }

//   @Override
//   public Object handle(Request req, Response res) {
//     try {
//       String uid = req.queryParams("uid");
//       if (uid == null || uid.isEmpty()) {
//         res.status(400);
//         return Utils.toMoshiJson(
//             Map.of("status", "failure", "message", "User ID (uid) is required."));
//       }

//       List<Map<String, Object>> pins = storage.getPins(uid);
//       if (pins.isEmpty()) {
//         return Utils.toMoshiJson(
//             Map.of("status", "success", "message", "No pins found for user.", "data", pins));
//       } else {
//         return Utils.toMoshiJson(Map.of("status", "success", "data", pins));
//       }
//     } catch (Exception e) {
//       e.printStackTrace();
//       res.status(500);
//       return Utils.toMoshiJson(Map.of("status", "failure", "message", e.getMessage()));
//     }
//   }
// }
