package edu.brown.cs.student.main.server.parseFilterHelpers;

/**
 * This class represents a recipe object, which contains all the information about a recipe that we
 * need to display it on the frontend.
 */
public class Recipe {

  // Fields
  private String[] ingredients;
  private String[] measurements;
  private String id;
  private String name;
  private String category;
  private String source;
  private String youtube;
  private String thumbnail;
  private String instructions;
  private String totalIngredients;
  private String sharedIngredients;

  // Constructors
  public Recipe(
      String[] ingredients,
      String[] measurements,
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
    this.measurements = measurements; // set measurements in constructor
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

  // Getters
  public String[] getIngredients() {
    return ingredients;
  }

  public String[] getMeasurements() {
    return measurements;
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
