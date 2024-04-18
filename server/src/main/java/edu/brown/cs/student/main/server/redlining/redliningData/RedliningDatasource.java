package edu.brown.cs.student.main.server.redlining.redliningData;

import edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.GeoMapCollection.GeoMapCollection;

/** An interface describing a datasource for redlining data. */
public interface RedliningDatasource {
  /**
   * Gathers all redlining data.
   *
   * @return the GeoMapCollection of complete redlining data
   */
  GeoMapCollection getData();

  /**
   * Gathers a filtered subset of the redlining data based on caller-specified bounding box.
   *
   * @param minLat the minimum longitude in the user's desired "bounding box"
   * @param maxLat the maximum longitude in the user's desired "bounding box"
   * @param minLong the minimum latitude in the user's desired "bounding box"
   * @param maxLong the maximum latitude in the user's desired "bounding box"
   * @return a GeoMapCollection composed of all the GeoMaps within the caller-defined "bounding box"
   */
  GeoMapCollection getData(double minLat, double maxLat, double minLong, double maxLong);
}
