import React, { useState } from "react";

const Recipes: React.FC = () => {
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [searchResults, setSearchResults] = useState<string[]>([]);
  const [showResults, setShowResults] = useState<boolean>(false);

  const handleSearch = () => {
    // Perform search based on searchTerm
    // Update searchResults state with search results
    console.log(searchTerm);
    setShowResults(true);
  };

  return (
    <div>
      <div className="search-box">
        <div className="row">
          <input
            type="text"
            className="input-box"
            id="input-box"
            value={searchTerm}
            placeholder="Search for recipes..."
            autoComplete="off"
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <button onClick={handleSearch}>
            <i className="fa-solid fa-magnifying-glass"></i>
          </button>
        </div>
      </div>
      {showResults && (
        <div className="recipes-box" id="recipes-box">
          <table>
            <thead>
              <tr>
                <th>Recipe Name</th>
                {/* Add more table headers as needed */}
              </tr>
            </thead>
            <tbody>
              {searchResults.map((recipe, index) => (
                <tr key={index}>
                  <td>{recipe}</td>
                  {/* Add more table data cells for additional recipe details */}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default Recipes;
