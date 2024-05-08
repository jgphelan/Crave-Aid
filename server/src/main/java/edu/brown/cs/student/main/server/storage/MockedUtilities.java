package edu.brown.cs.student.main.server.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MockedUtilities is a class that implements StorageInterface. It is used for testing purposes to
 * simulate the behavior of FirebaseUtilities without actually interacting with Firebase.
 */
public class MockedUtilities implements StorageInterface {
  private final Map<String, Map<String, Map<String, Map<String, Object>>>> userData =
      new HashMap<>();

  /**
   * Returns the userData map.
   *
   * @return userData map
   */
  public Map<String, Map<String, Map<String, Map<String, Object>>>> getUserData() {
    return userData;
  }

  /**
   * Adds an ingredient to a user's collection in the userData map.
   *
   * @param uid user's unique id
   * @param collectionName name of collection to add ingredient to
   * @param ingredientName name of ingredient to add
   */
  @Override
  public void addIngredient(String uid, String collectionName, String ingredientName) {
    // Add user if not present
    userData.putIfAbsent(uid, new HashMap<>());
    // Add collection if not present
    Map<String, Map<String, Map<String, Object>>> userCollections = userData.get(uid);
    userCollections.putIfAbsent(collectionName, new HashMap<>());
    // Add ingredient
    Map<String, Map<String, Object>> collection = userCollections.get(collectionName);
    collection.put(ingredientName, Map.of("name", ingredientName));
  }

  /**
   * Removes an ingredient from a user's collection in the userData map.
   *
   * @param uid user's unique id
   * @param collectionName name of collection to remove ingredient from
   * @param ingredientName name of ingredient to remove
   */
  @Override
  public void removeIngredient(String uid, String collectionName, String ingredientName) {
    // Remove ingredient if user and collection exist in userData
    if (userData.containsKey(uid) && userData.get(uid).containsKey(collectionName)) {
      userData.get(uid).get(collectionName).remove(ingredientName);
    }
  }

  /**
   * Gets all ingredients from a user's collection in the userData map.
   *
   * @param uid user's unique id
   * @param collectionName name of collection to get ingredients from
   * @return list of all ingredients in collection
   */
  @Override
  public List<String> getAllIngredients(String uid, String collectionName) {
    List<String> ingredients = new ArrayList<>();
    // Get ingredients if user and collection exist in userData
    if (userData.containsKey(uid) && userData.get(uid).containsKey(collectionName)) {
      ingredients.addAll(userData.get(uid).get(collectionName).keySet());
    }
    return ingredients;
  }

  /**
   * Clears all ingredients from a user's collection in the userData map.
   *
   * @param uid user's unique id
   * @param collectionName name of collection to clear ingredients from
   */
  @Override
  public void clearAllIngredients(String uid, String collectionName) {
    // Clear ingredients if user and collection exist in userData
    if (userData.containsKey(uid) && userData.get(uid).containsKey(collectionName)) {
      userData.get(uid).get(collectionName).clear();
    }
  }

  /**
   * Adds a user to the userData map.
   *
   * @param uid user's unique id
   */
  @Override
  public void clearUser(String uid) {
    userData.remove(uid);
  }

  /**
   * Adds a document to a collection in the userData map.
   *
   * @param uid user's unique id
   * @param collection_id id of collection to add document to
   * @param doc_id id of document to add
   * @param data data to add to document
   */
  @Override
  public void addDocument(
      String uid, String collection_id, String doc_id, Map<String, Object> data) {}

  /**
   * gets a document from a collection in the userData map.
   *
   * @param uid user's unique id
   * @param collection_id id of collection to get document from
   */
  @Override
  public List<Map<String, Object>> getCollection(String uid, String collection_id) {
    return new ArrayList<>();
  }
}
