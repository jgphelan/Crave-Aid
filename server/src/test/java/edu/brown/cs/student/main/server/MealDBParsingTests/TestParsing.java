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

    // Invoke the method to be tested
    String[] mealIDs = Caller.parseMealIDFromMulti(jsonContent);

    // Example assertion (adjust as necessary based on expected output)
    assertNotNull(mealIDs, "Meal IDs should not be null");
    assertTrue(mealIDs.length > 0, "There should be at least one meal ID");
    assertEquals("52772", mealIDs[0], "The first meal ID should match the expected value");
  }

  @Test
  public void testParseMealIDFromMulti() throws IOException {
    String jsonContent = new String(Files.readAllBytes(Paths.get(nullJSONPath)));

    // Invoke the method to be tested
    String[] mealIDs = Caller.parseMealIDFromMulti(jsonContent);

    assertNotNull(mealIDs, "Meal IDs should not be null");
    assertTrue(mealIDs.length > 0, "There should be at least one meal ID");

    assertEquals(mealIDs.length, 5, "Meals IDs should be equal to 5");

    assertEquals(mealIDs[2], "53011", "3rd Meal ID should match actual");
  }

  // Test the null case return
  @Test
  public void testParseMealIDFromMultiOnNull() throws IOException, JSONException {
    String jsonContent = new String(Files.readAllBytes(Paths.get(nullJSONPath)));

    assertThrows(
        JSONException.class,
        () -> {
          // This should throw JSONException because "meals" is not a JSONArray
          Caller.parseMealIDFromMulti(jsonContent);
        });
  }

  @Test
  public void testParseSingle() {
    String[] idArr = {"53049"};
    String[] ingredients = {"Salt"};
    try {
      String[][] mealInfo = Caller.parse(idArr, ingredients);
      assertEquals(mealInfo[0][0], "Milk");
      assertEquals(mealInfo[0][1], "Oil");
      assertEquals(mealInfo[0][2], "Eggs");
      assertEquals(mealInfo[0][3], "Flour");
      assertEquals(mealInfo[0][4], "Baking Powder");
      assertEquals(mealInfo[0][5], "Salt");
      assertEquals(mealInfo[0][6], "Unsalted Butter");
      assertEquals(mealInfo[0][7], "Sugar");
      assertEquals(mealInfo[0][8], "Peanut Butter");
      assertEquals(mealInfo[0][9], "");
      assertEquals(mealInfo[0][20], "53049");
      assertEquals(mealInfo[0][21], "Apam balik");
      assertEquals(mealInfo[0][22], "Dessert");
      assertEquals(
          mealInfo[0][23], "https://www.nyonyacooking.com/recipes/apam-balik~SJ5WuvsDf9WQ");
      assertEquals(mealInfo[0][24], "https://www.youtube.com/watch?v=6R8ffRRJcrg");
      assertEquals(
          mealInfo[0][25], "https://www.themealdb.com/images/media/meals/adxcbq1619787919.jpg");
      assertEquals(
          mealInfo[0][26],
          "Mix milk, oil and egg together. Sift flour, baking powder and salt into the mixture. Stir well until all ingredients are combined evenly.\r\n\r\nSpread some batter onto the pan. Spread a thin layer of batter to the side of the pan. Cover the pan for 30-60 seconds until small air bubbles appear.\r\n\r\nAdd butter, cream corn, crushed peanuts and sugar onto the pancake. Fold the pancake into half once the bottom surface is browned.\r\n\r\nCut into wedges and best eaten when it is warm.");
      assertEquals(mealInfo[0][27], "9");
      assertEquals(mealInfo[0][28], "1");

    } catch (IOException e) {
      assertTrue(false);
    }
  }
}
