const axios = require('axios');

export async function getData(searchText, tag, minYear, maxYear) {
    let response;
    response = await axios.get(`/api/getTable/`, {params: {search: searchText, tagsList: tag, min: minYear, max: maxYear}});
    if (response.data.length === 0) {
        return "empty table";
    }
    return response.data;
}

export async function getCoordsForLocation(searchText) {
    var url = 'https://api.mapbox.com/geocoding/v5/mapbox.places/' + encodeURIComponent(searchText) +
        '.json?access_token=pk.eyJ1IjoicG9zaWU5OCIsImEiOiJjbDV5MTVteXAwOHRoM2VwZDFlYzN4YTJuIn0.1rRyi4xUKIBqfnhfA9GfVQ&limit=1';
    let response = await axios.get(url);
    //TODO: mettere tutto minuscolo o maiuscolo per confrontarli
    if (response.data.features[0].text === searchText) {
        console.log("It's a place or a country")
        console.log(response.data.features);
    }
}