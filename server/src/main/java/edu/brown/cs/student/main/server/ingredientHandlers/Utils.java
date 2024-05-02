package edu.brown.cs.student.main.server.ingredientHandlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Utils {

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
    connection.setRequestProperty("Accept", "application/json"); //from documentation

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


}

