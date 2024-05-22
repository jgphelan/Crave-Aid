package edu.brown.cs.student.main.server.storage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * FirebaseUtilities holds all methods responsible for interactions with Firebase. Implements
 * StorageInterface. Class is specifically responsible for adding, removing, and getting ingredients
 * from Firebase.
 */
public class FirebaseUtilities implements StorageInterface {

  /**
   * Constructor for FirebaseUtilities. Initializes FirebaseApp with FirebaseOptions.
   *
   * @throws IOException if FirebaseOptions cannot be initialized.
   */
  @SuppressWarnings("deprecation")
  public FirebaseUtilities() throws IOException {
    // Create /resources/ folder with firebase_config.json and
    // add your admin SDK from Firebase. see:
    // https://docs.google.com/document/d/10HuDtBWjkUoCaVj_A53IFm5torB_ws06fW3KYFZqKjc/edit?usp=sharing
    String workingDirectory = System.getProperty("user.dir");
    Path firebaseConfigPath =
        Paths.get(workingDirectory, "src", "main", "resources", "firebase_config.json");
    // ^-- if your /resources/firebase_config.json exists but is not found,
    // try printing workingDirectory and messing around with this path.

    FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath.toString());

    FirebaseOptions options =
        new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();

    FirebaseApp.initializeApp(options);
  }

  /**
   * Adds an ingredient to a user's collection in Firebase.
   *
   * @param uid user's unique id
   * @param collectionName name of collection to add ingredient to
   * @param ingredientName name of ingredient to add
   */
  @Override
  public void addIngredient(String uid, String collectionName, String ingredientName) {
    Firestore db = FirestoreClient.getFirestore();
    CollectionReference collectionRef =
        db.collection("users").document(uid).collection(collectionName);
    Map<String, Object> ingredientData = Map.of("name", ingredientName);
    collectionRef.document(ingredientName).set(ingredientData);
  }

  /**
   * Removes an ingredient from a user's collection in Firebase.
   *
   * @param uid user's unique id
   * @param collectionName name of collection to remove ingredient from
   * @param ingredientName name of ingredient to remove
   */
  @Override
  public void removeIngredient(String uid, String collectionName, String ingredientName) {
    Firestore db = FirestoreClient.getFirestore();
    CollectionReference collectionRef =
        db.collection("users").document(uid).collection(collectionName);
    collectionRef.document(ingredientName).delete();
  }

  /**
   * Gets all ingredients from a user's collection in Firebase.
   *
   * @param uid user's unique id
   * @param collectionName name of collection to get ingredients from
   * @return list of all ingredients in collection
   * @throws InterruptedException if thread is interrupted
   * @throws ExecutionException if execution fails
   */
  @Override
  public List<String> getAllIngredients(String uid, String collectionName)
      throws InterruptedException, ExecutionException {
    Firestore db = FirestoreClient.getFirestore();
    CollectionReference collectionRef =
        db.collection("users").document(uid).collection(collectionName);
    List<String> ingredients = new ArrayList<>();
    List<QueryDocumentSnapshot> documents = collectionRef.get().get().getDocuments();
    for (DocumentSnapshot document : documents) {
      ingredients.add(document.getId());
    }
    return ingredients;
  }

  /**
   * Clears all ingredients from a user's collection in Firebase.
   *
   * @param uid user's unique id
   * @param collectionName name of collection to clear ingredients from
   */
  @Override
  public void clearAllIngredients(String uid, String collectionName) {
    Firestore db = FirestoreClient.getFirestore();
    CollectionReference collectionRef =
        db.collection("users").document(uid).collection(collectionName);
    collectionRef.listDocuments().forEach(DocumentReference::delete);
  }

  /**
   * clears the collections inside of a specific user.
   *
   * @param uid user's unique id
   * @throws IllegalArgumentException if uid is null
   */
  @Override
  public void clearUser(String uid) throws IllegalArgumentException {
    if (uid == null) {
      throw new IllegalArgumentException("removeUser: uid cannot be null");
    }
  }

  /**
   * Adds a document to a user's collection in Firebase.
   *
   * @param uid user's unique id
   * @param collection_id id of collection to add document to
   * @param doc_id id of document to add
   * @param data map of data to add to document
   * @throws IllegalArgumentException if uid, collection_id, doc_id, or data is null
   */
  @Override
  public void addDocument(String uid, String collection_id, String doc_id, Map<String, Object> data)
      throws IllegalArgumentException {
    if (uid == null || collection_id == null || doc_id == null || data == null) {
      throw new IllegalArgumentException(
          "addDocument: uid, collection_id, doc_id, or data cannot be null");
    }
    // adds a new document 'doc_name' to colleciton 'collection_id' for user 'uid'
    // with data payload 'data'.

    // use the guide below to implement this handler
    // - https://firebase.google.com/docs/firestore/quickstart#add_data

    Firestore db = FirestoreClient.getFirestore();
    // 1: Get a ref to the collection that you created
    CollectionReference collectionRef =
        db.collection("users").document(uid).collection(collection_id);

    // 2: Write data to the collection ref
    collectionRef.document(doc_id).set(data);
  }

  /**
   * gets all documents in the collection 'collection_id' for user 'uid
   *
   * @param uid user's unique id
   * @param collection_id id of collection to get document from
   * @param doc_id id of document to get
   * @return map of data from document
   * @throws InterruptedException if thread is interrupted
   * @throws ExecutionException if execution fails
   * @throws IllegalArgumentException if uid, collection_id, or doc_id is null
   */
  @Override
  public List<Map<String, Object>> getCollection(String uid, String collection_id)
      throws InterruptedException, ExecutionException, IllegalArgumentException {
    if (uid == null || collection_id == null) {
      throw new IllegalArgumentException("getCollection: uid and/or collection_id cannot be null");
    }

    Firestore db = FirestoreClient.getFirestore();
    // 1: Make the data payload to add to your collection
    CollectionReference dataRef = db.collection("users").document(uid).collection(collection_id);

    // 2: Get pin documents
    QuerySnapshot dataQuery = dataRef.get().get();

    // 3: Get data from document queries
    List<Map<String, Object>> data = new ArrayList<>();
    for (QueryDocumentSnapshot doc : dataQuery.getDocuments()) {
      data.add(doc.getData());
    }

    return data;
  }
}
