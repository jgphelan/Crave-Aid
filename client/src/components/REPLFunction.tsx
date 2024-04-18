/**
 * Interface that takes in an Array<string> and returns either a String or an String[][].
 */
export interface REPLFunction {
  (args: Array<string>): Promise<String | String[][]>;
}

/**
 * Adds functions from the Map containing all REPLFunctions
 *
 * @param map A map mapping strings to REPLFunctions
 * @param name The name of the function to be added to the map, key value
 * @param newFunc The REPLFunction to be added to the map
 */
export function addFunc(
  map: Map<string, REPLFunction>,
  name: string,
  newFunc: REPLFunction
) {
  map.set(name, newFunc);
}

/**
 * Removes functions from the Map containing all REPLFunctions
 *
 * @param map A map mapping strings to REPLFunctions
 * @param name The name of the function to be removed from the map
 */
export function removeFunc(map: Map<string, REPLFunction>, name: string) {
  map.delete(name);
}
