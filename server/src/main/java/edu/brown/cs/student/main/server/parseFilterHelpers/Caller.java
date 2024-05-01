package edu.brown.cs.student.main.server.parseFilterHelpers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
      public String[] parseMealIDFromMulti(String json) {
        // Convert JSON string to JSON Object
        JSONObject obj = new JSONObject(json);
        JSONArray meals = obj.getJSONArray("meals");

          // get idMeal from each recipie
        String[] idMeals = new String[meals.length()];

        for (int i = 0; i < meals.length(); i++) {
                JSONObject meal = meals.getJSONObject(i);
                idMeals[i] = meal.getString("idMeal");
        }

        return idMeals;
    }
    public String[][] parse(String idArr) {
        // get idMeal from each recipe
        String[][] mealInfo = new String[idArr.length()][27];
        for (int i = 0; i < idArr.length(); i++) {
            //get the json from the query to pertaining mealId
            String json = ""; //TODO should be different json per meal
            //turn Json into object
            JSONObject obj = new JSONObject(json);
            //follow previous shape to enter singular object with all info, may be redundant but for function sake
            JSONArray meals = obj.getJSONArray("meals");
            JSONObject meal = meals.getJSONObject(0);

            for (int j = 0; j <20 ; j++) {
                mealInfo[i][j] = meal.getString("strIngredient" + j );
            }
            mealInfo[i][20] = meal.getString("idMeal" ); //id
            mealInfo[i][21] = meal.getString("strMeal" ); // name
            mealInfo[i][22] = meal.getString("strCategory" ); //category
            mealInfo[i][23] = meal.getString("strSource" ); // website link
            mealInfo[i][24] = meal.getString("strYoutube" ); // youtube
            mealInfo[i][25] = meal.getString("strMealThumb" ); // thumbnail
            mealInfo[i][26] = meal.getString("strInstructions" ); // instructions
        }
        return mealInfo;
    }

//    public String [] filter(String[][] full){
//      f
//
//      return null
//    }
//
//    public String[][] parseAllMeals(String[] ids, String[] ingredients) {
//    HttpClient client = HttpClient.newHttpClient();
//            for (String id : ids) {
//          String url = String.format("https://www.themealdb.com/api/json/v1/1/lookup.php?i=%s", id);
//          HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
//
//          try {
//            HttpResponse<String> response =
//                client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            System.out.println("Response for ID " + id + ": " + response.body());
//          } catch (IOException | InterruptedException e) {
//            System.out.println("Error during HTTP request for ID " + id);
//            e.printStackTrace();
//          }
//        };
//
//    return new String[][] {{"", ""}};
//  }
}
