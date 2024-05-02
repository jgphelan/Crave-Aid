package edu.brown.cs.student.main.server.externalHandlers;

import edu.brown.cs.student.main.server.ingredientHandlers.Utils;
import edu.brown.cs.student.main.server.parseFilterHelpers.Caller;

import edu.brown.cs.student.main.server.storage.FirebaseUtilities;
import spark.Request;
import spark.Response;
import spark.Route;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;


public class RecipeCallHandler implements Route {

    @Override
    public Object handle(Request req, Response res) {
        String ingredients = req.queryParams("ingredients"); //TODO
        //splits comma delineated ingredient list
        String[] ingredientArray = ingredients.split(",");


        if (ingredients == null) {
            res.status(400);
            return Utils.toMoshiJson(Map.of("status", "failure", "message", "Missing parameters"));
        }

        try {
            //TODO GET THE INITIAL JSON FROM CALL TO ALL RECIPES AND PASS THAT JSON INTO parseMealIDFromMulti
            String url = "https://www.themealdb.com/api/json/v2/9973533/filter.php?i=" + ingredients;
            String json = Utils.fullApiResponseString(url);


            String[] idArr = Caller.parseMealIDFromMulti("");
            String[][] infoArr = Caller.parse(idArr, ingredientArray);


            // Serialize the 2D array
            String jsonData = Utils.toJson2DArray(infoArr);

//            String idmeal =infoArr[i][20];
//            String strMeal= infoArr[i][21];
//            String strCategory= infoArr[i][22];
//            String strSource=infoArr[i][23];
//            String strYoutube=infoArr[i][24];
//            String strMealThumb=infoArr[i][25];
//            String strInstructions=infoArr[i][26];

            //TODO confirm correct
            return Utils.toMoshiJson(Map.of("status", "success", "data", jsonData));
        } catch (Exception e) {
            res.status(500);
            return Utils.toMoshiJson(Map.of("status", "failure", "message", e.getMessage()));
        }
    }

}
