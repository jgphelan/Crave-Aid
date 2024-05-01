package edu.brown.cs.student.main.server;

import static org.junit.jupiter.api.Assertions.*;

import edu.brown.cs.student.main.server.storage.MockedUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MockedUtilitiesTest {
  private MockedUtilities mockedUtilities;

  @BeforeEach
  void setUp() {
    mockedUtilities = new MockedUtilities();
  }

  @Test
  void testClearAllIngredients() {
    String uid = "user1";
    String collectionName = "ingredient_pantry";
    mockedUtilities.addIngredient(uid, collectionName, "Sugar");
    mockedUtilities.addIngredient(uid, collectionName, "Salt");

    mockedUtilities.clearAllIngredients(uid, collectionName);
    assertTrue(mockedUtilities.getUserData().get(uid).get(collectionName).isEmpty());
    mockedUtilities.clearUser(uid);
  }

  @Test
  void testClearAllIngredientsFromNonExistentCollection() {
    String uid = "user2";
    String nonExistentCollection = "non_existent_collection";

    mockedUtilities.addIngredient(uid, "some_other_collection", "Pepper");
    mockedUtilities.clearAllIngredients(uid, nonExistentCollection);
    assertNull(mockedUtilities.getUserData().get(uid).get(nonExistentCollection));
    mockedUtilities.clearUser(uid);
  }

  @Test
  void testClearAllIngredientsFromNonExistentUser() {
    String uid = "non_existent_user";
    String collectionName = "ingredient_pantry";

    mockedUtilities.clearAllIngredients(uid, collectionName);
    assertNull(mockedUtilities.getUserData().get(uid));
    mockedUtilities.clearUser(uid);
  }

  @Test
  void testMultipleClearOperations() {
    String uid = "user3";
    String collectionName = "ingredient_pantry";
    mockedUtilities.addIngredient(uid, collectionName, "Sugar");
    mockedUtilities.addIngredient(uid, collectionName, "Salt");

    mockedUtilities.clearAllIngredients(uid, collectionName);
    assertTrue(mockedUtilities.getUserData().get(uid).get(collectionName).isEmpty());
    mockedUtilities.addIngredient(uid, collectionName, "Sugar");
    mockedUtilities.addIngredient(uid, collectionName, "Salt");
    mockedUtilities.clearAllIngredients(uid, collectionName);
    assertTrue(mockedUtilities.getUserData().get(uid).get(collectionName).isEmpty());
    mockedUtilities.clearUser(uid);
  }
}
