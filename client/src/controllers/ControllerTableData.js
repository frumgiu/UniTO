const axios = require('axios');
let dataAll = [];
/*
* Invia la richiesta al server con i parametri per filtrare
* i dati e riportarli aggiornati sulla mappa
*/
export async function getData(searchText, tag, minYear, maxYear, coords) {
    let response;
    if (coords !== "" && typeof coords !== 'undefined') {
        response = await axios.get(`/api/getTableWithBBox/`,
            {params: {search: searchText, tagsList: tag, min: minYear, max: maxYear, bbox: coords.bbox, infoPlace: coords.info}});
    } else {
        response = await axios.get(`/api/getTable/`, {params: {search: searchText, tagsList: tag, min: minYear, max: maxYear}});
    }
    if (response.data.length === 0) {
        return [];
    }
    dataAll = response.data;
    //console.log("data all number: " + dataAll.length);
    return response.data;
}

export async function updateData(bbmap) {
    return selectData(bbmap);
}

function selectData(bb) {
    let sendData = [];
    for (const data of dataAll) {
        if ((data.log >= bb[0][0] && data.log <= bb[1][0]) && (data.lat >= bb[0][1] && data.lat <= bb[1][1])) {
            sendData.push(data);
        }
    }
    //console.log("data number: " + sendData.length);
    return sendData;
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
    console.log(response);
    //console.log(response.data.features[0].context[2].text)
    return response.data.features[0].context[2].text;
}