const axios = require('axios');

export async function getData(searchText, tag, minYear, maxYear, coords) {
    let response;
    if (coords !== "" && typeof coords !== 'undefined') {
        response = await axios.get(`/api/getTableWithBBox/`, {params: {search: searchText, tagsList: tag, min: minYear, max: maxYear, bbox: coords.bbox, infoPlace: coords.info}});
    } else {
        response = await axios.get(`/api/getTable/`, {params: {search: searchText, tagsList: tag, min: minYear, max: maxYear}});
    }
    if (response.data.length === 0) {
        return "empty table";
    }
    return response.data;
}

export async function getCoordsForLocation(searchText) {
    const url = 'https://api.mapbox.com/geocoding/v5/mapbox.places/' + encodeURIComponent(searchText) +
        '.json?access_token=pk.eyJ1IjoicG9zaWU5OCIsImEiOiJjbDV5MTVteXAwOHRoM2VwZDFlYzN4YTJuIn0.1rRyi4xUKIBqfnhfA9GfVQ&limit=1';
    let response = await axios.get(url);
    let returnData = "not valid location";

    if (response.data.features.length > 0 && response.data.features[0].text.toLowerCase() === searchText.toLowerCase()) {
        returnData = {bbox: response.data.features[0].bbox, log: response.data.features[0].center[0],
            lat: response.data.features[0].center[1], info: response.data.features[0].place_type[0]}
    }
    console.log(returnData);
    return returnData;
}