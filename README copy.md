# Project Details

**Maps**  
**Team members:** Abigael Bousquet (@abousque) & Kole Newman (@knewman6)  
**Estimated Total Time:** 30ish hours between the two of us  
**Repo:** https://github.com/cs0320-s24/maps-abousque-knewman6

# Design Choices

## High Level Design Overview

### Back End (server/src)

The pinHandlers package contains classes related to marker pins that users can place on the map, their persistent storage with cookies in Firestore, and handler classes that handle systematic adding and clearing of these pins from Firestore for a user.

The redlining package contains classes related to the redlining data and the two different endpoints related to using this data. This package has 3 subpackages:

1. parser -- This package contains the gearup code for parsing the redlining geojson (fullData.geojson in server/data) into a GeoMapCollection.
2. redliningData -- This package contains the classes related to the redliningData endpoint. The RedliningDatasource interface describes what a datasource of redlining data should include; the CachedRedlining (proxy class wrapping another RedliningDatasource), RedliningReal (uses the real redlining data from fullDownload.json), and RedliningMocked (mocks redlining data for testing) classes implement this interface. This allows the RedliningDataHandler to not care precisely where the data is coming from and creates a great strategy pattern for the endpoint. RedliningDataHandler implements Route and is the handler class for the redliningData endpoint, which can query all the redlining data or a subset in a provided bounded box of coordinates.
3. redliningSearch -- This package contains the classes related to the redliningSearch ednpoint. RedliningSearch takes a RedliningDatasource (see above) and can search the redlining data to find all GeoMap objects whose area descriptions include a user-provided keyword. RedliningSearchHandler implements Route and handles the redliningSearch endpoint, which works with a RedliningSearch object.

The storage package contains the classes related to Firebase storage (Firestore) of user data (specifically pins for this project).

The handlers package contains handler classes for managing users and their cookies with Firestore. This is the logic for the clear-user endpoint, which is primarily for testing with a sample user.

Server.java contains the Server class, which starts a server at local port 3232 to service all of the endpoints described above. Server.java also contains the main method which officially starts the back end.

### Front End (client/src)

Components contains all of the components of the front end. This includes important files like App.tsx which is the topmost component of the UI, Mapbox.tsx which controls pins, overlay, and other functionality on the Mapbox element of the UI, REPLInput.tsx and REPLHistory.tsx which manage user input through the input box in the UI and resultant output respectively with React, REPL.tsx which wraps REPLInput and REPLOutput components, and MapsGearup.tsx which wraps both Mapbox and REPL components. Auth is a subfolder of components which contains the logic for Firebase authentication for login/logout UI functionality.

Styles ocntains all of the css files that contain style information for each part of the UI.

Utils contains utility files used by components to cleanly work with the UI. api.ts is used for interacting with API endpoints to connect front end logic to back end data. cookie.ts is used to manage user cookies with Firebase. overlay.ts is used to manage Mapbox overlays for the full redlining data as well as highlighted search results.

## Specific Design Choices

1. We chose to have the redlining APIs (redliningData and redliningSearch) return successful json responses of the form of a serialized GeoMapCollection rather than our original form of serialized Map of String to Object with "result": "success" and "data": serialized GeoMapCollection. While our final choice is not as clear for the caller as the serialized Map would have been (with things like restating caller-inputted arguments), we justified this with prioritizing functionality, given that our frontend was struggling to understand the nested json response and extract data from the serialized Map.
2. We chose our overlay "highlight" for keyword searches to be in a darker purple color with opacity 0.8 (higher than the general redlining data overlay). This color was very distinct from the redlining data grade colors which combined with a higher opacity makes the highlighted areas very clear to a frontend user.
3. We chose to cache bounding box queries to redliningData. This was mostly as a runtime optimization, with the bonus of counting towards a patterns badge. We chose to set up our cache in our Server with max 20 entries and a max hold time of 2 minutes to strike a balance between runtime optimization and also not creating a too-big, space-heavy cache given that many of the GeoMapCollections resulting from a bounding box search can be large.

# Errors/Bugs

Some of the pin-related playwright tests in particular are flaky between browsers and between runs. This is fairly expected per the handout though.

# Tests

Please see javadocs above tests in respective testing files for more complete descriptions of how each test verifies different parts of the program functionality.

As an overview:

- src\test\java\edu\brown\cs\student\main\server contains test classes in java that contain tests to (a) unit test data sources and backend classes and (b) test the integration of the aforementioned sources with the server via the handler classes. Some of the integration tests use mocked data whereas others use real data.
- client\tests\e2e contains test files in typscript with Playwright to test e2e functionality of the front end with mocked data as well as the front end with real back end data via API calls. App.spec.ts contains smaller e2e tests whereas App.long.spec.ts contains two long, expansive e2e tests of many different user functions in a row; one of these tests uses mocked data while the other uses real data.

# How to

**The following instructions are assuming that you have opened the folder maps-abousque-knewman6 in Visual Studio Code.**

## Run Tests

### Run Front End Tests

1. cd client
2. npm install
3. npm test

### Run Back End Tests

1. cd server
2. mvn test OR mvn package

## End User (use UI)

1. Open a first terminal
2. Enter "cd server"
3. Enter "mvn package" and wait for project to build
4. Enter "./run" to start back end Server
5. Open a second terminal
6. Enter "cd client"
7. Enter "npm install"
8. Enter "npm start"
9. Navigate to a new tab in a browser and go to "http:/localhost:8000"
10. Click "Log in with Google" and log in using your Google account

Once the page has loaded you can interact with the following actions in any order:

1. Add a pin to the map by clicking anywhere on the map displayed (these pins will persist on logout/login/reload and are associated with your Google account)
2. Clear all pins from the map (and your account) by clicking the "Clear All Pins" button below the map
3. Zoom in/out of the map using your mouse while hovering over the map
4. Move around the map by clicking and dragging on the map
5. Search the areas in the redlining data for a keyword in area descriptions by entering "search {your keyword of interest}" into the command prompt and clicking the "Submit" button below it
6. Clearing your search results from the above by entering "reset" into the command prompt and clicking the "Submit" button below it
7. Clear the REPL history above the command prompt for readability by entering "clear" into the command prompt and clicking the "Submit" button below it

## Developer Stakeholder (Webapp author)

### Run the Server

1. Open a first terminal
2. Enter "cd server"
3. Enter "mvn package" and wait for project to build
4. Enter "./run" to start back end Server

### Call the APIs (either from a browser or fetch in own webapp, i.e. User Story 3)

5. Call the redliningData endpoint to query redlining data for major U.S. cities in the 1930’s, sourced from Mapping Inequality:

   - http://localhost:3232/redliningData (to query all data)  
      OR
   - http://localhost:3232/redliningData?minLat=Number&maxLat=Number&minLong=Number&maxLong=Number (to query data for regions enclosed by a specific bounding box of coordinates)

6. Call the redliningSearch endpoint to search the redlining data for major U.S. cities in the 1930’s (from Mapping Inequality) for areas whose area descriptions contain a certain keyword: http://localhost:port/redliningSearch?keyword=String

# Collaboration

Helped @ddedona with caching concepts by showing some of our CachedRedlining.java code.
