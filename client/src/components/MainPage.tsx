// MapsGearup.tsx
import React, { useState } from "react";
import Mapbox from "./Mapbox";
import REPL from "./REPL";
import "../styles/maps.css";
import { getLoginCookie } from "../utils/cookie";
import Pantry from "./Pantry";
import Recipes from "./Recipes";

enum Section {
  PANTRY_PAGE = "PANTRY_PAGE",
  RECIPES_PAGE = "RECIPES_PAGE",
}

export default function MapsGearup() {
  const [keywordSearch, setKeywordSearch] = useState("");
  const userID = getLoginCookie();
  const [section, setSection] = useState<Section>(Section.PANTRY_PAGE);

  return (
    <div>
      <h1 aria-label="Gearup Title"> Crave Aid </h1>
      <h3 aria-label="Gearup Subtitle">
        {" "}
        Use the search box to add ingredients.{" "}
      </h3>
      <h3 aria-label="Gearup Subtitle">
        {" "}
        Click on the ingredient to remove it.{" "}
      </h3>
      <div className="button-container">
        <button
          onClick={() => setSection(Section.PANTRY_PAGE)}
          className={
            section === Section.PANTRY_PAGE
              ? "active-button"
              : "inactive-button"
          }
        >
          My Pantry
        </button>
        <button
          onClick={() => setSection(Section.RECIPES_PAGE)}
          className={
            section === Section.RECIPES_PAGE
              ? "active-button"
              : "inactive-button"
          }
        >
          Recipes
        </button>
      </div>
      {section === Section.PANTRY_PAGE ? <Pantry /> : null}
      {section === Section.RECIPES_PAGE ? <Recipes /> : null}
    </div>
  );
}
