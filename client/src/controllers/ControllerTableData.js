const axios = require('axios');

export async function getData(searchText, tag, minYear, maxYear) {
    let response;
    response = await axios.get(`/api/getTable/`, {params: {search: searchText, tagsList: tag, min: minYear, max: maxYear}});
    if (response.data.length === 0) {
        return "empty table";
    }
    return response.data;
}