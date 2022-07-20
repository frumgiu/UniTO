const axios = require('axios');

export async function getData() {
    let response = await axios.get( `/api/getTable`);
    return response.data;
}

export async function getDataWithSearch(searchText) {
    let response = await axios.get( `/api/getTableWithSearch/`, {params: {search: searchText}});
    return response.data;
}

export async function getDataByTag(tag) {
    let response = await axios.get( `/api/getTableByTag/`, {params: {tagFilter: tag}});
    return response.data;
}