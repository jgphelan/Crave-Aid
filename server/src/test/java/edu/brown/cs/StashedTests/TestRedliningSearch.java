package edu.brown.cs.StashedTests;
// package edu.brown.cs.student.main.server;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.fail;

// import
// edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.GeoMapCollection.GeoMapCollection;
// import edu.brown.cs.student.main.server.redlining.parser.parserParameterizedTypes.JSONParser;
// import edu.brown.cs.student.main.server.redlining.redliningData.RedliningMocked;
// import edu.brown.cs.student.main.server.redlining.redliningData.RedliningReal;
// import edu.brown.cs.student.main.server.redlining.redliningSearch.RedliningSearch;
// import java.io.FileNotFoundException;
// import java.util.ArrayList;
// import org.junit.jupiter.api.Test;

// /** Tests for the functionality of the RedliningSearch class. */
// public class TestRedliningSearch {

//   /** Tests functionality of areaDescContainsKeyword method. */
//   @Test
//   public void testContainsKeyword() {
//     GeoMapCollection example = new GeoMapCollection();
//     try {
//       JSONParser geojsonSource = new JSONParser("data/geojson/mockedFull.geojson");
//       example = geojsonSource.getData();
//     } catch (FileNotFoundException e) {
//       fail();
//     }

//     assert (RedliningSearch.areaDescContainsKeyword(example.features.get(0), "Gardens"));
//     assert (RedliningSearch.areaDescContainsKeyword(example.features.get(0), "policy of
// holding"));
//     assertFalse(
//         RedliningSearch.areaDescContainsKeyword(example.features.get(0), "keywordgibberish"));
//   }

//   /////////////////////////////////////// MOCKED DATA
// /////////////////////////////////////////////

//   /** Test search for keyword that doesn't exist in any GeoMap in data. */
//   @Test
//   public void testSearchNoResultsMocked() {
//     GeoMapCollection mockedFull = new GeoMapCollection();
//     GeoMapCollection mockedSubset = new GeoMapCollection();
//     try {
//       JSONParser geojsonSource = new JSONParser("data/geojson/mockedFull.geojson");
//       mockedFull = geojsonSource.getData();

//       geojsonSource = new JSONParser("data/geojson/mockedSubset.geojson");
//       mockedSubset = geojsonSource.getData();
//     } catch (FileNotFoundException e) {
//       fail();
//     }

//     GeoMapCollection emptyCollection = new GeoMapCollection();
//     emptyCollection.type = "FeatureCollection";
//     emptyCollection.features = new ArrayList<>();

//     RedliningMocked mockedSrc = new RedliningMocked(mockedFull, mockedSubset);
//     RedliningSearch searcher = new RedliningSearch(mockedSrc);

//     assertEquals(emptyCollection, searcher.search("totalgibberish"));
//   }

//   /** Test search for keyword that does exist in only some GeoMaps in data. */
//   @Test
//   public void testSearchSubsetAsResultsMocked() {
//     GeoMapCollection mockedFull = new GeoMapCollection();
//     GeoMapCollection mockedSubset = new GeoMapCollection();
//     try {
//       JSONParser geojsonSource = new JSONParser("data/geojson/mockedFull.geojson");
//       mockedFull = geojsonSource.getData();

//       geojsonSource = new JSONParser("data/geojson/mockedSubset.geojson");
//       mockedSubset = geojsonSource.getData();
//     } catch (FileNotFoundException e) {
//       fail();
//     }

//     RedliningMocked mockedSrc = new RedliningMocked(mockedFull, mockedSubset);
//     RedliningSearch searcher = new RedliningSearch(mockedSrc);

//     assertEquals(
//         mockedSubset, searcher.search("Sloping upward from valley to crest of Red Mountain"));
//   }

//   ///////////////////////////////////////// REAL DATA
// /////////////////////////////////////////////
//   /** Test search for keyword that doesn't exist in any GeoMap in data. */
//   @Test
//   public void testSearchNoResultsReal() {
//     GeoMapCollection emptyCollection = new GeoMapCollection();
//     emptyCollection.type = "FeatureCollection";
//     emptyCollection.features = new ArrayList<>();

//     RedliningReal realSrc = new RedliningReal();
//     RedliningSearch searcher = new RedliningSearch(realSrc);

//     assertEquals(emptyCollection, searcher.search("totalgibberish"));
//   }

//   /** Test search for keyword that does exist in only some GeoMaps in data. */
//   @Test
//   public void testSearchSubsetAsResultsReal() {
//     GeoMapCollection expectedSubset = new GeoMapCollection();
//     try {
//       JSONParser geojsonSource = new JSONParser("data/geojson/mockedSubset.geojson");
//       expectedSubset = geojsonSource.getData();
//     } catch (FileNotFoundException e) {
//       fail();
//     }

//     RedliningReal realSrc = new RedliningReal();
//     RedliningSearch searcher = new RedliningSearch(realSrc);

//     assertEquals(
//         expectedSubset, searcher.search("Sloping upward from valley to crest of Red Mountain"));
//   }
// }
