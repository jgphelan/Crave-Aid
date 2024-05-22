import React, { useEffect, useState } from "react";
import { IngredientsHolder } from "./IngredientHolder";
import {
  addIngredient,
  getAllIngredients,
  removeIngredient,
} from "../utils/api";

// Search bar and autofill code adapted from here: www.youtube.com/watch?v=pdyFf1ugVfk
const Pantry: React.FC = () => {
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [suggestions, setSuggestions] = useState<string[]>([]);
  const [selectedItems, setSelectedItems] = useState<string[]>([]); // State for selected items

  //Loading all ingredients in from firebase
  useEffect(() => {
    console.log("loadAllIngredients()");
    loadAllIngredients();
  }, []);

  // Function to fetch all ingredients from the 'pantry' collection
  const loadAllIngredients = async () => {
    try {
      const ingredients = await getAllIngredients("pantry");
      console.log(ingredients);
      if (ingredients) {
        setSelectedItems(ingredients.data);
      }
    } catch (error) {
      console.error("Failed to load ingredients:", error);
    }
  };

  //Function to autofill search bar with ingredients based off user typing
  const handleChange = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    setSearchTerm(value);
    if (value.length > 0) {
      // Taking care of case-sensitivity.
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
        await removeIngredient("pantry", suggestion.replace("%20", " "));
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
        await addIngredient("pantry", suggestion);
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
      //Removing ingredient from pantry
      await removeIngredient("pantry", itemToRemove);
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

  return (
    <div>
      <div
        className="search-box"
        aria-label="search-box"
        aria-description="Search box for ingredients to add to pantry"
      >
        <div className="row">
          <input
            type="text"
            className="input-box"
            id="input-box"
            onChange={handleChange}
            value={searchTerm}
            placeholder="Search ingredients to add to your pantry"
            autoComplete="off"
            aria-description="Input box to enter ingredients"
          />
        </div>
        <div
          className="result-box"
          id="result-box"
          aria-description="Foods to add to pantry based off search, uses autocomplete"
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
      <div
        className="pantry-items"
        aria-label="pantry-items"
        aria-description="Items in the pantry"
      >
        <table>
          <tbody>
            {chunkArray(selectedItems, 3).map((chunk, rowIndex) => (
              <tr key={rowIndex}>
                {chunk.map((item, columnIndex) => (
                  <td
                    key={columnIndex}
                    onClick={() => handleItemClick(rowIndex * 3 + columnIndex)}
                  >
                    {item}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Pantry;
