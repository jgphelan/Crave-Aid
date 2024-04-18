package edu.brown.cs.student.main.server.storage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
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
  public void addPin(String uid, String pinId, double lat, double lng) {
    Firestore db = FirestoreClient.getFirestore();
    DocumentReference userRef = db.collection("users").document(uid);
    CollectionReference pins = userRef.collection("pins");
    Map<String, Object> pinData = Map.of("lat", lat, "lng", lng);
    pins.document(pinId).set(pinData);
  }

  @Override
  public List<Map<String, Object>> getPins(String uid) {
    List<Map<String, Object>> pinsList = new ArrayList<>();
    Firestore db = FirestoreClient.getFirestore();
    CollectionReference pins = db.collection("users").document(uid).collection("pins");
    try {
      pins.get()
          .get()
          .forEach(
              document -> {
                Map<String, Object> pinData = new HashMap<>();
                pinData.put("id", document.getId());
                pinData.putAll(document.getData());
                pinsList.add(pinData);
              });
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    return pinsList;
  }

  @Override
  public void clearPins(String uid) {
    Firestore db = FirestoreClient.getFirestore();
    CollectionReference pins = db.collection("users").document(uid).collection("pins");
    pins.listDocuments().forEach(document -> document.delete());
  }

  // clears the collections inside of a specific user.
  @Override
  public void clearUser(String uid) throws IllegalArgumentException {
    if (uid == null) {
      throw new IllegalArgumentException("removeUser: uid cannot be null");
    }
    try {
      // removes all pins data for user 'uid'
      clearPins(uid);
    } catch (Exception e) {
      System.err.println("Error removing user : " + uid);
      System.err.println(e.getMessage());
    }
  }
}
