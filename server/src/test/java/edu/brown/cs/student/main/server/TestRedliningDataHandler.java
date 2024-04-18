package edu.brown.cs.student.main.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.GeoMapCollection.GeoMapCollection;
import edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.JSONParser;
import edu.brown.cs.student.main.server.redlining.redliningData.RedliningDataHandler;
import edu.brown.cs.student.main.server.redlining.redliningData.RedliningDatasource;
import edu.brown.cs.student.main.server.redlining.redliningData.RedliningMocked;
import java.io.FileNotFoundException;
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

/** Class for testing the RedliningDataHandler class. */
public class TestRedliningDataHandler {
  private RedliningDataHandler redliningDataHandler;
  private RedliningDatasource redliningDatasource;
  private GeoMapCollection exampleFull;
  private GeoMapCollection exampleFiltered;
  private JsonAdapter<Map<String, String>> adapter;
  private final Type mapStringObject =
      Types.newParameterizedType(Map.class, String.class, String.class);
  private JsonAdapter<GeoMapCollection> geoMapAdapter;

  /** Sets up the server and passes in the mocked api before each test */
  @BeforeEach
  public void setup() {
    this.exampleFull = new GeoMapCollection();
    this.exampleFiltered = new GeoMapCollection();
    try {
      JSONParser fullGeojsonSource = new JSONParser("data/geojson/mockedFull.geojson");
      this.exampleFull = fullGeojsonSource.getData();

      JSONParser filteredGeojsonSource = new JSONParser("data/geojson/providenceSubset.geojson");
      this.exampleFiltered = filteredGeojsonSource.getData();
    } catch (FileNotFoundException e) {
      fail();
    }
    this.redliningDatasource = new RedliningMocked(this.exampleFull, this.exampleFiltered);
    this.redliningDataHandler = new RedliningDataHandler(this.redliningDatasource);

    Spark.get("/redliningData", this.redliningDataHandler);
    Spark.awaitInitialization();
    Moshi moshi = new Moshi.Builder().build();
    this.adapter = moshi.adapter(this.mapStringObject);
    this.geoMapAdapter = moshi.adapter(GeoMapCollection.class);
  }

  /** Breaks down the server after each test */
  @AfterEach
  public void tearDown() {
    Spark.unmap("/redliningData");
    Spark.awaitStop();
  }

  /**
   * Helper method to send a request to the server
   *
   * @param apiCall the arguments to pass to the endpoint, following a "?"
   * @return the connection for the given URL, just after connecting
   * @throws IOException if unable to connect to Server
   */
  private HttpURLConnection tryRequest(String apiCall) throws IOException {
    // Configure the connection (but don't actually send a request yet)
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/redliningData?" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    // The request body contains a Json object
    clientConnection.setRequestProperty("Content-Type", "application/json");
    // We're expecting a Json object in the response body
    clientConnection.setRequestProperty("Accept", "application/json");

    clientConnection.connect();
    return clientConnection;
  }

  /**
   * Tests retrieving all data (mocked).
   *
   * @throws IOException if unable to connect to Server
   */
  @Test
  public void testGetAllDataMocked() throws IOException {
    HttpURLConnection connection = tryRequest("");
    assertEquals(200, connection.getResponseCode());
    GeoMapCollection responseBody =
        this.geoMapAdapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
    assertEquals(this.exampleFull, responseBody);
  }

  /**
   * Tests retrieving filtered data (mocked).
   *
   * @throws IOException if unable to connect to Server
   */
  @Test
  public void testGetFilteredDataMocked() throws IOException {
    HttpURLConnection connection = tryRequest("minLat=1&maxLat=5&minLong=-20&maxLong=3");
    assertEquals(200, connection.getResponseCode());
    GeoMapCollection responseBody =
        this.geoMapAdapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
    assertEquals(this.exampleFiltered, responseBody);
  }

  /**
   * Tests error checking of bad arguments to endpoint (partial bounding box).
   *
   * @throws IOException if unable to connect to Server
   */
  @Test
  public void testBadArgsWrongNumber() throws IOException {
    HttpURLConnection connection = tryRequest("minLat=1&maxLat=5");
    assertEquals(200, connection.getResponseCode());
    Map<String, String> responseBody =
        this.adapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
    assertEquals(
        "error_bad_request: Must include entirety of bounding box or none. Received only part of a bounding box.",
        responseBody.get("result"));
  }

  /**
   * Tests error checking of bad arguments to endpoint (non-number args).
   *
   * @throws IOException if unable to connect to Server
   */
  @Test
  public void testBadArgsNonNumbers() throws IOException {
    HttpURLConnection connection = tryRequest("minLat=1&maxLat=5&minLong=something&maxLong=2");
    assertEquals(200, connection.getResponseCode());
    Map<String, String> responseBody =
        this.adapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
    assertEquals(
        "error_bad_request: At least one of bounding box parameters are non-numbers.",
        responseBody.get("result"));
  }
}
