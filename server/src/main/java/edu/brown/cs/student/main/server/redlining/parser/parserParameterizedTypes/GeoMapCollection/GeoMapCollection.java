package edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.GeoMapCollection;

import edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.GeoMapCollection.GeoMap.GeoMap;
import java.util.List;

public class GeoMapCollection {
  public String type;
  public List<GeoMap> features;

  /**
   * Equals method for a GeoMapCollection.
   *
   * @param o the object to compare to this
   * @return true if this has the same features as o, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof GeoMapCollection)) {
      return false;
    }
    GeoMapCollection other = (GeoMapCollection) o;
    return (this.features.equals(other.features));
  }

  // TODO: implement hashcode override
}
