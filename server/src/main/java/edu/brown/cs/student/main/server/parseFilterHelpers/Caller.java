package edu.brown.cs.student.main.server.parseFilterHelpers;


import java.io.IOException;
import java.util.List;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;


import edu.brown.cs.student.main.server.ingredientHandlers.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Caller {

    private final Moshi moshi = new Moshi.Builder().build();
    private final Type listType = Types.newParameterizedType(List.class, List.class, String.class);
    private final JsonAdapter<List<List<String>>> listJsonAdapter = this.moshi.adapter(this.listType);


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
    public static String[][] parse(String[] idArr, String[] ingredients) throws IOException {


        // get idMeal from each recipe
        String[][] mealInfo = new String[idArr.length][29];
        for (int i = 0; i < idArr.length; i++) {
            //get the json from the query to pertaining mealId



            // TODO check validity and try catch IOException
            String url = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=" + idArr[i];
            String json = Utils.fullApiResponseString(url);





            //turn Json into object
            JSONObject obj = new JSONObject(json);
            //follow previous shape to enter singular object with all info, may be redundant for functions sake
            JSONArray meals = obj.getJSONArray("meals");
            JSONObject meal = meals.getJSONObject(0);

            // loops over each ingredient field and adds it to corresponding array space
            int emptyCount = 0;
            int sharedCount = 0;

            for (int j = 0; j <20 ; j++) {
                String ing = meal.getString("strIngredient" + j);
                mealInfo[i][j] = ing;

                //get total ingredient count
                if (ing.isEmpty()) {
                    emptyCount++;
                }

                for (String x : ingredients) {
                    if (x.equals(ing)) {
                        sharedCount++;
                    }
                }
            }
            // pass individual recipe info to 2d array in respective row
            mealInfo[i][20] = meal.getString("idMeal" ); //id
            mealInfo[i][21] = meal.getString("strMeal" ); // name
            mealInfo[i][22] = meal.getString("strCategory" ); //category
            mealInfo[i][23] = meal.getString("strSource" ); // website link
            mealInfo[i][24] = meal.getString("strYoutube" ); // youtube
            mealInfo[i][25] = meal.getString("strMealThumb" ); // thumbnail
            mealInfo[i][26] = meal.getString("strInstructions" ); // instructions
            mealInfo[i][27] = Integer.toString(emptyCount); // number of total ingredients in recipe;
            mealInfo[i][28] = Integer.toString(sharedCount);  // number of ingredients in recipe and search;
        }
        return mealInfo;
  }






































//    /**
//     * Private helper method for setting up an HttpURLConnection connection with the provided URL
//     *
//     * @return an HttpURLConnection with the provided URL
//     * @param requestURL the URL which we want to set up a connection to
//     * @throws DatasourceException if API connection doesn't result in success
//     * @throws IOException so different callers can handle differently if needed.
//     */
//    private static HttpURLConnection connect(URL requestURL) throws DatasourceException, IOException {
//        URLConnection urlConnection = requestURL.openConnection();
//        if (!(urlConnection instanceof HttpURLConnection clientConnection))
//            throw new DatasourceException("unexpected: result of connection wasn't HTTP");
//        clientConnection.connect(); // GET
//        if (clientConnection.getResponseCode() != 200)
//            throw new DatasourceException(
//                    "unexpected: API connection not success status " + clientConnection.getResponseMessage());
//        return clientConnection;
//    }



//    private String fetchStateId(String state) throws Exception {
//        // create state map if undefined
//            URL requestURL = new URL("https", "api.census.gov", "/data/2010/dec/sf1?get=NAME&for=state:*");
//            HttpURLConnection clientConnection = connect(requestURL);
//            List<List<String>> statesFromJson = this.listJsonAdapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
//            Map<String, String> statesMap = new HashMap<>();
//            if (statesFromJson != null) {
//                for (List<String> stateStateId : statesFromJson) {
//                    // skips header
//                    if (!stateStateId.get(0).equals("NAME")) {
//                        statesMap.put(stateStateId.get(0), stateStateId.get(1));
//                    }
//                }
//
//                this.states = statesMap;
//            }
//        }
//
//        if (this.states == null) {
//            throw new DatasourceException("There was an issue fetching states. Please re-query.");
//        }
//
//        return stateId;
//    }
//






















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
