package edu.brown.cs.student.main.server.MealDBParsingTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.brown.cs.student.main.server.ingredientHandlers.UtilsIngredients;
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

//  @Test
//  public void testParseMealIDFromMulti() throws IOException {
//    String jsonContent = new String(Files.readAllBytes(Paths.get(nullJSONPath)));
//
//    // Invoke the method to be tested
//    String[] mealIDs = Caller.parseMealIDFromMulti(jsonContent);
//
//    assertNotNull(mealIDs, "Meal IDs should not be null");
//    assertTrue(mealIDs.length > 0, "There should be at least one meal ID");
//
//    assertEquals(mealIDs.length, 5, "Meals IDs should be equal to 5");
//
//    assertEquals(mealIDs[2], "53011", "3rd Meal ID should match actual");
//  }

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

//  @Test
//  public void testParseSingle() {
//    String[] idArr = {"53049"};
//    String[] ingredients = {"Salt"};
//    try {
//      String[][] mealInfo = Caller.parse(null, idArr, ingredients, null);
//      assertEquals(mealInfo[0][0], "Milk");
//      assertEquals(mealInfo[0][1], "Oil");
//      assertEquals(mealInfo[0][2], "Eggs");
//      assertEquals(mealInfo[0][3], "Flour");
//      assertEquals(mealInfo[0][4], "Baking Powder");
//      assertEquals(mealInfo[0][5], "Salt");
//      assertEquals(mealInfo[0][6], "Unsalted Butter");
//      assertEquals(mealInfo[0][7], "Sugar");
//      assertEquals(mealInfo[0][8], "Peanut Butter");
//      assertEquals(mealInfo[0][9], "");
//      assertEquals(mealInfo[0][20], "53049");
//      assertEquals(mealInfo[0][21], "Apam balik");
//      assertEquals(mealInfo[0][22], "Dessert");
//      assertEquals(
//          mealInfo[0][23], "https://www.nyonyacooking.com/recipes/apam-balik~SJ5WuvsDf9WQ");
//      assertEquals(mealInfo[0][24], "https://www.youtube.com/watch?v=6R8ffRRJcrg");
//      assertEquals(
//          mealInfo[0][25], "https://www.themealdb.com/images/media/meals/adxcbq1619787919.jpg");
//      assertEquals(
//          mealInfo[0][26],
//          "Mix milk, oil and egg together. Sift flour, baking powder and salt into the mixture. Stir well until all ingredients are combined evenly.\r\n\r\nSpread some batter onto the pan. Spread a thin layer of batter to the side of the pan. Cover the pan for 30-60 seconds until small air bubbles appear.\r\n\r\nAdd butter, cream corn, crushed peanuts and sugar onto the pancake. Fold the pancake into half once the bottom surface is browned.\r\n\r\nCut into wedges and best eaten when it is warm.");
//      assertEquals(mealInfo[0][27], "9");
//      assertEquals(mealInfo[0][28], "1");
//
//    } catch (IOException e) {
//      assertTrue(false);
//    }
//  }

