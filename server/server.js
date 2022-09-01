const express = require('express');
const path = require('path');
const app = express(), bodyParser = require("body-parser");
const db = require('./database');
const {createTableDemo} = require("./database");
port = 3080;
const date = new Date();

app.use(bodyParser.json());
app.use(express.static(path.join(__dirname, '../client/build')));

app.get(`/api/getTable`, (req, res) => {
    console.log('api/getTable called! ' + date.toUTCString());
    db.getTable(result => {res.json(result.rows);});
})

app.get(`/api/getTableBySearch/`, (req, res) => {
    console.log('api/getTableBySearch called! ' + date.toUTCString());
    console.log('Searched text: ' + req.query.search + "\n")
    db.getTableWithSearch(result => {res.json(result.rows);}, req.query.search);
})

app.get(`/api/getTableByFilter/`, (req, res) => {
    console.log('api/getTableByFilter called! ' + date.toUTCString());
    console.log('Tag list: ' + req.query.tagsList + "\n")
    db.getTableWithTag(result => {res.json(result.rows);}, req.query.tagsList, req.query.min, req.query.max);
})

app.get(`/`, (req,res) => {
    res.sendFile(path.join(__dirname, '../client/build/index.html'));
});

app.listen(port, () => {
    console.log('Server listening on port::${port} ' + date.toUTCString());
});