package edu.brown.cs.student.main.server.ingredientHandlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.parseFilterHelpers.Recipe;
import edu.brown.cs.student.main.server.storage.FirebaseUtilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/*
 * UtilsIngredients class for helpers that are used in the ingredient handlers
 * such as converting a map to a JSON string, converting a 2D array of Strings
 * to a JSON string, making a GET request to a URL and returning the response as
 * a string, parsing the recipe data from a 2D array of Strings to a JSON string,
 * and getting all ingredients for a user from the Firebase database
 */
public class UtilsIngredients {

  /**
   * Converts a Map to a JSON string using Moshi
   *
   * @param map The map to convert to a JSON string
   * @return The JSON string representation of the map
   */
  public static String toMoshiJson(Map<String, Object> map) {
    // Setup Moshi
    Moshi moshi = new Moshi.Builder().build();
    // type for a map of Strings to Objects
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    // Create a JsonAdapter for the map
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    // Convert the map to a JSON string and return it
    return adapter.toJson(map);
  }

  /**
   * Converts a 2D array of Strings to a JSON string using Moshi, is not used in the final
   * implementation but is useful for testing as well as if a additional user wishes to use this
   * function in a different front end that requires a general 2D array of Strings as oppsed to a
   * list of Recipe objects
   *
   * @param data The 2D array of Strings to convert to a JSON string
   * @return The JSON string representation of the 2D array
   */
  public static String toJson2DArray(String[][] data) {
    Moshi moshi = new Moshi.Builder().build();
    // Define the type for a 2D array of Strings
    Type type2DArray = Types.arrayOf(Types.arrayOf(String.class));
    // Create a JsonAdapter for the 2D array
    JsonAdapter<String[][]> adapter = moshi.adapter(type2DArray);

    // Convert the 2D array to a JSON string and return it
    return adapter.toJson(data);
  }

  /**
   * Makes a GET request to the given URL and returns the response as a string This function is used
   * to get the recipe data from the API
   *
   * @param urlStr The URL to make the GET request to
   * @return The response from the GET request as a string
   * @throws IOException If an error occurs while making the GET request
   * @throws RuntimeException If the response code is not 200
   */
  public static String fullApiResponseString(String urlStr) throws IOException, RuntimeException {
    URL url = new URL(urlStr);
    // Open a connection to the URL
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    // Set the request method to GET
    connection.setRequestMethod("GET");
    // Set the request property to accept JSON
    connection.setRequestProperty("Accept", "application/json"); // from documentation

    // Make sure response is not a failure
    if (connection.getResponseCode() != 200) {
      throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
    }

    // Read the response from the connection into a input streamreader
    // that is used to create a new buffered reader
    BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
    // using this new data structure to store the response data as a string
    // because it is mutable and specifically adapted to work with api responses
    // according to documentation
    StringBuilder response = new StringBuilder();
    String output;
    // Read the response line by line and append it to the response string
    while ((output = br.readLine()) != null) {
      response.append(output);
    }
    // Close the connection and return the response as a string
    connection.disconnect();
    return response.toString();
  }

  /**
   * Parses the recipe data from the 2D array of Strings to a JSON string that can be sent to the
   * front end and easily deserialized
   *
   * @param recipesData The 2D array of Strings to parse, for this implementation rows are recipes
   *     and columns are attributes of the recipe
   * @return The JSON string representation of the recipe data for this implementation the JSON
   *     string is a list of Recipe objects
   */
  public static String parseRecipe(String[][] recipesData) {

    // Convert to List of Recipe objects
    List<Recipe> recipes = new ArrayList<>();
    // Create a map of the recipes that will be used to get a specific recipe by number
    HashMap<String, Recipe> recipesMap = new HashMap<String, Recipe>();

    // i is used to keep track of the recipe number
    int i = 0;
    // Loop through the recipe data and create a new Recipe object for each recipe
    for (String[] recipeData : recipesData) {
      // Create a new Recipe object with the recipe data since each column is an attribute of the
      // recipe constructor
      String[] ingredients = java.util.Arrays.copyOfRange(recipeData, 0, 20);
      String[] measurements = java.util.Arrays.copyOfRange(recipeData, 29, 49);
      String id = recipeData[20];
      String name = recipeData[21];
      String category = recipeData[22];
      String source = recipeData[23];
      String youtube = recipeData[24];
      String thumbnail = recipeData[25];
      String instructions = recipeData[26];
      int totalIngredients = Integer.parseInt(recipeData[27]);
      int sharedIngredients = Integer.parseInt(recipeData[28]);
      Recipe rec =
          new Recipe(
              ingredients,
              measurements,
              id,
              name,
              category,
              source,
              youtube,
              thumbnail,
              instructions,
              totalIngredients,
              sharedIngredients);
      // Add the recipe to the list of recipes
      recipes.add(rec);

      // Add the recipe to the map of recipes (no longer using this in the
      // final implementation but left in for possible future use in upgrading
      // the efficiency of the code)
      recipesMap.put("Recipe" + i, rec);
      i++;
    }

    recipes.sort(
        (r1, r2) -> {
          if (r1.getSharedIngredients() != r2.getSharedIngredients()) {
            return Integer.compare(r2.getSharedIngredients(), r1.getSharedIngredients());
          } else {
            return Integer.compare(r1.getTotalIngredients(), r2.getTotalIngredients());
          }
        });

    // Setup Moshi
    Moshi moshi = new Moshi.Builder().build();
    // Define the type for a list of Recipe objects
    Type recipeListType = Types.newParameterizedType(List.class, Recipe.class);
    // Create a JsonAdapter for the list of Recipe objects
    JsonAdapter<List<Recipe>> jsonAdapter = moshi.adapter(recipeListType);

    // Convert to JSON string and return
    return jsonAdapter.toJson(recipes);
  }

  /**
   * Gets all ingredients for a user from the Firebase database
   *
   * @param uid The user id to get the ingredients for
   * @return A list of all ingredients for the user
   * @throws IOException If an error occurs while getting the ingredients
   */
  public static List<String> getAllIngredientsForUser(String uid) throws IOException {
    FirebaseUtilities firebaseUtilities = new FirebaseUtilities();
    try {
      // Get all ingredients from the user's pantry
      return firebaseUtilities.getAllIngredients(uid, "pantry");
      // Catch InterruptedException exceptions and return null to be checked for
      // in relevant code
    } catch (InterruptedException e) {
      e.printStackTrace();
      return null;
      // Catch ExecutionException exceptions and return null to be checked for
      // in relevant code
    } catch (ExecutionException e) {
      e.printStackTrace();
      return null;
    }
  }
}
