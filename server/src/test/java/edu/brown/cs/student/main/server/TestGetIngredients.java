package edu.brown.cs.student.main.server;

import static org.junit.jupiter.api.Assertions.*;

import edu.brown.cs.student.main.server.storage.MockedUtilities;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestGetIngredients {
  private MockedUtilities mockFirebaseUtilities;

  @BeforeEach
  void setUp() {
    mockFirebaseUtilities = new MockedUtilities();
  }

  @Test
  void testGetAllIngredients() {
    String uid = "user1";
    String collectionName = "ingredient_pantry";
    mockFirebaseUtilities.addIngredient(uid, collectionName, "Sugar");
    mockFirebaseUtilities.addIngredient(uid, collectionName, "Salt");

    List<String> ingredients = mockFirebaseUtilities.getAllIngredients(uid, collectionName);
    assertEquals(2, ingredients.size());
    assertTrue(ingredients.contains("Sugar"));
    assertTrue(ingredients.contains("Salt"));
    mockFirebaseUtilities.clearUser(uid);
  }

  @Test
  void testGetAllIngredientsWithEmptyCollection() {
    String uid = "user2";
    String collectionName = "ingredient_pantry";

    List<String> ingredients = mockFirebaseUtilities.getAllIngredients(uid, collectionName);
    assertTrue(ingredients.isEmpty());
    mockFirebaseUtilities.clearUser(uid);
  }

  @Test
  void testGetAllIngredientsFromNonExistentCollection() {
    String uid = "user3";
    String collectionName = "non_existent_collection";

    List<String> ingredients = mockFirebaseUtilities.getAllIngredients(uid, collectionName);
    assertTrue(ingredients.isEmpty());
    mockFirebaseUtilities.clearUser(uid);
  }

  @Test
  void testGetAllIngredientsFromNonExistentUser() {
    String uid = "non_existent_user";
    String collectionName = "ingredient_pantry";

    List<String> ingredients = mockFirebaseUtilities.getAllIngredients(uid, collectionName);
    assertTrue(ingredients.isEmpty());
    mockFirebaseUtilities.clearUser(uid);
  }

  @Test
  void testClearIngredientsAndCheckList() {
    String uid = "user4";
    String collectionName = "ingredient_pantry";
    mockFirebaseUtilities.addIngredient(uid, collectionName, "Pepper");
    mockFirebaseUtilities.addIngredient(uid, collectionName, "Cinnamon");

    mockFirebaseUtilities.clearUser(uid);

    List<String> ingredients = mockFirebaseUtilities.getAllIngredients(uid, collectionName);
    assertTrue(ingredients.isEmpty());
    mockFirebaseUtilities.clearUser(uid);
  }
}
