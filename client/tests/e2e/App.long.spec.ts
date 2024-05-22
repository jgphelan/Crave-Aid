mport { expect, test } from "@playwright/test";
import { clearUser } from "../../src/utils/api";

/**
  The general shapes of tests in Playwright Test are:
    1. Navigate to a URL
    2. Interact with the page
    3. Assert something about the page against your expectations
  Look for this pattern in the tests below!
 */

const SPOOF_UID = "mock-user-id";

interface Recipe {
  name: string;
  image: string;
  instructions: string;
  ingredients: string[];
  youtube: string;
}

const mockRecipes: Recipe[] = [
  {
    name: "Pasta Carbonara",
    image: "pasta_carbonara.jpg",
    instructions:
      "Cook spaghetti according to package instructions. In a separate bowl, whisk together eggs, grated Parmesan cheese, and black pepper. In a pan, cook diced pancetta until crispy. Once spaghetti is cooked, drain and add it to the pan with pancetta. Immediately pour the egg and cheese mixture over the hot pasta, stirring quickly to coat the spaghetti. Serve hot, garnished with additional Parmesan cheese and black pepper if desired.",
    ingredients: [
      "spaghetti",
      "eggs",
      "Parmesan cheese",
      "black pepper",
      "pancetta",
    ],
    youtube: "https://www.youtube.com/watch?v=example",
  },
  {
    name: "Chicken Curry",
    image: "chicken_curry.jpg",
    instructions:
      "In a large skillet, heat oil over medium heat. Add chopped onions and cook until softened. Add minced garlic, grated ginger, and curry powder, stirring constantly for about a minute. Add diced chicken breast to the skillet and cook until browned on all sides. Pour in diced tomatoes and coconut milk, stirring to combine. Simmer for 20-25 minutes until chicken is cooked through and sauce is thickened. Serve hot over rice, garnished with fresh cilantro.",
    ingredients: [
      "chicken breast",
      "onions",
      "garlic",
      "ginger",
      "curry powder",
      "diced tomatoes",
      "coconut milk",
      "rice",
      "fresh cilantro",
    ],
    youtube: "https://www.youtube.com/watch?v=example",
  },
];

test.beforeEach(
  "add spoof uid cookie to browser",
  async ({ context, page }) => {
    // - Add "uid" cookie to the browser context
    await context.addCookies([
      {
        name: "uid",
        value: SPOOF_UID,
        url: "http://localhost:8000",
      },
    ]);

    // wipe everything for this spoofed UID in the database.
    await clearUser(SPOOF_UID);
  }
);

test("test-initial-load-visibility", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await expect(page.getByRole("button", { name: "My Pantry" })).toBeVisible();
  await expect(page.getByRole("button", { name: "Recipes" })).toBeVisible();
  await expect(
    page.getByPlaceholder("Search ingredients to add to")
  ).toBeVisible();
  await expect(page.getByLabel("Gearup Title")).toBeVisible();
  await expect(page.getByRole("button", { name: "Sign Out" })).toBeVisible();
});

test("test-pantry-page-visibility", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await page.getByRole("button", { name: "Recipes" }).click();
  await expect(
    page.getByPlaceholder("Search a recipe by ingredients")
  ).toBeVisible();
  await expect(page.getByRole("button").nth(3)).toBeVisible();
  await expect(page.getByLabel("Gearup Title")).toBeVisible();
  await expect(page.getByRole("button", { name: "My Pantry" })).toBeVisible();
  await expect(page.getByRole("button", { name: "Sign Out" })).toBeVisible();
});

test("test-proper-ingredient-options-on-search", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await page.getByPlaceholder("Search ingredients to add to").click();
  await page.getByPlaceholder("Search ingredients to add to").fill("salmon");
  await expect(
    page.locator("li").filter({ hasText: /^Salmon$/ })
  ).toBeVisible();
  await expect(
    page.locator("li").filter({ hasText: "Smoked Salmon" })
  ).toBeVisible();
  await expect(
    page
      .locator("li")
      .filter({ hasText: /^Salmon$/ })
      .locator("i")
  ).toBeVisible();
});