//  @Test
//  public void testParseDoubleTwoShared() {
//    String[] idArr = {"53049", "53016"};
//    String[] ingredients = {"Salt", "Milk"};
//    try {
//      String[][] mealInfo = Caller.parse(null, idArr, ingredients, null);
//      assertEquals(mealInfo[0][0], "Milk");
//      assertEquals(mealInfo[0][1], "Oil");
//      assertEquals(mealInfo[0][2], "Eggs");
//      assertEquals(mealInfo[0][3], "Flour");
//      assertEquals(mealInfo[0][4], "Baking Powder");
//      assertEquals(mealInfo[0][5], "Salt");
//      assertEquals(mealInfo[0][6], "Unsalted Butter");
//      assertEquals(mealInfo[0][7], "Sugar");
//      assertEquals(mealInfo[0][8], "Peanut Butter");
//      assertEquals(mealInfo[0][9], "");
//      assertEquals(mealInfo[0][20], "53049");
//      assertEquals(mealInfo[0][21], "Apam balik");
//      assertEquals(mealInfo[0][22], "Dessert");
//      assertEquals(
//          mealInfo[0][23], "https://www.nyonyacooking.com/recipes/apam-balik~SJ5WuvsDf9WQ");
//      assertEquals(mealInfo[0][24], "https://www.youtube.com/watch?v=6R8ffRRJcrg");
//      assertEquals(
//          mealInfo[0][25], "https://www.themealdb.com/images/media/meals/adxcbq1619787919.jpg");
//      assertEquals(
//          mealInfo[0][26],
//          "Mix milk, oil and egg together. Sift flour, baking powder and salt into the mixture. Stir well until all ingredients are combined evenly.\r\n\r\nSpread some batter onto the pan. Spread a thin layer of batter to the side of the pan. Cover the pan for 30-60 seconds until small air bubbles appear.\r\n\r\nAdd butter, cream corn, crushed peanuts and sugar onto the pancake. Fold the pancake into half once the bottom surface is browned.\r\n\r\nCut into wedges and best eaten when it is warm.");
//      assertEquals(mealInfo[0][27], "9");
//      assertEquals(mealInfo[0][28], "2");
//
//      assertEquals(mealInfo[1][0], "Chicken Breast");
//      assertEquals(mealInfo[1][1], "Pickle Juice");
//      assertEquals(mealInfo[1][2], "Egg");
//      assertEquals(mealInfo[1][3], "Milk");
//      assertEquals(mealInfo[1][4], "Flour");
//      assertEquals(mealInfo[1][5], "Icing Sugar");
//      assertEquals(mealInfo[1][6], "Paprika");
//      assertEquals(mealInfo[1][7], "Salt");
//      assertEquals(mealInfo[1][8], "Black Pepper");
//      assertEquals(mealInfo[1][9], "Garlic Powder");
//      assertEquals(mealInfo[1][10], "Celery Salt");
//      assertEquals(mealInfo[1][11], "Cayenne Pepper");
//      assertEquals(mealInfo[1][12], "Olive Oil");
//      assertEquals(mealInfo[1][13], "Sesame Seed Burger Buns");
//      assertEquals(mealInfo[1][14], "");
//
//      assertEquals(mealInfo[1][20], "53016");
//      assertEquals(mealInfo[1][21], "Chick-Fil-A Sandwich");
//      assertEquals(mealInfo[1][22], "Chicken");
//      assertEquals(mealInfo[1][23], "https://hilahcooking.com/chick-fil-a-copycat/");
//      assertEquals(mealInfo[1][24], "https://www.youtube.com/watch?v=1WDesu7bUDM");
//      assertEquals(
//          mealInfo[1][25], "https://www.themealdb.com/images/media/meals/sbx7n71587673021.jpg");
//      assertEquals(
//          mealInfo[1][26],
//          "Wrap the chicken loosely between plastic wrap and pound gently with the flat side of a meat tenderizer until about 1/2 inch thick all around.\r\nCut into two pieces, as even as possible.\r\nMarinate in the pickle juice for 30 minutes to one hour (add a teaspoon of Tabasco sauce now for a spicy sandwich).\r\nBeat the egg with the milk in a bowl.\r\nCombine the flour, sugar, and spices in another bowl.\r\nDip the chicken pieces each into the egg on both sides, then coat in flour on both sides.\r\nHeat the oil in a skillet (1/2 inch deep) to about 345-350.\r\nFry each cutlet for 2 minutes on each side, or until golden and cooked through.\r\nBlot on paper and serve on toasted buns with pickle slices.");
//      assertEquals(mealInfo[1][27], "14");
//      assertEquals(mealInfo[1][28], "2");
//    } catch (IOException e) {
//      assertTrue(false);
//    }
//  }

