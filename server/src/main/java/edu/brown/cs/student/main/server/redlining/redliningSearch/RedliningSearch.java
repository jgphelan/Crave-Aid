package edu.brown.cs.student.main.server.redlining.redliningSearch;

import edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.GeoMapCollection.GeoMap.GeoMap;
import edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.GeoMapCollection.GeoMapCollection;
import edu.brown.cs.student.main.server.redlining.redliningData.RedliningDatasource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A class for searching the redlining data for areas with area descriptions containing a keyword.
 */
public class RedliningSearch {
  private final GeoMapCollection fullData;

  /**
   * Constructor for the RedliningSearch class. Gets data from source, which can be mocked.
   *
   * @param source the RedliningDatasource that this should gather redlining data from
   */
  public RedliningSearch(RedliningDatasource source) {
    this.fullData = source.getData();
  }

  /**
   * Searches the area descriptions of the redlining data to gather the subset of GeoMaps whose area
   * descriptions contain a certain keyword.
   *
   * @param keyword the String of interest to search area descriptions for
   * @return a GeoMapCollection containing all GeoMaps fitting the search criteria
   */
  public GeoMapCollection search(String keyword) {
    GeoMapCollection results = new GeoMapCollection();
    results.type = this.fullData.type;
    results.features = new ArrayList<>();

    for (GeoMap loc : this.fullData.features) {
      if (areaDescContainsKeyword(loc, keyword)) {
        results.features.add(loc);
      }
    }
    return results;
  }

  /**
   * Helper method to determine if a certain GeoMap contains a certain keyword in its area
   * description.
   *
   * @param loc the GeoMap to check
   * @param keyword the String keyword to check for
   * @return true if keyword is in the area description of loc, false otherwise
   */
  public static boolean areaDescContainsKeyword(GeoMap loc, String keyword) {
    // make a copy of area description data -- defensive programming
    Map<String, String> area_desc = new HashMap<>(loc.getProperty().area_description_data);

    for (String value : area_desc.values()) {
      if (value.contains(keyword)) {
        return true;
      }
    }
    return false;
  }
}
