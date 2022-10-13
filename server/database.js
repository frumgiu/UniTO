const pg = require('pg');
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
*/
function getTableWithSearch(lambdaFunction, search, tags, minYear, maxYear, bboxMin, bboxMax) {
    let test = createTagsQuery(tags, minYear, maxYear, bboxMin, bboxMax);
    let query = `SELECT DISTINCT filename, year, country_formal, region, ST_X(geom::geometry) "log", 
                    ST_Y(geom::geometry) "lat" FROM wlm_data WHERE ` + test;
    client.query(query).then(res => {lambdaFunction(res); console.log("Close connection\n")}).catch(err => console.log(err))
}

/*
* Interrogo il db filtrando i dati per i parametri del menu di scelta, senza avere una parola chiave.
*/
function getTableWithFilters(lambdaFunction, tags, minYear, maxYear, bboxMin, bboxMax){
    let test = createTagsQuery(tags, minYear, maxYear, bboxMin, bboxMax);
    const query = `SELECT DISTINCT filename, year, country_formal, region, ST_X(geom::geometry) "log", 
                        ST_Y(geom::geometry) "lat" FROM wlm_data WHERE` + test;
    client.query(query).then(res => {lambdaFunction(res); console.log("Close connection\n")}).catch(err => console.log(err))
}

/*
 Create a part of the query, checking if there are any tags or bbox
*/
function createTagsQuery(tags, minYear, maxYear, bboxMin, bboxMax) {
    let test = ``;
    if (typeof tags !== 'undefined') {
        test += `(`;
        tags.forEach(function (value) {
            test += ` UPPER("region") LIKE UPPER('${value}') OR`;
        })
        test = test.substring(0, test.lastIndexOf(" ")); //I need to remove the last 'OR' operator
        test += `) AND `;
    }
    //if (typeof bbox !== 'undefined') {
    test += `( ST_X(geom::geometry) >= '${bboxMin[0]}' AND ST_X(geom::geometry) <= '${bboxMax[0]}' 
        AND ST_Y(geom::geometry) >= '${bboxMin[1]}' AND ST_Y(geom::geometry) <= '${bboxMax[1]}') AND `;
    //}
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