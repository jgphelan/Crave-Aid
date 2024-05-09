package edu.brown.cs.student.main.server.parseFilterHelpers;

import edu.brown.cs.student.main.server.ingredientHandlers.UtilsIngredients;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The Caller class is responsible for calling the API and parsing the JSON response to get
 * information for the recipe.
 *
 * <p>This class is principally used in the IngredientHandlers package to get the recipe information
 * the two parse fucntions are specifically used in the recipecallhandler and the rest of the
 * functions are used in the ingredientHandlers package more generally.
 */
public class Caller {

  // Takes in a json containing multiple items fitting multi ingredient choice
  /**
   * shape: { "meals": [ { "strMeal": "Chicken Fajita Mac and Cheese", "strMealThumb":
   * "https://www.themealdb.com/images/media/meals/qrqywr1503066605.jpg", "idMeal": "52818" }, {
   * "strMeal": "Chicken Ham and Leek Pie", "strMealThumb":
   * "https://www.themealdb.com/images/media/meals/xrrtss1511555269.jpg", "idMeal": "52875" } ] }
   */

  /* Parses the meal ID from the JSON response which contains the ID for each
   * recipe in the JSON response.
   *
   * @param json The JSON response from the API call
   */
  /**
   * Parses the meal IDs from the JSON response which contains the ID for each recipe in the JSON
   * response.
   *
   * @param json The JSON response from the API call shape: { "meals": [ { "strMeal": "Chicken
   *     Fajita Mac and Cheese", "strMealThumb":
   *     "https://www.themealdb.com/images/media/meals/qrqywr1503066605.jpg", "idMeal": "52818" }, {
   *     "strMeal": "Chicken Ham and Leek Pie", "strMealThumb":
   *     "https://www.themealdb.com/images/media/meals/xrrtss1511555269.jpg", "idMeal": "52875" } ]
   *     }
   * @return An array of strings containing the meal ID for each recipe
   */
  public static String[] parseMealIDFromMulti(String json) {
    // Convert JSON string to JSON Object
    JSONObject obj = new JSONObject(json);
    // Get the meals array from the JSON object
    JSONArray meals = obj.getJSONArray("meals");

    // get idMeal from each recipe
    String[] idMeals = new String[meals.length()];

    // loop through each recipe and get the idMeal
    for (int i = 0; i < meals.length(); i++) {
      JSONObject meal = meals.getJSONObject(i);
      // get the idMeal and add it to the array
      idMeals[i] = meal.getString("idMeal");
    }

    // return the array of idMeals
    return idMeals;
  }

