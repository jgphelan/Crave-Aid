package edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.GeoMapCollection.GeoMap;

import java.util.List;

public class Geometry extends GeoMap {
  //  public String type;
  // NOTE: "type" does not work here because we used type already so I did not include a field
  public List<List<List<List<Double>>>> coordinates; // float doesnt exist in fromJava

  public List<List<List<List<Double>>>> getCoordinates() {
    return this.coordinates;
  }

  /**
   * Equals method for a Geometry.
   *
   * @param o the object to compare to this
   * @return true if this has the same coordinates as o, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Geometry)) {
      return false;
    }
    Geometry other = (Geometry) o;
    return (this.coordinates.equals(other.coordinates));
  }

  // TODO: implement hashcode override
}
