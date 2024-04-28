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

//    return (
//     <div>
//       <h1 aria-label="Gearup Title">Maps Gearup</h1>
//         <button onClick={() => setSection(Section.PANTRY_PAGE)}>
//         Section 1: My Pantry
//       </button>
//       <button onClick={() => setSection(Section.RECIPES_PAGE)}>
//         Section 2: Recipes
//       </button>
//       {section === Section.PANTRY_PAGE && <Pantry />}
//       {section === Section.RECIPES_PAGE && <Recipes />}
//       </div>);
// }

// return (
//   <div className="maps-gearup-wrapper">
//     <header className="maps-gearup-header">
//       <h2>Interactive Map and REPL Console</h2>
//       {userID && <h5>User ID: {userID}</h5>}
//     </header>
//     <div className="maps-gearup-container">
//       <div className="map-container">
//         {/* Now passing keywordSearch and setKeywordSearch as props */}
//         <Mapbox
//           keywordSearch={keywordSearch}
//           setKeywordSearch={setKeywordSearch}
//         />
//       </div>
//       <div className="repl-container">
//         {/* Only need to pass setKeywordSearch to REPL */}
//         <REPL setKeywordSearch={setKeywordSearch} />
//       </div>
//     </div>
//   </div>
// );
