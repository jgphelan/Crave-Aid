package edu.brown.cs.student.main.server.storage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface StorageInterface {

  void addDocument(String uid, String collection_id, String doc_id, Map<String, Object> data);

  List<Map<String, Object>> getCollection(String uid, String collection_id)
      throws InterruptedException, ExecutionException;

  void addPin(String uid, String pinId, double lat, double lng);

  List<Map<String, Object>> getPins(String uid);

  void clearPins(String uid);

  void clearUser(String uid) throws InterruptedException;

  void addIngredient(String uid, String collectionName, String ingredientName);
}
