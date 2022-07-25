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
    getTableWithSearch
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
        CREATE TABLE demo (id serial PRIMARY KEY, name VARCHAR(50), category VARCHAR(50), coordinates geometry null);
        INSERT INTO demo (name, category, coordinates) VALUES ('Parco del Valentino', 'Park', 'POINT(45.054847 7.686736)');
        INSERT INTO demo (name, category, coordinates) VALUES ('Piazza Castello', 'Square', 'POINT(45.071217 7.685089)');
        INSERT INTO demo (name, category, coordinates) VALUES ('Parco di Stupinigi', 'Park', 'POINT(44.980997 7.587417)');
        INSERT INTO demo (name, category, coordinates) VALUES ('Priamar', 'Other', 'POINT(44.30459 8.48426)');
        INSERT INTO demo (name, category, coordinates) VALUES ('Torre del Brandale', 'Building', 'POINT(44.3072 8.484106)');`;

    client.query(query).then(() => {
        console.log("Table created successfully!");
    }).catch(err => console.log(err))
}

/*
 Get all the query inside the table
 */
function getTable(lambdaFunction){
    const query = `SELECT name, coordinates FROM demo;`;
    client.query(query).then(res => {
        showResult(res);
        lambdaFunction(res);
        console.log("Close connection\n")
    }).catch(err => console.log(err))
}

/*
 Get the rows with filter as name or category
 */
function getTableWithSearch(lambdaFunction, filter){
    const query = `SELECT DISTINCT name, coordinates FROM demo WHERE UPPER("name") LIKE UPPER('%${filter}%') OR UPPER("category") LIKE UPPER('%${filter}%');`;
    console.log(query)
    client.query(query).then(res => {
        showResult(res);
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