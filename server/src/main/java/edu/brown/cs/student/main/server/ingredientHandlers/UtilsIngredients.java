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

public class UtilsIngredients {

  public static String toMoshiJson(Map<String, Object> map) {
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);

    return adapter.toJson(map);
  }

  public static String toJson2DArray(String[][] data) {
    Moshi moshi = new Moshi.Builder().build();
    // Define the type for a 2D array of Strings
    Type type2DArray = Types.arrayOf(Types.arrayOf(String.class));
    JsonAdapter<String[][]> adapter = moshi.adapter(type2DArray);

    return adapter.toJson(data);
  }

  public static String fullApiResponseString(String urlStr) throws IOException {
    URL url = new URL(urlStr);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    connection.setRequestProperty("Accept", "application/json"); // from documentation

    if (connection.getResponseCode() != 200) {
      throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
    }

    BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
    StringBuilder response = new StringBuilder();
    String output;
    while ((output = br.readLine()) != null) {
      response.append(output);
    }
    connection.disconnect();
    return response.toString();
  }

  public static String parseRecipe(String[][] recipesData) {

    // Convert to List of Recipe objects
    List<Recipe> recipes = new ArrayList<>();
    HashMap<String, Recipe> recipesMap = new HashMap<String, Recipe>();

    int i = 0;
    for (String[] recipeData : recipesData) {

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
      recipes.add(rec);

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
    Type recipeListType = Types.newParameterizedType(List.class, Recipe.class);
    JsonAdapter<List<Recipe>> jsonAdapter = moshi.adapter(recipeListType);

    // Convert to JSON
    return jsonAdapter.toJson(recipes);
  }

  public static List<String> getAllIngredientsForUser(String uid) throws IOException {
    FirebaseUtilities firebaseUtilities = new FirebaseUtilities();
    try {
      return firebaseUtilities.getAllIngredients(uid, "pantry");
    } catch (InterruptedException e) {
      e.printStackTrace();
      return null;
    } catch (ExecutionException e) {
      e.printStackTrace();
      return null;
    }
  }
}
