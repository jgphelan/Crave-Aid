package edu.brown.cs.student.main.server.redlining.redliningSearch;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.GeoMapCollection.GeoMapCollection;
import edu.brown.cs.student.main.server.redlining.redliningData.RedliningDatasource;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** Handler class for redliningSearch endpoint. */
public class RedliningSearchHandler implements Route {
  private final RedliningDatasource datasource;
  private final RedliningSearch searcher;

  /**
   * Constructor for the RedliningSearchHandler class.
   *
   * @param redliningDatasource the datasource to use to retrieve redlining data to search
   */
  public RedliningSearchHandler(RedliningDatasource redliningDatasource) {
    this.datasource = redliningDatasource;
    this.searcher = new RedliningSearch(this.datasource);
  }

  /**
   * The handle method is called every time a request is sent to redliningSearch.
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

    // request should look like: .../redliningSearch?keyword=String

    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("endpoint", "redliningSearch");
    String keyword = request.queryParams("keyword");
    responseMap.put("keyword", keyword);

    if (keyword == null) {
      // attempted to search without a keyword
      responseMap.put(
          "result",
          "error_bad_request: Must include a String keyword in search query. Received no keyword.");
      return adapter.toJson(responseMap);
    } else {
      // search area descriptions and return results
      return geoMapAdapter.toJson(this.searcher.search(formatKeyword(keyword)));
    }
  }

  /**
   * Formats API received keyword arg, which may contain space substitutes, to the format expected
   * by the RedliningSearch search method.
   *
   * @param raw the raw String to format
   * @return raw formatted as expected by RedliningSearch's search method
   */
  private static String formatKeyword(String raw) {
    return raw.replaceAll("%20", " ");
  }
}
