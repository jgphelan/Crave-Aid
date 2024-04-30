package edu.brown.cs.student.main.server;

import static org.junit.jupiter.api.Assertions.*;

import edu.brown.cs.student.main.server.storage.MockedUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestRemoveIngredients {
  private MockedUtilities mockFirebaseUtilities;

  @BeforeEach
  void setUp() {
    mockFirebaseUtilities = new MockedUtilities();
  }

  @Test
  void testRemoveIngredientSuccessfully() {
    String uid = "user1";
    String collectionName = "ingredient_pantry";
    String ingredientName = "Sugar";

    mockFirebaseUtilities.addIngredient(uid, collectionName, ingredientName);
    assertNotNull(
        mockFirebaseUtilities.getUserData().get(uid).get(collectionName).get(ingredientName));

    mockFirebaseUtilities.removeIngredient(uid, collectionName, ingredientName);
    assertFalse(
        mockFirebaseUtilities
            .getUserData()
            .get(uid)
            .get(collectionName)
            .containsKey(ingredientName));
    mockFirebaseUtilities.clearUser(uid);
  }

  @Test
  void testRemoveIngredientFromEmptyCollection() {
    String uid = "user2";
    String collectionName = "ingredient_pantry";
    String ingredientName = "Sugar";

    assertNull(mockFirebaseUtilities.getUserData().get(uid));

    mockFirebaseUtilities.removeIngredient(uid, collectionName, ingredientName);
    assertNull(mockFirebaseUtilities.getUserData().get(uid));
    mockFirebaseUtilities.clearUser(uid);
  }

  @Test
  void testRemoveNonExistentIngredient() {
    String uid = "user3";
    String collectionName = "ingredient_pantry";
    String existingIngredient = "Salt";
    String nonExistentIngredient = "Pepper";

    mockFirebaseUtilities.addIngredient(uid, collectionName, existingIngredient);
    assertNotNull(
        mockFirebaseUtilities.getUserData().get(uid).get(collectionName).get(existingIngredient));
    assertNull(
        mockFirebaseUtilities
            .getUserData()
            .get(uid)
            .get(collectionName)
            .get(nonExistentIngredient));

    mockFirebaseUtilities.removeIngredient(uid, collectionName, nonExistentIngredient);
    assertNotNull(
        mockFirebaseUtilities.getUserData().get(uid).get(collectionName).get(existingIngredient));
    assertNull(
        mockFirebaseUtilities
            .getUserData()
            .get(uid)
            .get(collectionName)
            .get(nonExistentIngredient));
    mockFirebaseUtilities.clearUser(uid);
  }
}
