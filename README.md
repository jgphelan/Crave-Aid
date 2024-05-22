# Project Details

**Crave-Aid**

Crave-Aid is a web-app that serves to provide user's with a limited knowledge of cooking recipes, ingredients, and tutorials for them to acheive their culinary goals. The archecture is built on React.js, Java, and is served with FireStore Database. The list of recipes and assocaited ingredients are pulled from [theMealDB.com.](https://www.themealdb.com/).

**Team members:** Kole Newman (@knewman6), Malin Leven (malinel2021), Domingo Viesca (Domingo-v), James Phelan (jgphelan)

**Repo:** https://github.com/cs0320-s24/Crave-Aid

# Design Choices

## High Level Design Overview

### Back End (server/src)

#### IngredientHandler Package

The ingredienthandler contains all the java classes for handling queries to firebase and acts as an intermediatary for the front-end to route through to retreive needed data.

Includes:

- AddIngredientHandler
- ClearIngredientHandler
- GetAllIngredientHandler
- RemoveIngredientHandler
- UtilsIngredients

#### Storage

Contains the firebase interface initailization, used by the ingredientHandler package.

Includes:

- **FirebaseUtilities**
- **MockedUtilities** Exception handlers
- **StorageInterface** stores a recipe object which is later serialized into a JSON

#### parseFilterHelpers

Includes:

- **Caller:** Contains the main parse functionality, notably the methods parseMealbyMulti() and Parse() which handle the bulk of the logic of fetching/reading the call to mealDB and returning to the endpoint handler.
- **MealDBDataSourceException:** Exception handlers
- **Recipe:** stores a recipe object which is later serialized into a JSON in caller.

**_ Implementation Notes _**

- Caller is aided by two function, isInPantry, and binarySearch which efficiently sort a recipe's ingrediants list, and binary search it with the user's ingredients list to find if the user contains, or does not contain, an ingredient.

### Front End (client/src)

Components contains all of the components of the front end. This includes important files like App.tsx which is the topmost component of the UI, Recipe.tsx which controls the recipe search, Pantry which controls the pantry functionality, and other functionality in Ingredients Holder. Auth is a subfolder of components which contains the logic for Firebase authentication for login/logout UI functionality.

Styles contains all of the css files that contain style information for each part of the UI.

Utils contains utility files used by components to cleanly work with the UI. api.ts is used for interacting with API endpoints to connect front end logic to back end data. cookie.ts is used to manage user cookies with Firebase.

## Specific Design Choices

1. We included all the possible ingredients in the front-end in a large list as to avoid a user entering in a ingredient incorrectly and returning an error. To enter an ingredient, a user must click on the pop-up.
2. We ordered user presented recipe's based on how many ingredients are in their "SharedIngredients" catergory. Basically, the number of ingredients shared between a user's pantry and rendered recipe. The the higher the shared number, the further up the list the recipe is presented when rendered.

# Errors/Bugs

Based on the day of testing, due to the binary search call, sometime the limit of Firestore reads may be reached, causing tests that read information from that database to fail.

The rendering speed can sometime be affected by this call as well.

# Tests

Please see javadocs above tests in respective testing files for more complete descriptions of how each test verifies different parts of the program functionality.

As an overview:

- src\test\java\edu\brown\cs\student\main\server contains test classes in java that contain tests to (a) unit test data sources and backend classes and (b) test the integration of the aforementioned sources with the server via the handler classes. Some of the integration tests use mocked data whereas others use real data.
- client\tests\e2e contains test files in typscript with Playwright to test e2e functionality of the front end with mocked data as well as the front end with real back end data via API calls. App.spec.ts contains smaller e2e tests whereas App.long.spec.ts contains two long, expansive e2e tests of many different user functions in a row; one of these tests uses mocked data while the other uses real data.

# How to

**The following instructions are assuming that you have opened the folder Crave-Aid in Visual Studio Code.**

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
10. Click "Sign In" and log in using your Google account

Once the page has loaded you can interact with the following actions in any order:

1. Add Ingredients to your pantry by searching and clicking the associated ingredient.
2. Clear Ingredients from your pantry by clicking on the desired removal.
3. Click the Recipe's section to pull up another search bar with a similar UI.
4. Add ingredients to search recipe's that contain those ingredients in the same way as adding to your pantry.
5. Click the search icon and watch as recipe's render.
6. Click "More Info" to see the follow scrollable recipe and an associated "How to" Youtube video.
7. Clear the search by deleted selected ingredients, choosing new ones, and clicking the icon again.

# Collaboration

TheMealDB, and Stack Overflow for algorithm advice.
