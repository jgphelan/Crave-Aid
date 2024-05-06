import React, { useState } from "react";
import { IngredientsHolder } from "./IngredientHolder";
import { getRecipes } from "../utils/api"

interface Recipe {
  name: string;
  image: string;
  description: string;
}

const Recipes: React.FC = () => {
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [searchResults, setSearchResults] = useState<Recipe[]>([]);
  const [suggestions, setSuggestions] = useState<string[]>([]);
  const [selectedItems, setSelectedItems] = useState<string[]>([]); // State for selected items
  const [showResults, setShowResults] = useState<boolean>(false);
  const [modal, setModal] = useState(false);

  const handleSearch = async () => {
    // For now, let's mock some recipe data
    const mockRecipes: Recipe[] = [
      {
        name: "Pasta Carbonara",
        image: "pasta_carbonara.jpg",
        description:
          "A classic Italian pasta dish with eggs, cheese, and pancetta.",
      },
      {
        name: "Chicken Curry",
        image: "chicken_curry.jpg",
        description:
          "A flavorful Indian dish made with chicken, spices, and tomatoes.",
      },
      // Add more mock recipes as needed
    ];


    const recipeJSON = getRecipes(selectedItems);

    console.log(recipeJSON);

    // Here you would perform the actual search based on searchTerm
    // For now, let's just use the mock data
    setSearchResults(mockRecipes);
    setShowResults(true);
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
  const toggleModal = () => {
    setModal(!modal);
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
        <table>
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
          <table className="recipes-table">
            <tbody>
              {searchResults.map((recipe, index) => (
                <tr key={index}>
                  <td>
                    <img src={recipe.image} alt={recipe.name} />
                  </td>
                  <td>{recipe.name}</td>
                  <td>{recipe.description}</td>
                  <td>
                    {" "}
                    <button className="modal-button" onClick={toggleModal}>
                      {" "}
                      See more{" "}
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
      {modal && (
        <div className="modal">
          <div onClick={toggleModal} className="overlay"></div>
          <div className="modal-content">
            <h2>Hello Modal</h2>
            <p>{populatedPopup}</p>
            <button className="close-modal" onClick={toggleModal}>
              CLOSE
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Recipes;
