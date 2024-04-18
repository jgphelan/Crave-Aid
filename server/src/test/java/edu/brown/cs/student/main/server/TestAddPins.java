package edu.brown.cs.student.main.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.pinHandlers.AddPinHandler;
import edu.brown.cs.student.main.server.storage.FirebaseUtilities;
import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

/** Class for testing the AddPinHandler functionality via GET requests. */
public class TestAddPins {
  private JsonAdapter<Map<String, Object>> adapter;
  private final Type mapStringObject =
      Types.newParameterizedType(Map.class, String.class, Object.class);

  /** Sets up the server before each test. */
  @BeforeEach
  public void setup() {
    // Initialize and start your Spark server here, if not already running
    StorageInterface storage = null;
    try {
      storage = new FirebaseUtilities();
    } catch (IOException e) {
      e.printStackTrace();
    }
    Spark.get("/add-pin", new AddPinHandler(storage));
    Spark.awaitInitialization();

    // Initialize Moshi adapter for JSON parsing
    Moshi moshi = new Moshi.Builder().build();
    this.adapter = moshi.adapter(this.mapStringObject);
  }

  /** Breaks down the server after each test. */
  @AfterEach
  public void tearDown() {
    Spark.stop();
    Spark.awaitStop();
  }

  /**
   * Helper method to send a GET request to the server.
   *
   * @param apiCall the API endpoint including query parameters
   * @return the connection for the given URL, just after connecting
   * @throws IOException if unable to connect to Server
   */
  private HttpURLConnection tryGetRequest(String apiCall) throws IOException {
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/add-pin?" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.setRequestProperty("Accept", "application/json");
    clientConnection.connect();
    return clientConnection;
  }

  /** Test adding a pin successfully via GET request. */
  @Test
  public void testAddPinSuccess() throws IOException {
    String apiCall = "uid=user123&id=pin1&lat=41.82&lng=-71.4";
    HttpURLConnection connection = tryGetRequest(apiCall);
    assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());

    Map<String, Object> response =
        this.adapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
    assertEquals("success", response.get("status"));
  }

  /** Test adding a pin with missing parameters via GET request. */
  @Test
  public void testAddPinMissingParams() throws IOException {
    String apiCall = "uid=user123&lat=41.82";
    HttpURLConnection connection = tryGetRequest(apiCall);
    assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, connection.getResponseCode());
    Map<String, Object> response =
        this.adapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
    assertEquals("failure", response.get("status"));
  }

  @Test
  public void testAddPinIncorrectParams() throws IOException {
    String apiCall = "uid=user123&id=pin1&lat=invalid&lng=invalid";
    HttpURLConnection connection = tryGetRequest(apiCall);
    assertEquals(HttpURLConnection.HTTP_INTERNAL_ERROR, connection.getResponseCode());
    Map<String, Object> response =
        this.adapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
    assertEquals("failure", response.get("status"));
  }
}
