package edu.brown.cs.StashedTests;
// package edu.brown.cs.student.main.server;

// import static org.junit.jupiter.api.Assertions.assertEquals;

// import edu.brown.cs.student.main.server.pinHandlers.GetPinsHandler;
// import edu.brown.cs.student.main.server.storage.FirebaseUtilities;
// import edu.brown.cs.student.main.server.storage.StorageInterface;
// import java.io.IOException;
// import java.net.HttpURLConnection;
// import java.net.URL;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import spark.Spark;

// /** Class for testing the GetPinsHandler class. */
// public class TestGetPins {
//   /** Sets up the server before each test */
//   @BeforeEach
//   public void setup() {
//     StorageInterface storage = null;
//     try {
//       storage = new FirebaseUtilities();
//     } catch (IOException e) {
//       e.printStackTrace();
//     }
//     Spark.get("/get-pins", new GetPinsHandler(storage));
//     Spark.awaitInitialization();
//   }

//   /** Breaks down the server after each test */
//   @AfterEach
//   public void tearDown() {
//     Spark.stop();
//     Spark.awaitStop();
//   }

//   /** Helper method to send a GET request to the server */
//   private HttpURLConnection tryRequest(String apiCall) throws IOException {
//     URL requestURL = new URL("http://localhost:" + Spark.port() + "/get-pins?" + apiCall);
//     HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
//     clientConnection.setRequestProperty("Accept", "application/json");
//     clientConnection.connect();
//     return clientConnection;
//   }

//   /** Tests retrieving pins successfully. */
//   @Test
//   public void testGetPinsSuccess() throws IOException {
//     HttpURLConnection connection = tryRequest("uid=1234321");
//     assertEquals(200, connection.getResponseCode());
//   }

//   /** Tests retrieving pins with no user ID provided, expecting failure. */
//   @Test
//   public void testGetPinsNoUserId() throws IOException {
//     HttpURLConnection connection = tryRequest("");
//     assertEquals(400, connection.getResponseCode());
//   }
// }
