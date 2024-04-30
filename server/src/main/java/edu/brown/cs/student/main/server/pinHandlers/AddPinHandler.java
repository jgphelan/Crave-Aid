// package edu.brown.cs.student.main.server.pinHandlers;

// import edu.brown.cs.student.main.server.storage.StorageInterface;
// import java.util.HashMap;
// import java.util.Map;
// import spark.Request;
// import spark.Response;
// import spark.Route;

// public class AddPinHandler implements Route {
//   private final StorageInterface storage;

//   public AddPinHandler(StorageInterface storage) {
//     this.storage = storage;
//   }

//   @Override
//   public Object handle(Request req, Response reso) {
//     Map<String, Object> responseMap = new HashMap<>();
//     try {
//       String uid = req.queryParams("uid");
//       String pinId = req.queryParams("id");
//       double lat = Double.parseDouble(req.queryParams("lat"));
//       double lng = Double.parseDouble(req.queryParams("lng"));

//       storage.addPin(uid, pinId, lat, lng);
//       responseMap.put("status", "success");
//     } catch (Exception e) {
//       e.printStackTrace();
//       reso.status(500);
//       responseMap.put("status", "failure");
//       responseMap.put("message", e.getMessage());
//     }
//     return Utils.toMoshiJson(responseMap);
//   }
// }
