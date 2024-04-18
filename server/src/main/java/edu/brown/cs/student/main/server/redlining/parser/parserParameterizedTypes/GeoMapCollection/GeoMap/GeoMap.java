package edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.GeoMapCollection.GeoMap;

public class GeoMap {
  public String type;
  public Geometry geometry;
  public Property properties;

  public Geometry getGeometry() {
    return this.geometry;
  }

  public Property getProperty() {
    return this.properties;
  }

  /**
   * Equals method for a GeoMap.
   *
   * @param o the object to compare to this
   * @return true if this has the same geometry as o, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof GeoMap)) {
      return false;
    }
    GeoMap other = (GeoMap) o;
    if (this.geometry == null) {
      return (other.geometry == null);
    } else {
      return (this.geometry.equals(other.geometry));
    }
  }

  // TODO: implement hashcode override
}
