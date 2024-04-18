package edu.brown.cs.student.main.server.redlining.redliningData;

import edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.GeoMapCollection.GeoMapCollection;

public class RedliningMocked implements RedliningDatasource {
  private final GeoMapCollection fullMockedData;
  private final GeoMapCollection filteredMockedData;

  /**
   * Constructor for the RedliningMocked class. Sets data to caller specified values.
   *
   * @param fullData the GeoMapCollection the user wants to mock full available redlining data
   * @param filtered the GeoMapCollection the user wants to mock filtered redlining data
   */
  public RedliningMocked(GeoMapCollection fullData, GeoMapCollection filtered) {
    this.fullMockedData = fullData;
    this.filteredMockedData = filtered;
  }

  /**
   * Gathers all redlining data. (MOCKED)
   *
   * @return the GeoMapCollection of complete redlining data
   */
  public GeoMapCollection getData() {
    // not returning a defensive copy because mocked data is low-risk
    return this.fullMockedData;
  }

  /**
   * Gathers a filtered subset of the redlining data based on caller-specified bounding box.
   * (MOCKED)
   *
   * @param minLat the minimum longitude in the user's desired "bounding box"
   * @param maxLat the maximum longitude in the user's desired "bounding box"
   * @param minLong the minimum latitude in the user's desired "bounding box"
   * @param maxLong the maximum latitude in the user's desired "bounding box"
   * @return a GeoMapCollection composed of all the GeoMaps within the caller-defined "bounding box"
   */
  public GeoMapCollection getData(double minLat, double maxLat, double minLong, double maxLong) {
    // not returning a defensive copy because mocked data is low-risk
    return this.filteredMockedData;
  }
}
