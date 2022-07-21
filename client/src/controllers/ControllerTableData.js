const axios = require('axios');

export async function getData() {
    let response = await axios.get( `/api/getTable`);
    return response.data;
}

export async function getDataBySearch(searchText) {
    let response;
    if (searchText === "") {
        response = await axios.get( `/api/getTable`);
    } else {
        response = await axios.get(`/api/getTableBySearch/`, {params: {search: searchText}});
        if (response.data.length === 0) {
            return "empty table";
        }
    }
    return response.data;
}

//TODO: not used for now
export async function getDataByTag(tag) {
    let response = await axios.get( `/api/getTableByTag/`, {params: {tagFilter: tag}});
    return response.data;
}