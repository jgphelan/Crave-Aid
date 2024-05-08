package edu.brown.cs.student.main.server.parseFilterHelpers;

import edu.brown.cs.student.main.server.ingredientHandlers.UtilsIngredients;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;

public class Caller {

  // Takes in a json containing multiple items fitting multi ingredient choice
  /**
   * shape: { "meals": [ { "strMeal": "Chicken Fajita Mac and Cheese", "strMealThumb":
   * "https://www.themealdb.com/images/media/meals/qrqywr1503066605.jpg", "idMeal": "52818" }, {
   * "strMeal": "Chicken Ham and Leek Pie", "strMealThumb":
   * "https://www.themealdb.com/images/media/meals/xrrtss1511555269.jpg", "idMeal": "52875" } ] }
   */
  public static String[] parseMealIDFromMulti(String json) {
    // Convert JSON string to JSON Object
    JSONObject obj = new JSONObject(json);
    JSONArray meals = obj.getJSONArray("meals");

    // get idMeal from each recipe
    String[] idMeals = new String[meals.length()];

    for (int i = 0; i < meals.length(); i++) {
      JSONObject meal = meals.getJSONObject(i);
      idMeals[i] = meal.getString("idMeal");
    }

    return idMeals;
  }

  public static String[][] parse(
      String uid, String[] idArr, String[] ingredients, List<String> ingredientList)
      throws IOException {

    // get idMeal from each recipe
    String[][] mealInfo = new String[idArr.length][29];
    for (int i = 0; i < idArr.length; i++) {
      // get the json from the query to pertaining mealId

      String url = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=" + idArr[i];
      String json = UtilsIngredients.fullApiResponseString(url);

      // turn Json into object
      JSONObject obj = new JSONObject(json);
      // follow previous shape to enter singular object with all info, may be
      // redundant for
      // functions sake
      JSONArray meals = obj.getJSONArray("meals");
      JSONObject meal = meals.getJSONObject(0);

      // loops over each ingredient field and adds it to corresponding array space
      int emptyCount = 20;
      int sharedCount = 0;

      for (int j = 0; j < 20; j++) {

        try {
          String ing = meal.getString("strIngredient" + (j + 1)).trim();

          mealInfo[i][j] = ing;

          // get total ingredient count
          if (ing.isEmpty()) {
            emptyCount--;
          }

          if (isFoundInPantry(ing.toLowerCase(), uid, ingredientList)) {
            sharedCount++;
          }
        } catch (Exception e) {
          e.printStackTrace();
          mealInfo[i][j] = "";
          emptyCount--;
        }
      }
      // pass individual recipe info to 2d array in respective row
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
      mealInfo[i][27] = Integer.toString(emptyCount); // number of total ingredients in recipe;
      mealInfo[i][28] =
          Integer.toString(sharedCount); // number of ingredients in recipe and search;
    }
    return mealInfo;
  }

  public static boolean isFoundInPantry(String ingredient, String uid, List<String> ingredientList)
      throws Exception {
    List<String> lowerCaseIngredients =
        ingredientList.stream().map(String::toLowerCase).collect(Collectors.toList());
    return binarySearch(lowerCaseIngredients, ingredient.toLowerCase());
  }

  private static boolean binarySearch(List<String> sortedIngredients, String target) {
    int low = 0;
    int high = sortedIngredients.size() - 1;

    while (low <= high) {
      int mid = low + (high - low) / 2;
      String midVal = sortedIngredients.get(mid);

      if (midVal.compareTo(target) < 0) {
        low = mid + 1;
      } else if (midVal.compareTo(target) > 0) {
        high = mid - 1;
      } else {
        return true;
      }
    }

    return false;
  }
}
