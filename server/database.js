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
    createTableDemo,
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
 Create a demo table with some rows
*/
function createTableDemo() {
    const query = `DROP TABLE IF EXISTS demo;
        CREATE TABLE demo (id serial PRIMARY KEY, name VARCHAR(50), year INT, region VARCHAR(50), coordinates geography(POINT) null);
        INSERT INTO demo (name, year, region, coordinates) VALUES ('Parco del Valentino', 2020, 'Europe', 'POINT(7.686736 45.054847)');
        INSERT INTO demo (name, year, region, coordinates) VALUES ('Piazza Castello', 2018, 'Europe', 'POINT(7.685089 45.071217)');
        INSERT INTO demo (name, year, region, coordinates) VALUES ('Parco di Stupinigi', 2020, 'Europe', 'POINT(7.587417 44.980997)');
        INSERT INTO demo (name, year, region, coordinates) VALUES ('Priamar', 2012, 'Europe', 'POINT(8.48426 44.30459)');
        INSERT INTO demo (name, year, region, coordinates) VALUES ('Torre del Brandale', 2015, 'Europe', 'POINT(8.484106 44.3072)');
        INSERT INTO demo (name, year, region, coordinates) VALUES ('Sirenetta', 2011, 'Europe', 'POINT(12.5928976284 55.6890472438)');
        INSERT INTO demo (name, year, region, coordinates) VALUES ('Cremlino', 2015, 'Europe', 'POINT(37.629005 55.699336)');`;

    client.query(query).then(() => {
        console.log("Table created successfully!");
    }).catch(err => console.log(err))
}

/*
 Get the rows with filtered by name
*/
function getTableWithSearch(lambdaFunction, search, tags, minYear, maxYear) {
    let test = createTagsQuery(tags, minYear, maxYear);
   /* const query = `SELECT DISTINCT name, year, region, ST_X(coordinates::geometry) "log", ST_Y(coordinates::geometry) "lat"
                   FROM demo WHERE UPPER("name") LIKE UPPER('%${search}%') AND` + test; */
    const query = `SELECT DISTINCT filename, year, country_formal, region, x "log", y "lat" 
                   FROM wlm_data WHERE UPPER("filename") LIKE UPPER('%${search}%') AND` + test;
    console.log(query)
    client.query(query).then(res => {
        lambdaFunction(res);
        console.log("Close connection\n")
    }).catch(err => console.log(err))
}

/*
 Get the rows filtered by country, from the tags list, and years
*/
function getTableWithFilters(lambdaFunction, tags, minYear, maxYear){
    let test = createTagsQuery(tags, minYear, maxYear);
    const query = `SELECT DISTINCT filename, year, country_formal, region, x "log", y "lat" 
                   FROM wlm_data WHERE` + test;
    console.log(query)
    client.query(query).then(res => {
        lambdaFunction(res);
        console.log("Close connection\n")
    }).catch(err => console.log(err))
}

/*
 Create a part of the query, checking if there are any tags
*/
function createTagsQuery(tags, minYear, maxYear) {
    let test = ``;
    if (typeof tags !== 'undefined') {
        tags.forEach(function (value) {
            test += ` UPPER("region") LIKE UPPER('${value}') OR`;
        })
        test = test.substring(0, test.lastIndexOf(" ")); //I need to remove the last 'OR' operator
        test += `AND `;
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