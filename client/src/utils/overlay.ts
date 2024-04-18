import { FeatureCollection } from "geojson";
import { FillLayer } from "react-map-gl";

/**
 * Shape of error JSON response resulting from an API call, with "result" field. Includes only end-user-relevant fields.
 */
interface errorJsonResponse {
  result: string;
}

/**
 * Narrowing function for identifying errorJsonResponse.
 *
 * @param rjson the raw json
 * @returns type predicate indicating if rjson is a errorJsonResponse
 */
function isErrorJsonResponse(rjson: any): rjson is errorJsonResponse {
  if (!("result" in rjson)) return false;
  return true;
}

/**
 * Narrowing function for identifying FeatureCollection.
 *
 * @param json the json of interest
 * @returns type predicate indicating if kson is a FeatureCollection
 */
function isFeatureCollection(json: any): json is FeatureCollection {
  return json.type === "FeatureCollection";
}

/**
 * Gets full redlining data from the redliningData server endpoint.
 *
 * @returns a Promise<FeatureCollection | String>, the full redlining data, or an error message if applicable
 */
export function fetchFullRedliningData(): Promise<String | FeatureCollection> {
  return fetch("http://localhost:3232/redliningData")
    .then((response) => response.json())
    .then((responseObject) => {
      if (!isErrorJsonResponse(responseObject)) {
        return responseObject;
      } else {
        // error, return error message
        return responseObject.result;
      }
    })
    .catch((error) => "Error: Unable to connect to redliningData endpoint.");
}

/**
 * Gets redlining data keyword search results from the redliningSearch server endpoint.
 *
 * @returns a Promise<FeatureCollection | String>, the search results, or an error message if applicable
 */
export function fetchSearchResults(
  keyword: string
): Promise<String | FeatureCollection> {
  return fetch("http://localhost:3232/redliningSearch?keyword=" + keyword)
    .then((response) => response.json())
    .then((responseObject) => {
      if (!isErrorJsonResponse(responseObject)) {
        return responseObject;
      } else {
        // error, return error message
        return responseObject.result;
      }
    })
    .catch((error) => "Error: Unable to connect to redliningSearch endpoint.");
}

// display settings for basic redlining data layer
const propertyName = "holc_grade";
export const geoLayer: FillLayer = {
  id: "geo_data",
  type: "fill",
  paint: {
    "fill-color": [
      "match",
      ["get", propertyName],
      "A",
      "#5bcc04",
      "B",
      "#04b8cc",
      "C",
      "#e9ed0e",
      "D",
      "#d11d1d",
      "#ccc",
    ],
    "fill-opacity": 0.2,
  },
};

// display settings for keyword search results data layer
export const searchResultsLayer: FillLayer = {
  id: "search_result_geo_data",
  type: "fill",
  paint: {
    "fill-color": "#800080",
    "fill-opacity": 0.8,
  },
};

/**
 * Gets overlay data for full redlining dataset.
 *
 * If an error is encountered, logs in console and returns undefined.
 *
 * @returns the FeatureCollection encompassing the whole redlining dataset,
 * or undefined if unable to get this data
 */
export function overlayData(): Promise<FeatureCollection | undefined> {
  return fetchFullRedliningData().then((result) => {
    if (result === undefined) {
      return undefined;
    } else if (isFeatureCollection(result)) {
      return result;
    } else {
      // result is string error message
      console.log("Error overlaying redlining data: " + result);
      return undefined;
    }
  });
}

/**
 * Gets overlay data for results of keyword search of redlining dataset.
 *
 * If an error is encountered, logs in console and returns undefined.
 *
 * @returns the FeatureCollection encompassing the whole search result,
 * or undefined if unable to get this data
 */
export function overlaySearchResults(
  keyword: string
): Promise<FeatureCollection | undefined> {
  return fetchSearchResults(keyword).then((result) => {
    if (result === undefined) {
      return undefined;
    } else if (isFeatureCollection(result)) {
      return result;
    } else {
      // result is string error message
      console.log("Error overlaying search results: " + result);
      return undefined;
    }
  });
}
