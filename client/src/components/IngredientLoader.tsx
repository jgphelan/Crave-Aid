import { promises as fs } from "fs";

class IngredientLoader {
  static async loadIngredients(filePath: string): Promise<string[]> {
    try {
      const data = await fs.readFile(filePath, { encoding: "utf8" });
      return IngredientLoader.parseIngredients(data);
    } catch (error) {
      console.error("Error reading file:", error);
      return [];
    }
  }

  private static parseIngredients(data: string): string[] {
    return data.split(",").map((ingredient) => ingredient.trim());
  }
}

export default IngredientLoader;