  /**
   * Parses the meal information from the 2D array of strings containing the information for each
   * recipe. Making API calls to theMealDB for every single one and consinuously calculating fields
   * such as numIngredients. The function also checks if the ingredient is found in the user's
   * pantry -> If the ingredient is found in the user's pantry, the sharedCount is incremented.
   *
   * @param uid The user ID of the user
   * @param idArr The array of meal IDs for each recipe
   * @param ingredients The array of ingredients for the user
   * @param firebaseUtilities The FirebaseUtilities object used to interact with the database
   * @return A 2D array of strings containing the information for each recipe where each row
   *     corresponds to a single recipe and each column corresponds to a different attribute of the
   *     recipe. The columns are as follows: 0-19: Ingredients, 20: ID, 21: Name, 22: Category, 23:
   *     Source, 24: Youtube, 25: Thumbnail, 26: Instructions, 27: Total Ingredients, 28: Shared
   *     Ingredients (Ingredients in the recipe that are also in the user's pantry).
   * @throws IOException If an error occurs while getting the ingredients
   */
  public static String[][] parse(
      String uid, String[] idArr, String[] ingredients, List<String> ingredientList)
      throws IOException {

    // get idMeal from each recipe
    String[][] mealInfo = new String[idArr.length][49];
    for (int i = 0; i < idArr.length; i++) {
      // get the json from the query to pertaining mealId

      // creates a url that will be used to make the query for an id in theMeadDB
      String url = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=" + idArr[i];
      // makes the query and gets the json
      String json = UtilsIngredients.fullApiResponseString(url);

      // turn Json into object
      JSONObject obj = new JSONObject(json);
      // follow previous shape to enter singular object with all info, may be
      // redundant for functions sake
      JSONArray meals = obj.getJSONArray("meals");
      JSONObject meal = meals.getJSONObject(0);

      // loops over each ingredient field and adds it to corresponding array space
      int emptyCount = 20;
      int sharedCount = 0;

      for (int j = 0; j < 20; j++) {
        try {
          // get ingredient
          String ing = meal.getString("strIngredient" + (j + 1)).trim();

          // add ingredient to array
          mealInfo[i][j] = ing;
          String measure = meal.getString("strMeasure" + (j + 1)).trim();
          mealInfo[i][j + 29] = measure;
          // get total ingredient count
          if (ing.isEmpty()) {
            // if ingredient is empty, decrement empty count
            emptyCount--;
          }
          if (isFoundInPantry(ing.toLowerCase(), uid, ingredientList)) {
            sharedCount++;
          }
        } catch (Exception e) {
          mealInfo[i][j] = "";
          mealInfo[i][j + 29] = "";
          emptyCount--;
        }
      }
      // pass individual recipe info to 2d array in respective row
      // again because of the nature of the API response, some fields may be null
      // but because now the getstring is different and so is the space into which
      // the string is being placed, the catch all is different for each field
      // could be refactored to be more concise using a helper function or even an
      // array of fields and a loop but this is more explicit and readable
      try {
        mealInfo[i][20] = meal.getString("idMeal"); // id
      } catch (Exception e) {
        mealInfo[i][20] = "";
      }
      try {
        mealInfo[i][21] = meal.getString("strMeal"); // name
      } catch (Exception e) {
        mealInfo[i][21] = "";
      }
      try {
        mealInfo[i][22] = meal.getString("strCategory"); // category
      } catch (Exception e) {
        mealInfo[i][22] = "";
      }
      try {
        mealInfo[i][23] = meal.getString("strSource"); // website link
      } catch (Exception e) {
        mealInfo[i][23] = "";
      }
      try {
        mealInfo[i][24] = meal.getString("strYoutube"); // youtube
      } catch (Exception e) {
        mealInfo[i][24] = "";
      }
      try {
        mealInfo[i][25] = meal.getString("strMealThumb"); // thumbnail
      } catch (Exception e) {
        mealInfo[i][25] = "";
      }
      try {
        mealInfo[i][26] = meal.getString("strInstructions"); // instructions
      } catch (Exception e) {
        mealInfo[i][26] = "";
      }
      // add total and shared ingredient count to array
      mealInfo[i][27] = Integer.toString(emptyCount); // number of total ingredients in recipe;
      mealInfo[i][28] =
          Integer.toString(sharedCount); // number of ingredients in recipe and search;
    }
    // return 2d array of recipe info
    return mealInfo;
  }

  /**
   * Checks if the ingredient is found in the user's pantry.
   *
   * @param ingredient The ingredient to check
   * @param uid The user ID of the user
   * @param firebaseUtilities The FirebaseUtilities object used to interact with the database
   * @return True if the ingredient is found in the user's pantry, false otherwise
   * @throws Exception If an error occurs while getting the ingredients
   */
  public static boolean isFoundInPantry(String ingredient, String uid, List<String> ingredientList)
      throws Exception {
    List<String> lowerCaseIngredients =
        ingredientList.stream().map(String::toLowerCase).collect(Collectors.toList());
    return binarySearch(lowerCaseIngredients, ingredient.toLowerCase());
  }

  /**
   * Checks if the ingredient is found in the user's pantry.
   *
   * @param ingredient The ingredient to check
   * @param uid The user ID of the user
   * @param firebaseUtilities The FirebaseUtilities object used to interact with the database
   * @return True if the ingredient is found in the user's pantry, false otherwise
   * @throws Exception If an error occurs while getting the ingredients
   */
  public static boolean binarySearch(List<String> sortedIngredients, String target) {
    int low = 0;
    int high = sortedIngredients.size() - 1;
    while (low <= high) {
      // calculate midpoint
      int mid = low + (high - low) / 2;
      // get mid value
      String midVal = sortedIngredients.get(mid);
      // compare mid val to target;
      if (midVal.compareTo(target) < 0) {
        // if mid val is less than target, set low to mid + 1
        low = mid + 1;
      } else if (midVal.compareTo(target) > 0) {
        // if mid val is greater than target, set high to mid - 1
        high = mid - 1;
      } else {
        // if mid val is equal to target, return true
        return true;
      }
    }
    // if target is not found, return false
    return false;
  }

  // Close the Caller class here
}