test("test-single-ingredient-pantry-addition", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await page.getByPlaceholder("Search ingredients to add to").click();
  await page.getByPlaceholder("Search ingredients to add to").fill("sa");
  await page.getByText("Salmon", { exact: true }).click();
  await expect(page.getByText("Salmon")).toBeVisible();
});
test("test-multi-ingredient-pantry-addition", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await page.getByPlaceholder("Search ingredients to add to").click();
  await page.getByPlaceholder("Search ingredients to add to").fill("sa");
  await page.getByText("Salmon", { exact: true }).click();
  await page.getByPlaceholder("Search ingredients to add to").click();
  await page.getByPlaceholder("Search ingredients to add to").fill("sa");
  await page.getByText("Balsamic Vinegar").click();
  await page.getByPlaceholder("Search ingredients to add to").click();
  await page.getByPlaceholder("Search ingredients to add to").fill("we");
  await page.getByText("Sunflower Oil").click();
  await page.getByPlaceholder("Search ingredients to add to").click();
  await page.getByPlaceholder("Search ingredients to add to").fill("tr");
  await page.getByText("Black Treacle").click();
  await expect(page.getByRole("cell", { name: "Salmon" })).toBeVisible();
  await expect(page.getByRole("cell", { name: "Sunflower Oil" })).toBeVisible();
  await expect(
    page.getByRole("cell", { name: "Balsamic Vinegar" })
  ).toBeVisible();
  await expect(page.getByRole("cell", { name: "Black Treacle" })).toBeVisible();
});

test("test-single-ingredient-pantry-addition-and-removal", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await page.getByPlaceholder("Search ingredients to add to").click();
  await page.getByPlaceholder("Search ingredients to add to").fill("sa");
  await page.getByText("Salmon", { exact: true }).click();
  await expect(page.getByRole("cell", { name: "Salmon" })).toBeVisible();
  await page.getByRole("cell", { name: "Salmon" }).click();
  await expect(page.getByRole("cell", { name: "Salmon" })).toBeHidden();
});

test("test-multi-ingredient-pantry-addition-and-removal", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await page.getByPlaceholder("Search ingredients to add to").click();
  await page.getByPlaceholder("Search ingredients to add to").fill("sa");
  await page.getByText("Salmon", { exact: true }).click();
  await page.getByPlaceholder("Search ingredients to add to").click();
  await page.getByPlaceholder("Search ingredients to add to").fill("a");
  await page.getByText("Avocado", { exact: true }).click();
  await page.getByPlaceholder("Search ingredients to add to").click();
  await page.getByPlaceholder("Search ingredients to add to").fill("sa");
  await page.getByText("Salmon", { exact: true }).click();
  await page.getByPlaceholder("Search ingredients to add to").click();
  await page.getByPlaceholder("Search ingredients to add to").fill("sa");
  await page.getByText("Balsamic Vinegar").click();
  await expect(page.getByRole("cell", { name: "Salmon" })).toBeVisible();
  await expect(
    page.getByRole("cell", { name: "Balsamic Vinegar" })
  ).toBeVisible();
  await expect(page.getByRole("cell", { name: "Avocado" })).toBeVisible();
  await page.getByRole("cell", { name: "Salmon" }).click();
  await page.getByRole("cell", { name: "Avocado" }).click();
  await page.getByRole("cell", { name: "Balsamic Vinegar" }).click();
  await expect(page.getByRole("cell", { name: "Salmon" })).toBeHidden();
  await expect(
    page.getByRole("cell", { name: "Balsamic Vinegar" })
  ).toBeHidden();
  await expect(page.getByRole("cell", { name: "Avocado" })).toBeHidden();
});

test("test-single-ingredient-recipe-addition", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await page.getByPlaceholder("Search ingredients to add to").click();
  await page.getByPlaceholder("Search ingredients to add to").fill("sa");
  await page.getByText("Salmon", { exact: true }).click();
  await expect(page.getByRole("cell", { name: "Salmon" })).toBeVisible();
});

