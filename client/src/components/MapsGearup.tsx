// MapsGearup.tsx
import React, { useState } from "react";
import Mapbox from "./Mapbox";
import REPL from "./REPL";
import "../styles/maps.css";
import { getLoginCookie } from "../utils/cookie";

export default function MapsGearup() {
  const [keywordSearch, setKeywordSearch] = useState("");
  const userID = getLoginCookie();

  return (
    <div className="maps-gearup-wrapper">
      <header className="maps-gearup-header">
        <h2>Interactive Map and REPL Console</h2>
        {userID && <h5>User ID: {userID}</h5>}
      </header>
      <div className="maps-gearup-container">
        <div className="map-container">
          {/* Now passing keywordSearch and setKeywordSearch as props */}
          <Mapbox
            keywordSearch={keywordSearch}
            setKeywordSearch={setKeywordSearch}
          />
        </div>
        <div className="repl-container">
          {/* Only need to pass setKeywordSearch to REPL */}
          <REPL setKeywordSearch={setKeywordSearch} />
        </div>
      </div>
    </div>
  );
}
