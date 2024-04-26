package edu.brown.cs.student.main.server;

import static org.junit.jupiter.api.Assertions.*;

import edu.brown.cs.student.main.server.storage.MockedUtilities;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MockFirebaseUtilitiesTest {
  private MockedUtilities mockFirebaseUtilities;

  @BeforeEach
  void setUp() {
    mockFirebaseUtilities = new MockedUtilities();
  }

  @Test
  void testAddIngredient() {
    String uid = "testUser";
    String collectionName = "ingredient_pantry";
    String ingredientName = "Sugar";

    mockFirebaseUtilities.addIngredient(uid, collectionName, ingredientName);

    Map<String, Map<String, Map<String, Object>>> userCollections =
        mockFirebaseUtilities.getUserData().get(uid);
    assertNotNull(userCollections);
    assertTrue(userCollections.containsKey(collectionName));
    assertTrue(userCollections.get(collectionName).containsKey(ingredientName));
    assertEquals("Sugar", userCollections.get(collectionName).get(ingredientName).get("name"));
  }

  @Test
  void testAddMultipleIngredients() {
    mockFirebaseUtilities.addIngredient("user1", "ingredient_pantry", "Flour");
    mockFirebaseUtilities.addIngredient("user1", "ingredient_pantry", "Sugar");

    assertEquals(
        2, mockFirebaseUtilities.getUserData().get("user1").get("ingredient_pantry").size());
    assertNotNull(
        mockFirebaseUtilities.getUserData().get("user1").get("ingredient_pantry").get("Flour"));
    assertNotNull(
        mockFirebaseUtilities.getUserData().get("user1").get("ingredient_pantry").get("Sugar"));
  }

  @Test
  void testAddIngredientsToDifferentCollections() {
    mockFirebaseUtilities.addIngredient("user1", "ingredient_pantry", "Salt");
    mockFirebaseUtilities.addIngredient("user1", "banned_ingredients", "Pepper");

    assertNotNull(
        mockFirebaseUtilities.getUserData().get("user1").get("ingredient_pantry").get("Salt"));
    assertNotNull(
        mockFirebaseUtilities.getUserData().get("user1").get("banned_ingredients").get("Pepper"));
  }
}
