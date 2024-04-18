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

export async function addPin(lat: number, lng: number, id: number) {
  const uid = getLoginCookie() || "";
  if (!uid) throw new Error("User ID not found.");

  const endpoint = "add-pin";
  const queryParams = {
    uid,
    lat: lat.toString(),
    lng: lng.toString(),
    id: id.toString(),
  };

  return queryAPI(endpoint, queryParams);
}

export async function clearPins() {
  const uid = getLoginCookie();
  if (!uid) throw new Error("User ID not found.");

  const endpoint = "clear-pins";
  const queryParams = { uid };

  return queryAPI(endpoint, queryParams);
}

export async function clearUser(uid: string = getLoginCookie() || "") {
  return await queryAPI("clear-user", {
    uid: uid,
  });
}
