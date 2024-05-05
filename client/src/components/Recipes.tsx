import React, { useState } from "react";

interface Recipe {
  name: string;
  image: string;
  description: string;
}

const Recipes: React.FC = () => {
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [searchResults, setSearchResults] = useState<Recipe[]>([]);
  const [showResults, setShowResults] = useState<boolean>(false);
  const [modal, setModal] = useState(false);

  const handleSearch = () => {
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

    // Here you would perform the actual search based on searchTerm
    // For now, let's just use the mock data
    setSearchResults(mockRecipes);
    setShowResults(true);
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
