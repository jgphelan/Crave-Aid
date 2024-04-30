package edu.brown.cs.StashedTests;
// package edu.brown.cs.student.main.server;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.fail;

// import
// edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.GeoMapCollection.GeoMapCollection;
// import edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.JSONParser;
// import edu.brown.cs.student.main.server.redlining.redliningData.CachedRedlining;
// import edu.brown.cs.student.main.server.redlining.redliningData.RedliningMocked;
// import edu.brown.cs.student.main.server.redlining.redliningData.RedliningReal;
// import java.io.FileNotFoundException;
// import java.util.ArrayList;
// import org.junit.jupiter.api.Test;

// public class TestRedliningDataSources {

//   ////////////////////////////////// RedliningReal
// ////////////////////////////////////////////////
//   /** Tests getting all redlining data via the getData method with no params. */
//   @Test
//   public void testGetAllDataReal() {
//     RedliningReal realDatasource = new RedliningReal();

//     JSONParser geojsonSource;
//     try {
//       geojsonSource = new JSONParser("data/geojson/fullDownload.geojson");
//       assertEquals(geojsonSource.getData(), realDatasource.getData());
//     } catch (FileNotFoundException e) {
//       fail();
//     }
//   }

//   /** Tests getData with bounding box yielding no results. */
//   @Test
//   public void testGetDataNoFilteredResultsReal() {
//     RedliningReal realDatasource = new RedliningReal();

//     GeoMapCollection emptyCollection = new GeoMapCollection();
//     emptyCollection.type = "FeatureCollection";
//     emptyCollection.features = new ArrayList<>();

//     // coordinates for the middle of the ocean
//     assertEquals(
//         emptyCollection, realDatasource.getData(39.777644, 37.583621, -70.465032, -65.158685));
//   }

//   /** Tests getData with bounding box yielding some results. */
//   @Test
//   public void testGetDataFilteredResultsReal() {
//     RedliningReal realDatasource = new RedliningReal();
//     GeoMapCollection actualCollection = realDatasource.getData(-71.5, -71.4, 41.7, 41.8);

//     GeoMapCollection emptyCollection = new GeoMapCollection();
//     emptyCollection.type = "FeatureCollection";
//     emptyCollection.features = new ArrayList<>();
//     assert (!actualCollection.equals(emptyCollection));
//   }

//   //  /**
//   //   * Tests getData with bounding box yielding some specific results.
//   //   */
//   //  @Test
//   //  public void testGetDataSpecificResultsReal() {
//   //    RedliningReal realDatasource = new RedliningReal();
//   //
//   //    try {
//   //      JSONParser geojsonSource = new JSONParser("data/geojson/providenceSubset.geojson");
//   //      GeoMapCollection expectedCollection = geojsonSource.getData();
//   //
//   ////      GeoMapCollection actualCollection = realDatasource.getData(-71.4189, -71.408,
// 41.76969,
//   // 41.775205);
//   ////      GeoMapCollection actualCollection = realDatasource.getData(-71.418, -71.408, 41.7696,
//   // 41.7753);
//   ////      GeoMapCollection actualCollection = realDatasource.getData(-71.418, -71.408, 41.769,
//   // 41.776);
//   ////      GeoMapCollection actualCollection = realDatasource.getData(-71.42, -71.41, 41.76,
//   // 41.78);
//   //      GeoMapCollection actualCollection = realDatasource.getData(-71.5, -71.4, 41.7, 41.8);
//   //
//   //      GeoMapCollection emptyCollection = new GeoMapCollection();
//   //      emptyCollection.type = "FeatureCollection";
//   //      emptyCollection.features = new ArrayList<>();
//   //
//   //      System.out.println(actualCollection.equals(emptyCollection));
//   //      assert(!actualCollection.equals(emptyCollection));
//   ////      assertEquals(expectedCollection, actualCollection);
//   //
//   //    } catch (FileNotFoundException e) {
//   //      fail();
//   //    }
//   //  }

//   ////////////////////////////////// RedliningMocked
// //////////////////////////////////////////////
//   /** Tests getting all redlining data via the getData method with no params. */
//   @Test
//   public void testGetAllDataMocked() {
//     GeoMapCollection example = new GeoMapCollection();
//     try {
//       JSONParser geojsonSource = new JSONParser("data/geojson/providenceSubset.geojson");
//       example = geojsonSource.getData();
//     } catch (FileNotFoundException e) {
//       fail();
//     }

//     RedliningMocked mockedSrc = new RedliningMocked(example, null);
//     assertEquals(example, mockedSrc.getData());
//   }

//   /** Tests getData with bounding box yielding filtered results. */
//   @Test
//   public void testGetDataFilteredResultsMocked() {
//     GeoMapCollection filtered = new GeoMapCollection();
//     try {
//       JSONParser geojsonSource = new JSONParser("data/geojson/providenceSubset.geojson");
//       filtered = geojsonSource.getData();
//     } catch (FileNotFoundException e) {
//       fail();
//     }

//     RedliningMocked mockedSrc = new RedliningMocked(null, filtered);
//     assertEquals(filtered, mockedSrc.getData(1, 2, 3, 4));
//     assertEquals(filtered, mockedSrc.getData(-50, 20, -31, 33));
//   }

//   ////////////////////////////////// CachedRedlining
// //////////////////////////////////////////////
//   /** Tests getting all redlining data via the getData method with no params. */
//   @Test
//   public void testGetAllDataCached() {
//     RedliningReal realDatasource = new RedliningReal();
//     CachedRedlining cachedDatasource = new CachedRedlining(realDatasource, 20, 2);

//     JSONParser geojsonSource;
//     try {
//       geojsonSource = new JSONParser("data/geojson/fullDownload.geojson");
//       assertEquals(geojsonSource.getData(), cachedDatasource.getData());
//     } catch (FileNotFoundException e) {
//       fail();
//     }
//   }

//   /** Tests getData with bounding box yielding no results. */
//   @Test
//   public void testGetDataNoFilteredResultsCached() {
//     RedliningReal realDatasource = new RedliningReal();
//     CachedRedlining cachedDatasource = new CachedRedlining(realDatasource, 20, 2);

//     GeoMapCollection emptyCollection = new GeoMapCollection();
//     emptyCollection.type = "FeatureCollection";
//     emptyCollection.features = new ArrayList<>();

//     // coordinates for the middle of the ocean
//     assertEquals(
//         emptyCollection, cachedDatasource.getData(39.777644, 37.583621, -70.465032, -65.158685));
//     assertEquals(
//         emptyCollection, cachedDatasource.getData(39.777644, 37.583621, -70.465032, -65.158685));
//   }

//   /** Tests getData with bounding box yielding some results. */
//   @Test
//   public void testGetDataFilteredResultsCached() {
//     RedliningReal realDatasource = new RedliningReal();
//     CachedRedlining cachedDatasource = new CachedRedlining(realDatasource, 20, 2);

//     GeoMapCollection emptyCollection = new GeoMapCollection();
//     emptyCollection.type = "FeatureCollection";
//     emptyCollection.features = new ArrayList<>();

//     GeoMapCollection actualCollection = cachedDatasource.getData(-71.5, -71.4, 41.7, 41.8);
//     assert (!actualCollection.equals(emptyCollection));

//     // call a few times to engage cache
//     actualCollection = cachedDatasource.getData(-71.5, -71.4, 41.7, 41.8);
//     assert (!actualCollection.equals(emptyCollection));

//     actualCollection = cachedDatasource.getData(-71.5, -71.4, 41.7, 41.8);
//     assert (!actualCollection.equals(emptyCollection));
//   }
// }
