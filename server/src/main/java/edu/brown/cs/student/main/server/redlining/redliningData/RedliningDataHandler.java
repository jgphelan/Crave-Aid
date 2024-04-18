package edu.brown.cs.student.main.server.redlining.redliningData;

import static java.lang.Double.parseDouble;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.GeoMapCollection.GeoMapCollection;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** Handler class for redliningData endpoint. */
public class RedliningDataHandler implements Route {
  private final RedliningDatasource datasource;

  /**
   * Constructor for the RedliningDataHandler class.
   *
   * @param redliningDatasource the datasource to use to retrieve redlining data
   */
  public RedliningDataHandler(RedliningDatasource redliningDatasource) {
    this.datasource = redliningDatasource;
  }

  /**
   * The handle method is called every time a request is sent to redliningData.
   *
   * @param request the Request of the user
   * @param response the Response to the request, unused in this implementation
   * @return a serialized json describing the results of executing request
   */
  @Override
  public Object handle(Request request, Response response) {
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, String.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    JsonAdapter<GeoMapCollection> geoMapAdapter = moshi.adapter(GeoMapCollection.class);

    // request can look like:
    // .../redliningData
    // .../redliningData?minLat=Number&maxLat=Number&minLong=Number&maxLong=Number

    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("endpoint", "redliningData");
    String minLat = request.queryParams("minLat");
    responseMap.put("minLat", minLat);
    String maxLat = request.queryParams("maxLat");
    responseMap.put("maxLat", maxLat);
    String minLong = request.queryParams("minLong");
    responseMap.put("minLong", minLong);
    String maxLong = request.queryParams("maxLong");
    responseMap.put("maxLong", maxLong);

    if ((minLat == null) && (maxLat == null) && (minLong == null) && (maxLong == null)) {
      // query all data
      return geoMapAdapter.toJson(this.datasource.getData());
    } else if ((minLat != null) && (maxLat != null) && (minLong != null) && (maxLong != null)) {
      // query filtered data
      try {
        double minLatNum = parseDouble(minLat);
        double maxLatNum = parseDouble(maxLat);
        double minLongNum = parseDouble(minLong);
        double maxLongNum = parseDouble(maxLong);

        return geoMapAdapter.toJson(
            this.datasource.getData(minLatNum, maxLatNum, minLongNum, maxLongNum));
      } catch (NumberFormatException n) {
        responseMap.put(
            "result",
            "error_bad_request: At least one of bounding box parameters are non-numbers.");
        return adapter.toJson(responseMap);
      } catch (IllegalStateException e) {
        responseMap.put(
            "result",
            "error_datasource: Unexpected non-coordinates encountered when parsing redlining data.");
        return adapter.toJson(responseMap);
      }
    } else {
      // attempted to query with only part of a bounding box
      responseMap.put(
          "result",
          "error_bad_request: Must include entirety of bounding box or none. Received only part of a bounding box.");
      return adapter.toJson(responseMap);
    }
  }
}
