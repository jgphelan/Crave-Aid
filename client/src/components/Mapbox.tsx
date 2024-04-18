import "mapbox-gl/dist/mapbox-gl.css";
import { useEffect, useState } from "react";
import Map, { Layer, Marker, Source, ViewStateChangeEvent } from "react-map-gl";
import {
  geoLayer,
  overlayData,
  searchResultsLayer,
  overlaySearchResults,
} from "../utils/overlay";
import { getLoginCookie } from "../utils/cookie";
import { addPin, clearPins } from "../utils/api";

const MAPBOX_API_KEY = process.env.MAPBOX_TOKEN;
if (!MAPBOX_API_KEY) {
  console.error("Mapbox API key not found. Please add it to your .env file.");
}

interface MapboxProps {
  keywordSearch: string;
  setKeywordSearch: (keyword: string) => void;
}

export interface LatLong {
  lat: number;
  long: number;
}

export interface Pin {
  id: number;
  lat: number;
  lng: number;
}

const ProvidenceLatLong = {
  lat: 41.824,
  long: -71.4128,
};
const initialZoom = 13;
let manualTrigger = 0;

export default function Mapbox(props: MapboxProps) {
  const [viewState, setViewState] = useState({
    longitude: ProvidenceLatLong.long,
    latitude: ProvidenceLatLong.lat,
    zoom: initialZoom,
    pitch: 40,
  });

  /** basic redlining data layer */
  const [overlay, setOverlay] = useState<GeoJSON.FeatureCollection | undefined>(
    undefined
  );

  /** keyword search results data layer */
  const [searchResultsOverlay, setSearchResultsOverlay] = useState<
    GeoJSON.FeatureCollection | undefined
  >(undefined);

  const [showOriginalOverlay, setShowOriginalOverlay] = useState(true);

  useEffect(() => {
    if (showOriginalOverlay) {
      overlayData().then((result) => {
        setOverlay(result || undefined);
      });
    }
  }, [showOriginalOverlay]);

  useEffect(() => {
    overlayData().then((result) => {
      if (result !== undefined) {
        setOverlay(result);
      }
    });
  }, []);

  useEffect(() => {
    if (props.keywordSearch === "") {
      setShowOriginalOverlay(true);
      setSearchResultsOverlay(undefined);
    } else {
      overlaySearchResults(props.keywordSearch).then((result) => {
        if (result === undefined) {
          // failed for some reason
          setSearchResultsOverlay(undefined);
        } else {
          setSearchResultsOverlay(result);
        }
      });
    }
  }, [props.keywordSearch]);

  /** pins */
  const [pins, setPins] = useState<Pin[]>([]);

  useEffect(() => {
    const fetchPins = async () => {
      const uid = getLoginCookie();
      if (uid) {
        try {
          const response = await fetch(
            `http://localhost:3232/get-pins?uid=${uid}`
          );
          if (response.ok) {
            const pinsData = await response.json();
            if (pinsData.status === "success") {
              setPins(pinsData.data);
            } else {
              console.error("Failed to fetch pins:", pinsData.message);
            }
          } else {
            console.error(
              "Failed to fetch pins, HTTP status:",
              response.status
            );
          }
        } catch (error) {
          console.error("Error fetching pins:", error);
        }
      }
    };

    fetchPins();
  }, [pins.length]);

  const handlePinClick = async (lat: number, lng: number) => {
    const newPin = { id: Date.now(), lat, lng };
    setPins((currentPins) => [...currentPins, newPin]);
    await addPin(lat, lng, newPin.id);
  };

  const handleClearPins = async () => {
    try {
      await clearPins();
      setPins([]);
      console.log("Pins cleared successfully.");
    } catch (error) {
      console.error("Error clearing pins:", error);
    }
  };

  return (
    <div className="map" style={{ width: "100%", height: "100%" }}>
      <Map
        mapboxAccessToken={MAPBOX_API_KEY}
        {...viewState}
        style={{ width: "100%", height: "100%" }}
        mapStyle={"mapbox://styles/mapbox/streets-v12"}
        onMove={(ev: ViewStateChangeEvent) => setViewState(ev.viewState)}
        onClick={(e) => {
          let lat = e.lngLat.lat;
          let long = e.lngLat.lng;
          handlePinClick(lat, long);
          console.log(lat);
          console.log(long);
        }}
      >
        <Source id="geo_data" type="geojson" data={overlay}>
          <Layer {...geoLayer} />
        </Source>
        {searchResultsOverlay && (
          <Source
            id="search_result_geo_data"
            type="geojson"
            data={searchResultsOverlay}
          >
            <Layer {...searchResultsLayer} />
          </Source>
        )}
        {pins.map((pin) => (
          <Marker key={pin.id} longitude={pin.lng} latitude={pin.lat}>
            <div style={{ fontSize: "30px" }}>📍</div>
          </Marker>
        ))}
      </Map>
      <button onClick={handleClearPins}>Clear All Pins</button>
    </div>
  );
}
