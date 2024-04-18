package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.server.handlers.ClearUserHandler;
import edu.brown.cs.student.main.server.pinHandlers.AddPinHandler;
import edu.brown.cs.student.main.server.pinHandlers.ClearPinsHandler;
import edu.brown.cs.student.main.server.pinHandlers.GetPinsHandler;
import edu.brown.cs.student.main.server.redlining.redliningData.CachedRedlining;
import edu.brown.cs.student.main.server.redlining.redliningData.RedliningDataHandler;
import edu.brown.cs.student.main.server.redlining.redliningData.RedliningReal;
import edu.brown.cs.student.main.server.redlining.redliningSearch.RedliningSearchHandler;
import edu.brown.cs.student.main.server.storage.FirebaseUtilities;
import java.io.IOException;
import spark.Spark;

/**
 * A class for running ACS and redlining APIs. ACS and redlining APIs are cached in this
 * implementation with cached results held for 4 minutes.
 */
public class Server {
  static final int port = 3232;

  /** Constructor for the Server class. */
  public Server() {
    Spark.port(port);
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    // REDLINING DATA
    RedliningReal redliningSrc = new RedliningReal();
    Spark.get("/redliningData", new RedliningDataHandler(new CachedRedlining(redliningSrc, 20, 2)));

    // REDLINING SEARCHING
    Spark.get("/redliningSearch", new RedliningSearchHandler(redliningSrc));

    // PINS
    FirebaseUtilities firebase_utility = null;
    try {
      firebase_utility = new FirebaseUtilities();
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    Spark.get("/add-pin", new AddPinHandler(firebase_utility));
    Spark.get("/get-pins", new GetPinsHandler(firebase_utility));
    Spark.get("/clear-pins", new ClearPinsHandler(firebase_utility));

    // CLEARING USER (for e2e testing)
    Spark.get("/clear-user", new ClearUserHandler(firebase_utility));

    Spark.notFound(
        (request, response) -> {
          response.status(404); // Not Found
          System.out.println("ERROR");
          return "404 Not Found - The requested endpoint does not exist.";
        });
    Spark.init();
    Spark.awaitInitialization();
  }

  /**
   * The main method for running a cached server servicing the broadband, redliningData, and
   * redliningSearch endpoints.
   *
   * @param args unused
   */
  public static void main(String[] args) {
    new Server();
    System.out.println("Server started at http://localhost:" + port);
    System.out.println(
        """
        1. Call the redliningData endpoint to query redlining data for major U.S. cities in the 1930’s, sourced from Mapping Inequality:
        http://localhost:port/redliningData (to query all data)
        OR
        http://localhost:port/redliningData?minLat=Number&maxLat=Number&minLong=Number&maxLong=Number (to query data for regions enclosed by a specific bounding box of coordinates)

        2. Call the redliningSearch endpoint to search the redlining data for major U.S. cities in the 1930’s (from Mapping Inequality) for
        areas whose area descriptions contain a certain keyword:
        http://localhost:port/redliningSearch?keyword=String
        """);
  }
}
