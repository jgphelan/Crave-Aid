package edu.brown.cs.student.main.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.brown.cs.student.main.server.pinHandlers.ClearPinsHandler;
import edu.brown.cs.student.main.server.storage.FirebaseUtilities;
import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

/** Class for testing the ClearPinsHandler class. */
public class TestClearPins {
  /** Sets up the server before each test */
  @BeforeEach
  public void setup() {
    StorageInterface storage = null;
    try {
      storage = new FirebaseUtilities();
    } catch (IOException e) {
      e.printStackTrace();
    }
    Spark.get("/clear-pins", new ClearPinsHandler(storage));
    Spark.awaitInitialization();
  }

  /** Breaks down the server after each test */
  @AfterEach
  public void tearDown() {
    Spark.stop();
    Spark.awaitStop();
  }

  /** Helper method to send a GET request to the server */
  private HttpURLConnection tryRequest(String apiCall) throws IOException {
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/clear-pins?" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.setRequestProperty("Accept", "application/json");
    clientConnection.connect();
    return clientConnection;
  }

  /** Tests clearing pins successfully. */
  @Test
  public void testClearPinsSuccess() throws IOException {
    HttpURLConnection connection = tryRequest("uid=someUserId");
    assertEquals(200, connection.getResponseCode());
    // Assuming response parsing logic here to check success message
  }

  /** Tests clearing pins with no user ID provided, expecting failure. */
  @Test
  public void testClearPinsNoUserId() throws IOException {
    HttpURLConnection connection = tryRequest("");
    assertEquals(400, connection.getResponseCode());
    // Assuming response parsing logic here to check error message
  }
}
