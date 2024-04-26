package edu.brown.cs.student.main.server.storage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
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

public class FirebaseUtilities implements StorageInterface {

  public FirebaseUtilities() throws IOException {
    // TODO: FIRESTORE PART 0:
    // Create /resources/ folder with firebase_config.json and
    // add your admin SDK from Firebase. see:
    // https://docs.google.com/document/d/10HuDtBWjkUoCaVj_A53IFm5torB_ws06fW3KYFZqKjc/edit?usp=sharing
    String workingDirectory = System.getProperty("user.dir");
    Path firebaseConfigPath =
        Paths.get(workingDirectory, "src", "server", "main", "resources", "firebase_config.json");
    // ^-- if your /resources/firebase_config.json exists but is not found,
    // try printing workingDirectory and messing around with this path.

    FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath.toString());

    FirebaseOptions options =
        new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();

    FirebaseApp.initializeApp(options);
  }

  @Override
  public void addIngredient(String uid, String collectionName, String ingredientName) {
    Firestore db = FirestoreClient.getFirestore();
    CollectionReference collectionRef =
        db.collection("users").document(uid).collection(collectionName);
    Map<String, Object> ingredientData = Map.of("name", ingredientName);
    collectionRef.document(ingredientName).set(ingredientData);
  }

  @Override
  public void removeIngredient(String uid, String collectionName, String ingredientName) {
    Firestore db = FirestoreClient.getFirestore();
    CollectionReference collectionRef =
        db.collection("users").document(uid).collection(collectionName);
    collectionRef.document(ingredientName).delete();
  }

  // @Override
  // public void addPin(String uid, String pinId, double lat, double lng) {
  //   Firestore db = FirestoreClient.getFirestore();
  //   DocumentReference userRef = db.collection("users").document(uid);
  //   CollectionReference pins = userRef.collection("pins");
  //   Map<String, Object> pinData = Map.of("lat", lat, "lng", lng);
  //   pins.document(pinId).set(pinData);
  // }

  // @Override
  // public List<Map<String, Object>> getPins(String uid) {
  //   List<Map<String, Object>> pinsList = new ArrayList<>();
  //   Firestore db = FirestoreClient.getFirestore();
  //   CollectionReference pins = db.collection("users").document(uid).collection("pins");
  //   try {
  //     pins.get()
  //         .get()
  //         .forEach(
  //             document -> {
  //               Map<String, Object> pinData = new HashMap<>();
  //               pinData.put("id", document.getId());
  //               pinData.putAll(document.getData());
  //               pinsList.add(pinData);
  //             });
  //   } catch (InterruptedException e) {
  //     e.printStackTrace();
  //   } catch (ExecutionException e) {
  //     e.printStackTrace();
  //   }
  //   return pinsList;
  // }

  // @Override
  // public void clearPins(String uid) {
  //   Firestore db = FirestoreClient.getFirestore();
  //   CollectionReference pins = db.collection("users").document(uid).collection("pins");
  //   pins.listDocuments().forEach(document -> document.delete());
  // }

  // clears the collections inside of a specific user.
  @Override
  public void clearUser(String uid) throws IllegalArgumentException {
    if (uid == null) {
      throw new IllegalArgumentException("removeUser: uid cannot be null");
    }
  }

  @Override
  public void addDocument(String uid, String collection_id, String doc_id, Map<String, Object> data)
      throws IllegalArgumentException {
    if (uid == null || collection_id == null || doc_id == null || data == null) {
      throw new IllegalArgumentException(
          "addDocument: uid, collection_id, doc_id, or data cannot be null");
    }
    // adds a new document 'doc_name' to colleciton 'collection_id' for user 'uid'
    // with data payload 'data'.

    // TODO: FIRESTORE PART 1:
    // use the guide below to implement this handler
    // - https://firebase.google.com/docs/firestore/quickstart#add_data

    Firestore db = FirestoreClient.getFirestore();
    // 1: Get a ref to the collection that you created
    CollectionReference collectionRef =
        db.collection("users").document(uid).collection(collection_id);

    // 2: Write data to the collection ref
    collectionRef.document(doc_id).set(data);
  }

  @Override
  public List<Map<String, Object>> getCollection(String uid, String collection_id)
      throws InterruptedException, ExecutionException, IllegalArgumentException {
    if (uid == null || collection_id == null) {
      throw new IllegalArgumentException("getCollection: uid and/or collection_id cannot be null");
    }
    // QUESTION TO TIM: should we make this an exercise too?

    // gets all documents in the collection 'collection_id' for user 'uid'

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
