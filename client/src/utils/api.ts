import { Pin } from "../components/Mapbox";
import { getLoginCookie } from "./cookie";

const HOST = "http://localhost:3232";

async function queryAPI(
  endpoint: string,
  query_params: Record<string, string>
) {
  const paramsString = new URLSearchParams(query_params).toString();
  const url = `${HOST}/${endpoint}?${paramsString}`;
  const response = await fetch(url);
  if (!response.ok) {
    console.error(response.status, response.statusText);
  }
  return response.json();
}

export async function addIngredient(collection: string, ingredient: string) {
  const uid = getLoginCookie();
  if (!uid) throw new Error("User ID not found.");

  const endpoint = `add-ingredient/${collection}/${encodeURIComponent(
    ingredient
  )}`;
  const queryParams = { uid };

  return queryAPI(endpoint, queryParams);
}

export async function removeIngredient(collection: string, ingredient: string) {
  const uid = getLoginCookie();
  if (!uid) throw new Error("User ID not found.");

  const endpoint = `remove-ingredient/${collection}/${encodeURIComponent(
    ingredient
  )}`;
  const queryParams = { uid };

  return queryAPI(endpoint, queryParams);
}

export async function getAllIngredients(collection: string) {
  const uid = getLoginCookie();
  if (!uid) throw new Error("User ID not found.");

  const endpoint = `get-ingredients/${collection}`;
  const queryParams = { uid };

  return queryAPI(endpoint, queryParams);
}

export async function clearUser(uid: string = getLoginCookie() || "") {
  return await queryAPI("clear-user", {
    uid: uid,
  });
}

export async function clearAllIngredients(collection: string) {
  const uid = getLoginCookie();
  if (!uid) throw new Error("User ID not found.");

  const endpoint = `clear-ingredients/${collection}`;
  const queryParams = { uid };

  return queryAPI(endpoint, queryParams);
}
