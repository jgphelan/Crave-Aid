package edu.brown.cs.student.main.server.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockedUtilities implements StorageInterface {
  private final Map<String, Map<String, Map<String, Map<String, Object>>>> userData =
      new HashMap<>();

  public Map<String, Map<String, Map<String, Map<String, Object>>>> getUserData() {
    return userData;
  }

  @Override
  public void addIngredient(String uid, String collectionName, String ingredientName) {
    userData.putIfAbsent(uid, new HashMap<>());
    Map<String, Map<String, Map<String, Object>>> userCollections = userData.get(uid);
    userCollections.putIfAbsent(collectionName, new HashMap<>());
    Map<String, Map<String, Object>> collection = userCollections.get(collectionName);
    collection.put(ingredientName, Map.of("name", ingredientName));
  }

  @Override
  public void clearUser(String uid) {
    userData.remove(uid);
  }

  @Override
  public void addDocument(
      String uid, String collection_id, String doc_id, Map<String, Object> data) {
    // Mock implementation
  }

  @Override
  public List<Map<String, Object>> getCollection(String uid, String collection_id) {
    return new ArrayList<>();
  }
}
