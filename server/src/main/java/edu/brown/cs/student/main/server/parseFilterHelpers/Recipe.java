package edu.brown.cs.student.main.server.parseFilterHelpers;

/**
 * mealInfo[i][20] = meal.getString("idMeal"); // id mealInfo[i][21] = meal.getString("strMeal"); //
 * name mealInfo[i][22] = meal.getString("strCategory"); // category mealInfo[i][23] =
 * meal.getString("strSource"); // website link mealInfo[i][24] = meal.getString("strYoutube"); //
 * youtube mealInfo[i][25] = meal.getString("strMealThumb"); // thumbnail mealInfo[i][26] =
 * meal.getString("strInstructions"); // instructions mealInfo[i][27] =
 * Integer.toString(emptyCount); // number of total ingredients in recipe; mealInfo[i][28] =
 * Integer.toString(sharedCount); // number of ingredients in recipe and search; }
 */
public class Recipe {

  private String[] ingredients;
  private String id;
  private String name;
  private String category;
  private String source;
  private String youtube;
  private String thumbnail;
  private String instructions;
  private String totalIngredients;
  private String sharedIngredients;

  // Constructors, getters, and setters
  public Recipe(
      String[] ingredients,
      String id,
      String name,
      String category,
      String source,
      String youtube,
      String thumbnail,
      String instructions,
      String totalIngredients,
      String sharedIngredients) {
    this.ingredients = ingredients;
    this.id = id;
    this.name = name;
    this.category = category;
    this.source = source;
    this.youtube = youtube;
    this.thumbnail = thumbnail;
    this.instructions = instructions;
    this.totalIngredients = totalIngredients;
    this.sharedIngredients = sharedIngredients;
  }

  public String[] getIngredients() {
    return ingredients;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCategory() {
    return category;
  }

  public String getSource() {
    return source;
  }

  public String getYoutube() {
    return youtube;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public String getInstructions() {
    return instructions;
  }

  public String getTotalIngredients() {
    return totalIngredients;
  }

  public String getSharedIngredients() {
    return sharedIngredients;
  }
}
