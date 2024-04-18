package edu.brown.cs.student.main.server.storage;

import java.util.List;
import java.util.Map;

public interface StorageInterface {

  void addPin(String uid, String pinId, double lat, double lng);

  List<Map<String, Object>> getPins(String uid);

  void clearPins(String uid);

  void clearUser(String uid) throws InterruptedException;
}
