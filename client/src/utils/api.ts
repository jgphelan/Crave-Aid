import { getLoginCookie } from "./cookie";

const HOST = "http://localhost:3232";

async function queryAPI(
  endpoint: string,
  query_params: Record<string, string | undefined>
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

  const endpoint = "add-ingredient";
  const queryParams = { uid, collection, ingredient: encodeURIComponent(ingredient) };

  return queryAPI(endpoint, queryParams);
}

export async function removeIngredient(collection: string, ingredient: string) {
  const uid = getLoginCookie();
  if (!uid) throw new Error("User ID not found.");

  const endpoint = "remove-ingredient";
  const queryParams = { uid, collection, ingredient: encodeURIComponent(ingredient) };

  return queryAPI(endpoint, queryParams);
}

export async function getAllIngredients(collection: string) {
  const uid = getLoginCookie();
  if (!uid) throw new Error("User ID not found.");

  const endpoint = "get-ingredients";
  const queryParams = { uid, collection };

  return queryAPI(endpoint, queryParams);
}

export async function clearUser(uid: string = getLoginCookie() || "") {
  const endpoint = "clear-user";
  const queryParams = { uid };

  return queryAPI(endpoint, queryParams);
}

export async function clearAllIngredients(collection: string) {
  const uid = getLoginCookie();
  if (!uid) throw new Error("User ID not found.");

  const endpoint = "clear-ingredients";
  const queryParams = { uid, collection };

  return queryAPI(endpoint, queryParams);
}

export async function getRecipes(ingredients: string[]) {
  const uid = getLoginCookie();
  if (!uid) throw new Error("User ID not found.");

  const ingredientsString = ingredients.map(encodeURIComponent).join(",");
  const endpoint = "get-recipes";
  const queryParams = { uid, ingredients: ingredientsString };

  return queryAPI(endpoint, queryParams);
}