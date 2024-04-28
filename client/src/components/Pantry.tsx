import React from "react";

const Pantry: React.FC = () => {
  return (
    <div>
      <h1>Pantry Page</h1>
      <div className="search-box">
        <div className="row">
          <input
            type="text"
            id="input-box"
            placeholder="Search ingredients to add to your pantry"
            autoComplete="off"
          />
          <button>Search</button>
        </div>
      </div>
    </div>
  );
};

export default Pantry;
