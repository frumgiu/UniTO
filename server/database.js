const pg = require('pg');
const {info} = require("fancy-log");
const config = {
    host: 'localhost',
    user: 'postgres',
    password: 'sono1peppia',
    database: 'TesiDB',
    port: 5432
}
const client = new pg.Client(config);

module.exports = {
    getTableWithSearch,
    getTableWithFilters
}

client.connect(err => {
    if (err) throw err;
    else {
        console.log("Connected to DB!\n")
    }
});

/*
* Interroga il db filtrando i dati per la parola cercata. Per mantenere la vista consistente bisogna tenere
* conto dei parametri non modificati della ricerca precedente (tags attivi, minyear e maxyear).
*
* Se la parola chiave e' uno stato posso comparare search con i parametri della colonna 'country_formal' (nome intero)
* Se la parola e' una citta' posso utilizzare il bounding box per selezionare i dati con coordinate comprese al suo intervallo
* Altrimenti comparo search per 'namefile'
*/
function getTableWithSearch(lambdaFunction, search, tags, minYear, maxYear, bbox, infoSearch) {
   let query;
   console.log(infoSearch);
    if (infoSearch === "country") {
        query = getTableByCountry(search, tags, minYear, maxYear);
    } else if (infoSearch === "place" || infoSearch === "region") {
        query = getTableByCity(search, tags, minYear, maxYear, bbox);
    } else {
        query = getTableByName(search, tags, minYear, maxYear);
    }
    console.log(query)
    client.query(query).then(res => {
        lambdaFunction(res);
        console.log("Close connection\n")
    }).catch(err => console.log(err))
}

function getTableByName(search, tags, minYear, maxYear) {
    console.log("search name");
    let test = createTagsQuery(tags, minYear, maxYear);
    return `SELECT DISTINCT filename, year, country_formal, region, ST_X(geom::geometry) "log", ST_Y(geom::geometry) "lat" 
                   FROM wlm_data WHERE (UPPER("filename") LIKE UPPER('%${search}%')) AND ` + test;
}

function getTableByCountry(search, tags, minYear, maxYear) {
    console.log("search country");
    let test = createTagsQuery(tags, minYear, maxYear);
    return `SELECT DISTINCT filename, year, country_formal, region, ST_X(geom::geometry) "log", ST_Y(geom::geometry) "lat" 
                   FROM wlm_data WHERE (UPPER("country_formal") LIKE UPPER('${search}')) AND ` + test;
}

function getTableByCity(search, tags, minYear, maxYear, bbox) {
    console.log("search city");
    let test = createTagsQuery(tags, minYear, maxYear, bbox);
    return `SELECT DISTINCT filename, year, country_formal, region, ST_X(geom::geometry) "log", ST_Y(geom::geometry) "lat" 
                   FROM wlm_data WHERE ` + test;
}

/*
* Interrogo il db filtrando i dati per i parametri del menu di scelta, senza avere una parola chiave
*/
function getTableWithFilters(lambdaFunction, tags, minYear, maxYear){
    let test = createTagsQuery(tags, minYear, maxYear);
    const query = `SELECT DISTINCT filename, year, country_formal, region, ST_X(geom::geometry) "log", ST_Y(geom::geometry) "lat" 
                   FROM wlm_data WHERE` + test;
    console.log(query)
    client.query(query).then(res => {
        lambdaFunction(res);
        console.log("Close connection\n")
    }).catch(err => console.log(err))
}

/*
 Create a part of the query, checking if there are any tags or bbox
*/
function createTagsQuery(tags, minYear, maxYear, bbox) {
    let test = ``;
    if (typeof tags !== 'undefined') {
        test += `(`;
        tags.forEach(function (value) {
            test += ` UPPER("region") LIKE UPPER('${value}') OR`;
        })
        test = test.substring(0, test.lastIndexOf(" ")); //I need to remove the last 'OR' operator
        test += `) AND `;
    }
    if (typeof bbox !== 'undefined') {
        test += `( ST_X(geom::geometry) >= '${bbox[0]}' AND ST_X(geom::geometry) <= '${bbox[2]}' 
        AND ST_Y(geom::geometry) >= '${bbox[1]}' AND ST_Y(geom::geometry) <= '${bbox[3]}') AND `;
    }
    test += ` year >= '${minYear}' AND year <= '${maxYear}'`
    return test;
}

/*
 Used to print query's result
 */
function showResult(res) {
    console.log("Table read successfully!");
    if (res.rows.length === 0)
        console.log("Empty table")
    const rows = res.rows;
    rows.map(row => {
        console.log(`read: ${JSON.stringify(row)}`);
    });
}