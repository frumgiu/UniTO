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

export async function getDataByFilter(tag) {
    let response = await axios.get( `/api/getTableByFilter/`, {params: {tagFilter: tag}});
    if (response.data.length === 0) {
        return "empty table";
    }
    return response.data;
}