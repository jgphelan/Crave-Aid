package edu.brown.cs.student.main.server.storage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/** StorageInterface defines the methods for a StorageInterface */
public interface StorageInterface {

  /**
   * Adds a document to a collection in Firestore.
   *
   * @param uid user's unique id
   * @param collection_id collection id
   * @param doc_id document id
   * @param data data to add to document
   */
  void addDocument(String uid, String collection_id, String doc_id, Map<String, Object> data);

  /**
   * Gets a collection in Firestore.
   *
   * @param uid user's unique id
   * @param collection_id collection id
   * @param doc_id document id
   * @return document data
   * @throws InterruptedException if thread is interrupted
   * @throws ExecutionException if execution fails
   */
  List<Map<String, Object>> getCollection(String uid, String collection_id)
      throws InterruptedException, ExecutionException;

  /**
   * clears user data
   *
   * @param uid user's unique id
   * @param collection_id collection id
   * @param doc_id document id
   */
  void clearUser(String uid) throws InterruptedException;

  /**
   * Adds an ingredient to a user's collection in Firestore.
   *
   * @param uid user's unique id
   * @param collectionName name of collection to add ingredient to
   * @param ingredientName name of ingredient to add
   */
  void addIngredient(String uid, String collectionName, String ingredientName);

  /**
   * Removes an ingredient from a user's collection in Firestore.
   *
   * @param uid user's unique id
   * @param collectionName name of collection to remove ingredient from
   * @param ingredientName name of ingredient to remove
   */
  public void removeIngredient(String uid, String collectionName, String ingredientName);

  /**
   * Gets all ingredients from a user's collection in Firestore.
   *
   * @param uid user's unique id
   * @param collectionName name of collection to get ingredients from
   * @return list of all ingredients in collection
   * @throws InterruptedException if thread is interrupted
   * @throws ExecutionException if execution fails
   */
  public List<String> getAllIngredients(String uid, String collectionName)
      throws InterruptedException, ExecutionException;

  /**
   * Clears ingredients in Firestore.
   *
   * @param uid user's unique id
   * @param collectionName name of collection to clear ingredients from
   */
  public void clearAllIngredients(String uid, String collectionName);
}
