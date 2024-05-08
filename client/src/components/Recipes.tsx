import React, { useState } from "react";
import { IngredientsHolder } from "./IngredientHolder";
import { getRecipes } from "../utils/api";

interface Recipe {
  name: string;
  image: string;
  instructions: string;
  ingredients: string[];
  measurements: string[];
  youtube: string;
}

const Recipes: React.FC = () => {
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [searchResults, setSearchResults] = useState<Recipe[]>([]);
  const [suggestions, setSuggestions] = useState<string[]>([]);
  const [selectedItems, setSelectedItems] = useState<string[]>([]); // State for selected items
  const [showResults, setShowResults] = useState<boolean>(false);
  const [modal, setModal] = useState(false);
  const [selectedRecipe, setSelectedRecipe] = useState<Recipe | null>(null);
  const [loading, setLoading] = useState(false);

  const handleSearch = async () => {
    // For now, let's mock some recipe data
    // const mockRecipes: Recipe[] = [
    //   {
    //     name: "Pasta Carbonara",
    //     image: "pasta_carbonara.jpg",
    //     description:
    //       "A classic Italian pasta dish with eggs, cheese, and pancetta.",
    //   },
    //   {
    //     name: "Chicken Curry",
    //     image: "chicken_curry.jpg",
    //     description:
    //       "A flavorful Indian dish made with chicken, spices, and tomatoes.",
    //   },
    //   // Add more mock recipes as needed
    // ];

    try {
      setSearchResults([]);
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
      }));

      // Here you would perform the actual search based on searchTerm
      // For now, let's just use the mock data
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
      // REPLACE WITH INGREDIENTS DATA
      // console.log(IngredientsHolder.ingredient_list);
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

  const populatedPopup = "hi";

  //Modal from here: https://www.youtube.com/watch?v=9DwGahSqcEc
  const toggleModal = (recipe: Recipe | null) => {
    setModal(!modal);
    setSelectedRecipe(recipe); // Add this line to set the selected recipe
  };

  if (modal) {
    document.body.classList.add("active-modal");
  } else {
    document.body.classList.remove("active-modal");
  }

  return (
    <div>
      <div className="search-box">
        <div className="row">
          <input
            aria-label="ingredients-searchbox"
            aria-description="Search box to find ingredients"
            type="text"
            className="input-box"
            id="input-box"
            onChange={handleChange}
            value={searchTerm}
            placeholder="Search a recipe by ingredients"
            autoComplete="off"
          />
          <button onClick={handleSearch}>
            <i className="fa-solid fa-magnifying-glass"></i>
          </button>
        </div>
        <div className="result-box" id="result-box">
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
      <div className="pantry-items">
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
                      <p>You have {} ingredients needed</p>
                      <p>You are missing {} ingredients</p>
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
