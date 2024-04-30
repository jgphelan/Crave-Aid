package edu.brown.cs.StashedTests;
// package edu.brown.cs.student.main.server;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.fail;

// import com.squareup.moshi.JsonAdapter;
// import com.squareup.moshi.Moshi;
// import com.squareup.moshi.Types;
// import
// edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.GeoMapCollection.GeoMapCollection;
// import edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.JSONParser;
// import edu.brown.cs.student.main.server.redlining.redliningData.RedliningDatasource;
// import edu.brown.cs.student.main.server.redlining.redliningData.RedliningMocked;
// import edu.brown.cs.student.main.server.redlining.redliningData.RedliningReal;
// import edu.brown.cs.student.main.server.redlining.redliningSearch.RedliningSearchHandler;
// import java.io.FileNotFoundException;
// import java.io.IOException;
// import java.lang.reflect.Type;
// import java.net.HttpURLConnection;
// import java.net.URL;
// import java.util.ArrayList;
// import java.util.Map;
// import okio.Buffer;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import spark.Spark;

// /** Tests RedliningSearchHandler in the context of the server (with real and mocked tests). */
// public class TestRedliningSearchHandler {
//   private RedliningSearchHandler redliningSearchHandler;
//   private RedliningDatasource redliningDatasource;
//   private GeoMapCollection exampleFull;
//   private GeoMapCollection exampleSearchResults;
//   private GeoMapCollection emptyCollection;
//   private JsonAdapter<Map<String, String>> adapter;
//   private final Type mapStringObject =
//       Types.newParameterizedType(Map.class, String.class, String.class);
//   private JsonAdapter<GeoMapCollection> geoMapAdapter;

//   /** Sets up the server and passes in the mocked api before each test */
//   @BeforeEach
//   public void setup() {
//     this.exampleFull = new GeoMapCollection();
//     this.exampleSearchResults = new GeoMapCollection();
//     try {
//       JSONParser fullGeojsonSource = new JSONParser("data/geojson/mockedFull.geojson");
//       this.exampleFull = fullGeojsonSource.getData();

//       JSONParser filteredGeojsonSource = new JSONParser("data/geojson/mockedSubset.geojson");
//       this.exampleSearchResults = filteredGeojsonSource.getData();
//     } catch (FileNotFoundException e) {
//       fail();
//     }

//     this.emptyCollection = new GeoMapCollection();
//     emptyCollection.type = "FeatureCollection";
//     emptyCollection.features = new ArrayList<>();

//     Moshi moshi = new Moshi.Builder().build();
//     this.adapter = moshi.adapter(this.mapStringObject);
//     this.geoMapAdapter = moshi.adapter(GeoMapCollection.class);
//   }

//   /** Breaks down the server after each test */
//   @AfterEach
//   public void tearDown() {
//     Spark.unmap("/redliningSearch");
//     Spark.awaitStop();
//   }

//   /**
//    * Helper method to send a request to the server.
//    *
//    * @param apicall the arguments to pass to the endpoint, following a "?"
//    * @return the connection for the given URL, just after connecting
//    * @throws IOException if unable to connect to Server
//    */
//   private HttpURLConnection tryRequest(String apicall) throws IOException {
//     // Configure the connection (but don't actually send a request yet)
//     URL requestURL = new URL("http://localhost:" + Spark.port() + "/redliningSearch?" + apicall);
//     HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
//     // The request body contains a Json object
//     clientConnection.setRequestProperty("Content-Type", "application/json");
//     // We're expecting a Json object in the response body
//     clientConnection.setRequestProperty("Accept", "application/json");

//     clientConnection.connect();
//     return clientConnection;
//   }

//   /////////////////////////////////////// MOCKED DATA
// /////////////////////////////////////////////

//   /** Test search for keyword that doesn't exist in any GeoMap in data. */
//   @Test
//   public void testSearchNoResultsMocked() throws IOException {
//     this.redliningDatasource = new RedliningMocked(this.exampleFull, this.exampleSearchResults);
//     this.redliningSearchHandler = new RedliningSearchHandler(this.redliningDatasource);
//     Spark.get("/redliningSearch", this.redliningSearchHandler);
//     Spark.awaitInitialization();

//     HttpURLConnection connection = tryRequest("keyword=totalgibberish");
//     assertEquals(200, connection.getResponseCode());
//     GeoMapCollection responseBody =
//         this.geoMapAdapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
//     assertEquals(this.emptyCollection, responseBody);
//   }

//   /** Test search for keyword that does exist in only some GeoMaps in data. */
//   @Test
//   public void testSearchSubsetAsResultsMocked() throws IOException {
//     this.redliningDatasource = new RedliningMocked(this.exampleFull, this.exampleSearchResults);
//     this.redliningSearchHandler = new RedliningSearchHandler(this.redliningDatasource);
//     Spark.get("/redliningSearch", this.redliningSearchHandler);
//     Spark.awaitInitialization();

//     HttpURLConnection connection =
//         tryRequest(
//             "keyword=Sloping upward from valley to crest of Red Mountain".replaceAll(" ",
// "%20"));
//     assertEquals(200, connection.getResponseCode());
//     GeoMapCollection responseBody =
//         this.geoMapAdapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
//     assertEquals(this.exampleSearchResults, responseBody);
//   }

//   ///////////////////////////////////////// REAL DATA
// /////////////////////////////////////////////
//   /** Test search for keyword that doesn't exist in any GeoMap in data. */
//   @Test
//   public void testSearchNoResultsReal() throws IOException {
//     this.redliningDatasource = new RedliningReal();
//     this.redliningSearchHandler = new RedliningSearchHandler(this.redliningDatasource);
//     Spark.get("/redliningSearch", this.redliningSearchHandler);
//     Spark.awaitInitialization();

//     HttpURLConnection connection = tryRequest("keyword=totalgibberish");
//     assertEquals(200, connection.getResponseCode());
//     GeoMapCollection responseBody =
//         this.geoMapAdapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
//     assertEquals(this.emptyCollection, responseBody);
//   }

//   /** Test search for keyword that does exist in only some GeoMaps in data. */
//   @Test
//   public void testSearchSubsetAsResultsReal() throws IOException {
//     this.redliningDatasource = new RedliningReal();
//     this.redliningSearchHandler = new RedliningSearchHandler(this.redliningDatasource);
//     Spark.get("/redliningSearch", this.redliningSearchHandler);
//     Spark.awaitInitialization();

//     HttpURLConnection connection =
//         tryRequest(
//             "keyword=Sloping upward from valley to crest of Red Mountain".replaceAll(" ",
// "%20"));
//     assertEquals(200, connection.getResponseCode());
//     GeoMapCollection responseBody =
//         this.geoMapAdapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
//     assertEquals(this.exampleSearchResults, responseBody);
//   }

//   /////////////////////////////////////// ERRORS
// /////////////////////////////////////////////////

//   /** Tests edge case of no keyword sent to endpoint */
//   @Test
//   public void testWrongNumberArgs() throws IOException {
//     this.redliningDatasource = new RedliningReal();
//     this.redliningSearchHandler = new RedliningSearchHandler(this.redliningDatasource);
//     Spark.get("/redliningSearch", this.redliningSearchHandler);
//     Spark.awaitInitialization();

//     HttpURLConnection connection = tryRequest("");
//     assertEquals(200, connection.getResponseCode());
//     Map<String, String> responseBody =
//         this.adapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
//     assertEquals(
//         "error_bad_request: Must include a String keyword in search query. Received no keyword.",
//         responseBody.get("result"));
//   }
// }
