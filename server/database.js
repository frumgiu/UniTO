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
    getTable,
    getTableWithSearch
}

client.connect(err => {
    if (err) throw err;
    else {
        console.log("Connected to DB!\n")
    }
});

function createTableDemo() {
    const query = `DROP TABLE IF EXISTS demo;
        CREATE TABLE demo (id serial PRIMARY KEY, name VARCHAR(50), category VARCHAR(50), coordinates geometry null);
        INSERT INTO demo (name, category, coordinates) VALUES ('Parco del Valentino', 'Park', 'POINT(0 0)');
        INSERT INTO demo (name, category, coordinates) VALUES ('Piazza Castello', 'Square', 'POINT(1 0)');
        INSERT INTO demo (name, category, coordinates) VALUES ('Parco di Stupinigi', 'Park', 'POINT(3 10)');
        INSERT INTO demo (name, category, coordinates) VALUES ('Toret', 'Other', 'POINT(2 1)');
        INSERT INTO demo (name, category, coordinates) VALUES ('Palazzo Madama', 'Building', 'POINT(0 0)');`;

    client.query(query).then(() => {
        console.log("Table created successfully!");
    }).catch(err => console.log(err))
}

function getTable(lambdaFunction){
    const query = `SELECT name FROM demo;`;
    client.query(query).then(res => {
        showResult(res);
        lambdaFunction(res);
        console.log("Close connection\n")
    }).catch(err => console.log(err))
}

function getTableWithSearch(lambdaFunction, filter){
    const query = `SELECT DISTINCT name FROM demo WHERE UPPER("name") LIKE UPPER('%${filter}%') OR UPPER("category") LIKE UPPER('%${filter}%');`;
    console.log(query)
    client.query(query).then(res => {
        showResult(res);
        lambdaFunction(res);
        console.log("Close connection\n")
    }).catch(err => console.log(err))
}

function showResult(res) {
    console.log("Table read successfully!");
    if (res.rows.length === 0)
        console.log("Empty table")
    const rows = res.rows;
    rows.map(row => {
        console.log(`read: ${JSON.stringify(row)}`);
    });
}