test("test-single-ingredient-recipe-addition-and-removal", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await page.getByRole("button", { name: "Recipes" }).click();
  await page.getByPlaceholder("Search a recipe by ingredients").click();
  await page.getByPlaceholder("Search a recipe by ingredients").fill("sa");
  await page.getByText("Salmon", { exact: true }).click();
  await expect(page.getByRole("cell", { name: "Salmon" })).toBeVisible();
  await page.getByRole("cell", { name: "Salmon" }).click();
  await expect(page.getByRole("cell", { name: "Salmon" })).toBeHidden();
});

test("test-recipe-page-multi-add-remove", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await expect(page.getByRole("button", { name: "Recipes" })).toBeVisible();
  await page.getByRole("button", { name: "Recipes" }).click();
  await page.getByPlaceholder("Search a recipe by ingredients").click();
  await page.getByPlaceholder("Search a recipe by ingredients").fill("sa");
  await page
    .locator("li")
    .filter({ hasText: /^Salmon$/ })
    .click();
  await page.getByPlaceholder("Search a recipe by ingredients").click();
  await page.getByPlaceholder("Search a recipe by ingredients").fill("re");
  await page.getByText("Black Treacle").click();
  await page.getByPlaceholder("Search a recipe by ingredients").click();
  await page.getByPlaceholder("Search a recipe by ingredients").fill("c");
  await page.getByText("Chicken", { exact: true }).click();
  await expect(page.getByRole("cell", { name: "Salmon" })).toBeVisible();
  await expect(page.getByRole("cell", { name: "Black Treacle" })).toBeVisible();
  await expect(page.getByRole("cell", { name: "Chicken" })).toBeVisible();
  await page.getByRole("cell", { name: "Chicken" }).click();
  await page.getByRole("cell", { name: "Black Treacle" }).click();
  await page.getByRole("cell", { name: "Salmon" }).click();
  await expect(page.getByRole("cell", { name: "Salmon" })).toBeHidden();
  await expect(page.getByRole("cell", { name: "Black Treacle" })).toBeHidden();
  await expect(page.getByRole("cell", { name: "Chicken" })).toBeHidden();
});

test("test-recipe-page-searching", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await page.getByRole("button", { name: "Recipes" }).click();
  await page.getByPlaceholder("Search a recipe by ingredients").click();
  await page.getByPlaceholder("Search a recipe by ingredients").fill("sal");
  await page.getByText("Salmon", { exact: true }).click();
  await expect(page.getByRole("cell", { name: "Salmon" })).toBeVisible();
  await page.getByPlaceholder("Search a recipe by ingredients").click();
  await page.getByPlaceholder("Search a recipe by ingredients").fill("we");
  await page.getByText("Sweetcorn").click();
  await expect(page.getByRole("cell", { name: "Sweetcorn" })).toBeVisible();
  await page.getByRole("button").nth(3).click();
  await expect(page.getByRole("cell", { name: "Salmon" })).toBeVisible();
  await expect(page.getByRole("cell", { name: "Sweetcorn" })).toBeVisible();
  await expect(page.getByText("recipes found.")).toBeVisible();
});

test("Search for recipes with one ingredient", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await page.getByRole("button", { name: "Recipes" }).click();
  await page.getByPlaceholder("Search a recipe by ingredients").click();
  await page.getByPlaceholder("Search a recipe by ingredients").fill("avo");
  await expect(page.getByText("avocado")).toBeVisible();
  await page.getByLabel("search-button").click();
  await expect(page.getByText("recipes found.")).toBeVisible();
  await expect(page.getByText("Cajun spiced fish tacos")).toBeVisible();
  await page
    .locator("tr")
    .filter({ hasText: "Cajun spiced fish tacos" })
    .getByLabel("modal-button")
    .click();
  await expect(
    page.getByRole("img", { name: "Cajun spiced fish tacos" })
  ).toBeVisible();
  await expect(page.getByText("6. Avocado")).toBeVisible();
  await page.getByLabel("close-modal").click();

  await page.getByPlaceholder("Search a recipe by ingredients").fill("beef");
  await expect(
    page.locator("li").filter({ hasText: "Beef Fillet" })
  ).toBeVisible();
});