//  @Test
//  public void testFullApiResponseStringWithMultiandParse() {
//
//    try {
//      String ingredients = "salt,milk,eggs,oil";
//      String[] ingredientArray = ingredients.split(",");
//      String url = "https://www.themealdb.com/api/json/v2/9973533/filter.php?i=" + ingredients;
//      String json = UtilsIngredients.fullApiResponseString(url);
//      String[] idArr = Caller.parseMealIDFromMulti(json);
//      String[][] mealInfo = Caller.parse(null, idArr, ingredientArray, null);
//
//      assertEquals(mealInfo[0][0], "Milk");
//      assertEquals(mealInfo[0][1], "Oil");
//      assertEquals(mealInfo[0][2], "Eggs");
//      assertEquals(mealInfo[0][3], "Flour");
//      assertEquals(mealInfo[0][4], "Baking Powder");
//      assertEquals(mealInfo[0][5], "Salt");
//      assertEquals(mealInfo[0][6], "Unsalted Butter");
//      assertEquals(mealInfo[0][7], "Sugar");
//      assertEquals(mealInfo[0][8], "Peanut Butter");
//      assertEquals(mealInfo[0][9], "");
//
//      assertEquals(mealInfo[0][20], "53049");
//      assertEquals(mealInfo[0][21], "Apam balik");
//      assertEquals(mealInfo[0][22], "Dessert");
//      assertEquals(
//          mealInfo[0][23], "https://www.nyonyacooking.com/recipes/apam-balik~SJ5WuvsDf9WQ");
//      assertEquals(mealInfo[0][24], "https://www.youtube.com/watch?v=6R8ffRRJcrg");
//      assertEquals(
//          mealInfo[0][25], "https://www.themealdb.com/images/media/meals/adxcbq1619787919.jpg");
//      assertEquals(
//          mealInfo[0][26],
//          "Mix milk, oil and egg together. Sift flour, baking powder and salt into the mixture. Stir well until all ingredients are combined evenly.\r\n\r\nSpread some batter onto the pan. Spread a thin layer of batter to the side of the pan. Cover the pan for 30-60 seconds until small air bubbles appear.\r\n\r\nAdd butter, cream corn, crushed peanuts and sugar onto the pancake. Fold the pancake into half once the bottom surface is browned.\r\n\r\nCut into wedges and best eaten when it is warm.");
//      assertEquals(mealInfo[0][27], "9");
//      assertEquals(mealInfo[0][28], "4");
//
//      assertEquals(mealInfo[1][0], "Water");
//      assertEquals(mealInfo[1][1], "Yeast");
//      assertEquals(mealInfo[1][2], "Sugar");
//      assertEquals(mealInfo[1][3], "Milk");
//      assertEquals(mealInfo[1][4], "Butter");
//      assertEquals(mealInfo[1][5], "Eggs");
//      assertEquals(mealInfo[1][6], "Salt");
//      assertEquals(mealInfo[1][7], "Flour");
//      assertEquals(mealInfo[1][8], "Oil");
//      assertEquals(mealInfo[1][9], "Lemon");
//      assertEquals(mealInfo[1][10], "Sugar");
//      assertEquals(mealInfo[1][11], "Cinnamon");
//      assertEquals(mealInfo[1][12], "");
//      assertEquals(mealInfo[1][13], "");
//      assertEquals(mealInfo[1][14], "");
//
//      assertEquals(mealInfo[1][20], "52928");
//      assertEquals(mealInfo[1][21], "BeaverTails");
//      assertEquals(mealInfo[1][22], "Dessert");
//      assertEquals(mealInfo[1][23], "https://www.tastemade.com/videos/beavertails");
//      assertEquals(mealInfo[1][24], "https://www.youtube.com/watch?v=2G07UOqU2e8");
//      assertEquals(
//          mealInfo[1][25], "https://www.themealdb.com/images/media/meals/ryppsv1511815505.jpg");
//      assertEquals(mealInfo[1][27], "12");
//      assertEquals(mealInfo[1][28], "4");
//    } catch (IOException e) {
//      assertTrue(false);
//    }
//  }

