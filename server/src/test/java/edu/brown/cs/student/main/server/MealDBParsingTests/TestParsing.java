package edu.brown.cs.student.main.server.MealDBParsingTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.brown.cs.student.main.server.parseFilterHelpers.Caller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

public class TestParsing {

  private Caller caller;

  String jsonPath = "/Users/Jimmy.Phelan/academics/cs32/Crave-Aid/server/data/Mocks/SearchByIDEx";

  String catJSONPath =
      "/Users/Jimmy.Phelan/academics/cs32/Crave-Aid/server/data/Mocks/CategoryExJSON";

  String jsonPath2 =
      "/Users/Jimmy.Phelan/academics/cs32/Crave-Aid/server/data/Mocks/SearchByMultiIngredient";

  String nullJSONPath =
      "/Users/Jimmy.Phelan/academics/cs32/Crave-Aid/server/data/Mocks/SearchByMultiInvalid";

  @Test
  public void testParseMealIDFromMultiBasic() throws IOException {
    // Read the JSON file content into a String
    String jsonContent = new String(Files.readAllBytes(Paths.get(jsonPath)));

    // Instantiate Caller
    Caller caller = new Caller();

    // Invoke the method to be tested
    String[] mealIDs = caller.parseMealIDFromMulti(jsonContent);

    // Example assertion (adjust as necessary based on expected output)
    assertNotNull(mealIDs, "Meal IDs should not be null");
    assertTrue(mealIDs.length > 0, "There should be at least one meal ID");
    assertEquals("52772", mealIDs[0], "The first meal ID should match the expected value");
  }

  @Test
  public void testParseMealIDFromMulti() throws IOException {
    String jsonContent = new String(Files.readAllBytes(Paths.get(nullJSONPath)));

    // Instantiate Caller
    Caller caller = new Caller();

    // Invoke the method to be tested
    String[] mealIDs = caller.parseMealIDFromMulti(jsonContent);

    assertNotNull(mealIDs, "Meal IDs should not be null");
    assertTrue(mealIDs.length > 0, "There should be at least one meal ID");

    assertEquals(mealIDs.length, 5, "Meals IDs should be equal to 5");

    assertEquals(mealIDs[2], "53011", "3rd Meal ID should match actual");
  }

  // Test the null case return
  @Test
  public void testParseMealIDFromMultiOnNull() throws IOException, JSONException {
    String jsonContent = new String(Files.readAllBytes(Paths.get(nullJSONPath)));

    // Instantiate Caller
    Caller caller = new Caller();

    assertThrows(
        JSONException.class,
        () -> {
          // This should throw JSONException because "meals" is not a JSONArray
          caller.parseMealIDFromMulti(jsonContent);
        });
  }

  @Test
  public void testParseCategory() throws IOException {
    String jsonContent = new String(Files.readAllBytes(Paths.get(catJSONPath)));

    // Instantiate Caller
    Caller caller = new Caller();
  }
}
