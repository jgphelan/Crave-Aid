package edu.brown.cs.student.main.server.redlining.redliningData;

import edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.GeoMapCollection.GeoMap.GeoMap;
import edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.GeoMapCollection.GeoMap.Geometry;
import edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.GeoMapCollection.GeoMapCollection;
import edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.JSONParser;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/** An interface describing a data source for ACS (Census) data. */
public class RedliningReal implements RedliningDatasource {
  private final GeoMapCollection data;

  /**
   * Constructor for the RedliningReal class. Parses redlining geoJSON data into GeoMapCollection.
   */
  public RedliningReal() {
    JSONParser geojsonSource;
    try {
      geojsonSource = new JSONParser("server/data/geojson/fullDownload.geojson");
    } catch (FileNotFoundException e) {
      throw new IllegalStateException("Redlining geoJSON unexpectedly couldn't be parsed.");
    }
    this.data = geojsonSource.getData();
  }

  /**
   * Gathers all the redlining data for major U.S. cities in the 1930’s, sourced from Mapping
   * Inequality.
   *
   * @return the GeoMapCollection of complete redlining data
   */
  public GeoMapCollection getData() {
    GeoMapCollection copy = new GeoMapCollection();
    copy.type = this.data.type;
    // I recognize this is a pretty hand-wavy defensive copy, but in the interest of time...
    copy.features = new ArrayList<>(this.data.features);
    return copy;
  }

  /**
   * Gathers a filtered subset of the redlining data for major U.S. cities in the 1930’s, sourced
   * from Mapping Inequality, of the regions completely enclosed in a caller-defined "bounding box".
   *
   * @param minLat the minimum longitude in the caller's desired "bounding box"
   * @param maxLat the maximum longitude in the caller's desired "bounding box"
   * @param minLong the minimum latitude in the caller's desired "bounding box"
   * @param maxLong the maximum latitude in the caller's desired "bounding box"
   * @return a GeoMapCollection composed of all the GeoMaps within the caller-defined "bounding box"
   * @throws IllegalStateException if for whatever reason the coordinates in the GeoMap are not
   *     lat-long pairs
   */
  public GeoMapCollection getData(double minLat, double maxLat, double minLong, double maxLong)
      throws IllegalStateException {
    GeoMapCollection filtered = new GeoMapCollection();
    filtered.type = this.data.type;
    filtered.features = new ArrayList<>();

    for (GeoMap loc : this.data.features) {
      if (this.isInBounds(loc, minLat, maxLat, minLong, maxLong)) {
        filtered.features.add(loc);
      }
    }
    return filtered;
  }

  /**
   * Helper method to determine if a GeoMap is completely within the bounds of a "bounding box".
   *
   * @param location the GeoMap representing the region in question
   * @param minLat the minimum longitude in the user's desired "bounding box"
   * @param maxLat the maximum longitude in the user's desired "bounding box"
   * @param minLong the minimum latitude in the user's desired "bounding box"
   * @param maxLong the maximum latitude in the user's desired "bounding box"
   * @return true if the GeoMap is completely within the bounds defined by the args, false otherwise
   * @throws IllegalStateException if for whatever reason the coordinates in the GeoMap are not
   *     lat-long pairs
   */
  private boolean isInBounds(
      GeoMap location, double minLat, double maxLat, double minLong, double maxLong)
      throws IllegalStateException {
    Geometry geometry = location.getGeometry();
    if (geometry == null) {
      return false;
    }
    List<List<List<List<Double>>>> coordinates = geometry.getCoordinates();
    for (List<List<List<Double>>> a : coordinates) {
      for (List<List<Double>> b : a) {
        for (List<Double> latLong : b) {
          if (latLong.size() != 2) {
            throw new IllegalStateException(
                "List<Double> of coordinates in GeoMap coordinates is not lat/long pair. Found: "
                    + latLong);
          } else {
            double lat = latLong.get(0);
            double lon = latLong.get(1);
            if ((lat < minLat) || (lat > maxLat)) {
              return false;
            }
            if ((lon < minLong) || (lon > maxLong)) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }
}