//  @Test
//  public void testtestFullApiResponseStringWithMultiandParsAndToJson2DArray() {
//    String flattened = "nope";
//    try {
//      String ingredients = "salt,milk,eggs,oil";
//      String[] ingredientArray = ingredients.split(",");
//      String url = "https://www.themealdb.com/api/json/v2/9973533/filter.php?i=" + ingredients;
//      String json = UtilsIngredients.fullApiResponseString(url);
//      String[] idArr = Caller.parseMealIDFromMulti(json);
//      String[][] mealInfo = Caller.parse(null, idArr, ingredientArray, null);
//      flattened = UtilsIngredients.toJson2DArray(mealInfo);
//
//      System.out.print("Hello \n\n\n" + flattened + "\n\n\n");
//      String compare =
//          "[[\"Milk\",\"Oil\",\"Eggs\",\"Flour\",\"Baking Powder\",\"Salt\",\"Unsalted Butter\",\"Sugar\",\"Peanut Butter\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"53049\",\"Apam balik\",\"Dessert\",\"https://www.nyonyacooking.com/recipes/apam-balik~SJ5WuvsDf9WQ\",\"https://www.youtube.com/watch?v=6R8ffRRJcrg\",\"https://www.themealdb.com/images/media/meals/adxcbq1619787919.jpg\",\"Mix milk, oil and egg together. Sift flour, baking powder and salt into the mixture. Stir well until all ingredients are combined evenly.\\r\\n"
//              + //
//              "\\r\\n"
//              + //
//              "Spread some batter onto the pan. Spread a thin layer of batter to the side of the pan. Cover the pan for 30-60 seconds until small air bubbles appear.\\r\\n"
//              + //
//              "\\r\\n"
//              + //
//              "Add butter, cream corn, crushed peanuts and sugar onto the pancake. Fold the pancake into half once the bottom surface is browned.\\r\\n"
//              + //
//              "\\r\\n"
//              + //
//              "Cut into wedges and best eaten when it is warm.\",\"9\",\"4\"],[\"Water\",\"Yeast\",\"Sugar\",\"Milk\",\"Butter\",\"Eggs\",\"Salt\",\"Flour\",\"Oil\",\"Lemon\",\"Sugar\",\"Cinnamon\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"52928\",\"BeaverTails\",\"Dessert\",\"https://www.tastemade.com/videos/beavertails\",\"https://www.youtube.com/watch?v=2G07UOqU2e8\",\"https://www.themealdb.com/images/media/meals/ryppsv1511815505.jpg\",\"In the bowl of a stand mixer, add warm water, a big pinch of sugar and yeast. Allow to sit until frothy.\\r\\n"
//              + //
//              "Into the same bowl, add 1/2 cup sugar, warm milk, melted butter, eggs and salt, and whisk until combined.\\r\\n"
//              + //
//              "Place a dough hook on the mixer, add the flour with the machine on, until a smooth but slightly sticky dough forms.\\r\\n"
//              + //
//              "Place dough in a bowl, cover with plastic wrap, and allow to proof for 1 1/2 hours.\\r\\n"
//              + //
//              "Cut dough into 12 pieces, and roll out into long oval-like shapes about 1/4 inch thick that resemble a beaver’s tail.\\r\\n"
//              + //
//              "In a large, deep pot, heat oil to 350 degrees. Gently place beavertail dough into hot oil and cook for 30 to 45 seconds on each side until golden brown.\\r\\n"
//              + //
//              "Drain on paper towels, and garnish as desired. Toss in cinnamon sugar, in white sugar with a squeeze of lemon, or with a generous slathering of Nutella and a handful of toasted almonds. Enjoy!\",\"12\",\"4\"]]";
//
//      assertEquals(flattened, compare);
//
//    } catch (IOException e) {
//      assertTrue(false);
//    }
//  }
}
