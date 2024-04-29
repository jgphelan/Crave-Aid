package edu.brown.cs.student.main.server.favoriteWordHandlers.parseFilterHelpers;

public class Caller {
  // Takes in a json containing multiple items fitting multi ingredient choice
  /**
   * shape: { "meals": [ { "strMeal": "Chicken Fajita Mac and Cheese", "strMealThumb":
   * "https://www.themealdb.com/images/media/meals/qrqywr1503066605.jpg", "idMeal": "52818" }, {
   * "strMeal": "Chicken Ham and Leek Pie", "strMealThumb":
   * "https://www.themealdb.com/images/media/meals/xrrtss1511555269.jpg", "idMeal": "52875" } ] }
   */
  public String parseMealIDFromMulti(String[] ingredients) {

    // parse json
    // get idMeal
    // return idMeal
    return "0";
  }

  public String[] parseMealInfoFromID(String json) {
    // parse json
    // get strMeal
    // get strMealThumb
    // return [strMeal, strMealThumb]
    return new String[] {"", ""};
  }

  public String[][] parseAllMeals(String json) {
    // parse json
    // get all meals
    // return [ [strMeal, strMealThumb], [strMeal, strMealThumb], ... ]
    return new String[][] {{"", ""}};
  }
}
