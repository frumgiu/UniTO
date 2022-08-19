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
    getTable,
    getTableWithSearch,
    getTableWithTag
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
        CREATE TABLE demo (id serial PRIMARY KEY, name VARCHAR(50), category VARCHAR(50), coordinates geography(POINT) null);
        INSERT INTO demo (name, category, coordinates) VALUES ('Parco del Valentino', 'Park', 'POINT(7.686736 45.054847)');
        INSERT INTO demo (name, category, coordinates) VALUES ('Piazza Castello', 'Square', 'POINT(7.685089 45.071217)');
        INSERT INTO demo (name, category, coordinates) VALUES ('Parco di Stupinigi', 'Park', 'POINT(7.587417 44.980997)');
        INSERT INTO demo (name, category, coordinates) VALUES ('Priamar', 'Other', 'POINT(8.48426 44.30459)');
        INSERT INTO demo (name, category, coordinates) VALUES ('Torre del Brandale', 'Building', 'POINT(8.484106 44.3072)');
        INSERT INTO demo (name, category, coordinates) VALUES ('Sirenetta', 'Statue', 'POINT(12.5928976284 55.6890472438)');
        INSERT INTO demo (name, category, coordinates) VALUES ('Cremlino', 'Building', 'POINT(37.629005 55.699336)');`;

    client.query(query).then(() => {
        console.log("Table created successfully!");
    }).catch(err => console.log(err))
}

/*
 Get all the query inside the table
 */
function getTable(lambdaFunction){
    const query = `SELECT name, ST_X(coordinates::geometry) "log", ST_Y(coordinates::geometry) "lat" FROM demo;`;
    client.query(query).then(res => {
        //showResult(res);
        lambdaFunction(res);
        console.log("Close connection\n")
    }).catch(err => console.log(err))
}

/*
 Get the rows with filtered by name or category
 */
function getTableWithSearch(lambdaFunction, filter){
    const query = `SELECT DISTINCT name, ST_X(coordinates::geometry) "log", ST_Y(coordinates::geometry) "lat" 
                   FROM demo WHERE UPPER("name") LIKE UPPER('%${filter}%') OR UPPER("category") LIKE UPPER('%${filter}%');`;
    console.log(query)
    client.query(query).then(res => {
        //showResult(res);
        lambdaFunction(res);
        console.log("Close connection\n")
    }).catch(err => console.log(err))
}

/*
 Get the rows filtered by category with tags list
 */
function getTableWithTag(lambdaFunction, filter){
    const query = `SELECT DISTINCT name, ST_X(coordinates::geometry) "log", ST_Y(coordinates::geometry) "lat" 
                   FROM demo WHERE UPPER("category") LIKE UPPER('%${filter}%');`;
    console.log(query)
    client.query(query).then(res => {
        lambdaFunction(res);
        console.log("Close connection\n")
    }).catch(err => console.log(err))
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