const axios = require('axios');

/*
* Invia la richiesta al server con i parametri per filtrare
* i dati e riportarli aggiornati sulla mappa
*/
export async function getData(searchText, tag, minYear, maxYear, coords) {
    let response;
    response = await axios.get(`/api/getTable/`, {params: {search: searchText, tagsList: tag, min: minYear, max: maxYear, bboxMin: coords[0], bboxMax: coords[1]}});
    if (response.data.length === 0) {
        return [];
    }
    //console.log("data all number: " + response.data.length);
    return response.data;
}

/*
* Utilizzo della geolocation API di Mapbox per svolgere forward geocoding
* Inserito un testo nella search bar viene chiamata la funzione di forward geocoding.
* Se il risultato è di tipo "place" o "country" viene ritornato il bounding box, altrimenti
* viene ritornata la stringa "not valid location"
*/
export async function getCoordsForLocation(searchText) {
    const url = 'https://api.mapbox.com/geocoding/v5/mapbox.places/' + encodeURIComponent(searchText) +
        '.json?access_token=pk.eyJ1IjoicG9zaWU5OCIsImEiOiJjbDV5MTVteXAwOHRoM2VwZDFlYzN4YTJuIn0.1rRyi4xUKIBqfnhfA9GfVQ&limit=1';
    let response = await axios.get(url);
    let returnData = "not valid location";

    if (response.data.features.length > 0 && response.data.features[0].text.toLowerCase() === searchText.toLowerCase()) {
        returnData = {bbox: response.data.features[0].bbox, log: response.data.features[0].center[0],
            lat: response.data.features[0].center[1], info: response.data.features[0].place_type[0]}
    }
    return returnData;
}

/* reverse geocoding */
export async function getNameForCoord(location) {
    const url = 'https://api.mapbox.com/geocoding/v5/mapbox.places/' + location.log + ',' + location.lat +
        '.json?access_token=pk.eyJ1IjoicG9zaWU5OCIsImEiOiJjbDV5MTVteXAwOHRoM2VwZDFlYzN4YTJuIn0.1rRyi4xUKIBqfnhfA9GfVQ&limit=1';
    let response = await axios.get(url);
    //console.log(response);
    //console.log(response.data.features[0].context[2].text)
    return response.data.features[0].context[2].text;
}