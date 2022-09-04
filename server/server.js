const express = require('express');
const path = require('path');
const app = express(), bodyParser = require("body-parser");
const db = require('./database');
const {createTableDemo} = require("./database");
port = 3080;
const date = new Date();

app.use(bodyParser.json());
app.use(express.static(path.join(__dirname, '../client/build')));

app.get(`/api/getTable/`, (req, res) => {
    console.log('api/getTable called! ' + date.toUTCString());
    if (req.query.search === '') {
        console.log('Tag list: ' + req.query.tagsList + " in " + req.query.min + " - " + req.query.max + "\n")
        db.getTableWithFilters(result => {res.json(result.rows);}, req.query.tagsList, req.query.min, req.query.max);
    } else {
        console.log('Search: ' + req.query.search + ' , tag list: ' + req.query.tagsList + " in " + req.query.min + " - " + req.query.max + "\n")
        db.getTableWithSearch(result => {res.json(result.rows);}, req.query.search, req.query.tagsList, req.query.min, req.query.max);
    }
})

app.get(`/`, (req,res) => {
    res.sendFile(path.join(__dirname, '../client/build/index.html'));
});

app.listen(port, () => {
    console.log('Server listening on port::${port} ' + date.toUTCString());
});