import React, { useState } from "react";
import { IngredientsHolder } from "./IngredientHolder";
import { getRecipes } from "../utils/api";

//Recipe interface, to be filled in from the backend for each recipe item
interface Recipe {
  name: string;
  image: string;
  instructions: string;
  ingredients: string[];
  measurements: string[];
  youtube: string;
  sharedIngredients: number;
  totalIngredients: number;
}

const nimTelsonRecipe: Recipe[] = [
  {
    name: "Nim Telson",
    image: "https://cs0320.github.io/staff/tim.jpg",
    instructions: "Enjoy your life!",
    ingredients: ["React", "Java", "Python", "CSS", "Typescript", "Node"],
    measurements: ["100%", "100%", "100%", "100%", "100%", "100%"],
    youtube:
      "https://www.youtube.com/watch?time_continue=8&v=E3RcD6LleYU&embeds_referring_euri=https%3A%2F%2Fvideo.search.yahoo.com%2F&embeds_referring_origin=https%3A%2F%2Fvideo.search.yahoo.com&source_ve_path=MzY4NDIsMjg2NjY&feature=emb_logo",
    sharedIngredients: 0,
    totalIngredients: 0,
  },
];

const Recipes: React.FC = () => {
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [searchResults, setSearchResults] = useState<Recipe[]>([]);
  const [suggestions, setSuggestions] = useState<string[]>([]);
  const [selectedItems, setSelectedItems] = useState<string[]>([]); // State for selected items
  const [showResults, setShowResults] = useState<boolean>(false);
  const [modal, setModal] = useState(false);
  const [selectedRecipe, setSelectedRecipe] = useState<Recipe | null>(null);
  const [loading, setLoading] = useState(false);

  //Method to handle searching for recipes
  const handleSearch = async () => {
    try {
      setSearchResults([]);
      if (selectedItems.includes("Nim Telson")) {
        setSearchResults(nimTelsonRecipe);
        alert("Thank you for a wonderful semester!");
      }
      setLoading(true); // Set loading state to true when search starts
      const response = await getRecipes(selectedItems);

      const jdata = JSON.parse(response.data);

      console.log(jdata);

      const recipes = jdata.map((item: any) => ({
        name: item.name,
        image: item.thumbnail,
        instructions: item.instructions,
        ingredients: Array.isArray(item.ingredients) ? item.ingredients : [],
        measurements: Array.isArray(item.measurements) ? item.measurements : [],
        youtube: item.youtube,
        sharedIngredients: item.sharedIngredients,
        totalIngredients: item.totalIngredients,
      }));

      // Here you would perform the actual search based on searchTerm
      // For now, let's just use the mock data
      if (selectedItems.includes("Nim Telson")) {
        alert("Thank you for a wonderful semester!");
      }
      setSearchResults(recipes);
      setShowResults(true);
    } catch (error) {
      console.log("Failed to fetch recipes", error);
    } finally {
      setLoading(false); // Set loading state to false when search completes
    }
  };

  //Function to autofill search bar with ingredients based off user typing
  const handleChange = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    setSearchTerm(value);
    if (value.length > 0) {
      const filteredSuggestions = IngredientsHolder.ingredient_list.filter(
        (suggestion) => suggestion.toLowerCase().includes(value.toLowerCase())
      );
      setSuggestions(filteredSuggestions);
    } else {
      setSuggestions([]);
    }
  };

  //Function to handle adding an item to the pantry if it is clicked
  const handleClick = async (suggestion: string) => {
    if (selectedItems.includes(suggestion)) {
      //If the ingredient is already in pantry, remove it
      try {
        const updatedItems = selectedItems.filter(
          (item) => item !== suggestion
        );
        setSelectedItems(updatedItems);
      } catch (error) {
        console.error("Failed to remove ingredient:", error);
      }
    } else {
      // Otherwise, add the item to selectedItems
      try {
        setSelectedItems([...selectedItems, suggestion]);
      } catch (error) {
        console.error("Failed to add ingredient:", error);
      }
    }
    // Clear the search term and suggestions after selecting
    setSearchTerm("");
    setSuggestions([]);
  };

  //Function to remove item from pantry if clicked
  const handleItemClick = async (index: number) => {
    const itemToRemove = selectedItems[index];
    try {
      const updatedItems = [...selectedItems];
      updatedItems.splice(index, 1);
      setSelectedItems(updatedItems);
    } catch (error) {
      console.error("Failed to remove ingredient:", error);
    }
  };

  //Helper function to chunk array into arrays of size n (for layout of table)
  const chunkArray = (arr: any[], size: number) => {
    return arr.reduce(
      (acc, _, i) => (i % size ? acc : [...acc, arr.slice(i, i + size)]),
      []
    );
  };

  //Modal from here: https://www.youtube.com/watch?v=9DwGahSqcEc
  //Modal which holds the recipe information
  const toggleModal = (recipe: Recipe | null) => {
    setModal(!modal);
    setSelectedRecipe(recipe); // Add this line to set the selected recipe
  };

  //Helping with the modal appearing and dissapearing
  if (modal) {
    document.body.classList.add("active-modal");
  } else {
    document.body.classList.remove("active-modal");
  }

  return (
    <div>
      <div
        className="search-box"
        aria-label="search-box"
        aria-description="Search box to find ingredients to cook with"
      >
        <div className="row">
          <input
            aria-label="ingredients-searchbox"
            aria-description="Input box to enter ingredients"
            type="text"
            className="input-box"
            id="input-box"
            onChange={handleChange}
            value={searchTerm}
            placeholder="Search a recipe by ingredients"
            autoComplete="off"
          />
          <button
            onClick={handleSearch}
            aria-label="handle-search-button"
            aria-description="Button to handle searching"
          >
            <i className="fa-solid fa-magnifying-glass"></i>
          </button>
        </div>
        <div
          className="result-box"
          id="result-box"
          aria-label="result-box"
          aria-description="Box to show ingredients"
        >
          <ul>
            {suggestions.map((suggestion, index) => (
              <li key={index} onClick={() => handleClick(suggestion)}>
                <span className="text">{suggestion}</span>
                {selectedItems.includes(suggestion) ? (
                  <i className="fa-solid fa-minus"></i>
                ) : (
                  <i className="fa-solid fa-plus"></i>
                )}
              </li>
            ))}
          </ul>
        </div>
      </div>
      <div className="pantry-items" aria-label="pantry-items">
        <table
          aria-label="pantry-items-table"
          aria-description="Table to hold pantry items"
        >
          <tbody>
            {chunkArray(selectedItems, 4).map((chunk, rowIndex) => (
              <tr key={rowIndex}>
                {chunk.map((item, columnIndex) => (
                  <td
                    key={columnIndex}
                    onClick={() => handleItemClick(rowIndex * 4 + columnIndex)}
                  >
                    {item}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {showResults && (
        <div className="recipes-box" id="recipes-box">
          <div className="recipes-header">
            {searchResults.length} recipes found.
          </div>
          {loading && <div className="spinner"></div>}
          {!loading && (
            <table className="recipes-table">
              <tbody>
                {searchResults.map((recipe, index) => (
                  <tr key={index}>
                    <hr className="line" width="75%" />

                    <img
                      aria-label="recipe-image"
                      aria-description="image of the displayed recipe"
                      className="recipe-image"
                      src={recipe.image}
                      alt={recipe.name}
                    />

                    <div className="centered-content">
                      <p
                        aria-label="recipe-name"
                        aria-description="Name of the recipe"
                        className="name"
                      >
                        {recipe.name}
                      </p>
                      <p>
                        You have <strong>{recipe.sharedIngredients}</strong> of{" "}
                        <strong>{recipe.totalIngredients}</strong> ingredients
                        needed
                      </p>
                      <p>
                        You are missing{" "}
                        <strong>
                          {recipe.totalIngredients - recipe.sharedIngredients}
                        </strong>{" "}
                        ingredients
                      </p>
                      <button
                        aria-label="modal-button"
                        aria-description="Button to see more about the recipe"
                        className="modal-button"
                        onClick={() => toggleModal(recipe)}
                      >
                        See more
                      </button>
                    </div>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}
      {modal && selectedRecipe && (
        <div className="modal">
          <div onClick={toggleModal} className="overlay"></div>
          <div className="modal-content">
            <h2>{selectedRecipe.name}</h2>
            <img
              className="recipe-image2"
              src={selectedRecipe.image}
              alt={selectedRecipe.name}
            />
            <div
              aria-label="ingredients-list"
              aria-description="List of ingredients"
              className="ingredients-list"
            >
              <h3>Ingredients:</h3>
              <table className="ingredients-table">
                <tbody>
                  {selectedRecipe.ingredients
                    .filter(Boolean)
                    .map((ingredient, idx) => (
                      <tr key={idx}>
                        <td
                          style={{ textAlign: "right", paddingRight: "10px" }}
                        >
                          {ingredient.charAt(0).toUpperCase() +
                            ingredient.slice(1)}
                        </td>
                        <td style={{ textAlign: "left", paddingLeft: "10px" }}>
                          {selectedRecipe.measurements[idx]}
                        </td>
                      </tr>
                    ))}
                </tbody>
              </table>
            </div>
            {selectedRecipe.instructions && (
              <div
                aria-label="instructions-list"
                aria-description="List of instructions to make recipe"
                className="instructions-list"
              >
                <h3>Instructions:</h3>
                {selectedRecipe.instructions
                  .split(".")
                  .filter((sentence) => sentence.trim() !== "")
                  .map((sentence, idx) => (
                    <div key={idx}>
                      <span>&#8226;</span> {sentence.trim()}.
                    </div>
                  ))}
              </div>
            )}
            <h3>Need help with this recipe?</h3>
            <a
              aria-label="video-link"
              aria-description="Link to recipe video"
              href={selectedRecipe.youtube}
              target="_blank"
            >
              Watch the video tutorial
            </a>
            <button
              aria-label="close-modal"
              aria-description="Button to close recipe modal"
              className="close-modal"
              onClick={() => toggleModal(null)}
            >
              <i className="fa-solid fa-xmark"></i>
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Recipes;