test("Select and deselect ingredients from suggestions", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await page.getByRole("button", { name: "Recipes" }).click();
  await page.getByPlaceholder("Search a recipe by ingredients").click();
  await page.getByPlaceholder("Search a recipe by ingredients").fill("avo");
  await expect(page.getByText("avocado")).toBeVisible();
  await page.click('li:has-text("avocado")');
  await page.click('li:has-text("avocado")'); // deselect
  await expect(page.getByText("avocado")).not.toBeVisible();
});

test("Search for recipes with multiple ingredients", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await page.getByPlaceholder("Search a recipe by ingredients").click();
  await page.getByPlaceholder("Search a recipe by ingredients").fill("avo");
  await page.click('li:has-text("avocado")');
  await page.getByPlaceholder("Search a recipe by ingredients").fill("garl");
  await page.click('li:has-text("garlic")');
  await page.getByLabel("search-button").click();
  await expect(page.getByText("recipes found.")).toBeVisible();
  await expect(page.getByText("Cajun spiced fish tacos")).toBeVisible();
});

test("View recipe details in modal", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await page.getByRole("button", { name: "Recipes" }).click();
  await page.getByPlaceholder("Search a recipe by ingredients").click();
  await page.getByPlaceholder("Search a recipe by ingredients").fill("avo");
  await page.click('li:has-text("avocado")');
  await page.getByLabel("search-button").click();
  await page
    .locator("tr")
    .filter({ hasText: "Cajun spiced fish tacos" })
    .getByLabel("modal-button")
    .click();
  await page.getByLabel("modal-button").click();
  await expect(page.getByText("Ingredients:")).toBeVisible();
  await expect(page.getByText("6. Avocado")).toBeVisible();
  await expect(page.getByText("Instructions:")).toBeVisible();
});

test("Search multiple ingredients with no common recipe", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await page.getByRole("button", { name: "Recipes" }).click();
  await page.getByPlaceholder("Search a recipe by ingredients").click();
  await page.getByPlaceholder("Search a recipe by ingredients").fill("avo");
  await page.click('li:has-text("avocado")');
  await page.getByPlaceholder("Search a recipe by ingredients").fill("beef");
  await page.click('li:has-text("beef")');
  await page
    .getByPlaceholder("Search a recipe by ingredients")
    .fill("cinnamon");
  await page.click('li:has-text("cinnamon")');

  await locator.click();
  await expect(page.getByText(" 0 recipes found.")).toBeVisible();
});

test("Search for recipes with empty input", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await page.getByRole("button", { name: "Recipes" }).click();

  await page.getByLabel("search-button").click();
  await expect(page.getByText("recipes found.")).not.toBeVisible();
});

test("Search for recipes with non-existing ingredient", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await page.getByRole("button", { name: "Recipes" }).click();

  await page.getByPlaceholder("Search a recipe by ingredients").click();
  await page.getByPlaceholder("Search a recipe by ingredients").fill("xyz");
  await expect(page.getByText("avocado")).not.toBeVisible();
});

test("Search for recipes with case-insensitive ingredient", async ({
  page,
}) => {
  await page.goto("http://localhost:8000/");
  await page.getByRole("button", { name: "Recipes" }).click();

  await page.getByPlaceholder("Search a recipe by ingredients").click();
  await page.getByPlaceholder("Search a recipe by ingredients").fill("AVO");
  await expect(page.getByText("avocado")).toBeVisible();
});

test("Search for recipes with the end of the word", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await page.getByRole("button", { name: "Recipes" }).click();

  await page.getByPlaceholder("Search a recipe by ingredients").click();
  await page.getByPlaceholder("Search a recipe by ingredients").fill("cado");
  await expect(page.getByText("avocado")).toBeVisible();
});
