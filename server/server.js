const express = require('express');
const path = require('path');
const app = express(),
    bodyParser = require("body-parser");
const db = require('./database');
const {createTableDemo} = require("./database");
port = 3080;
const date = new Date();

app.use(bodyParser.json());
app.use(express.static(path.join(__dirname, '../client/build')));

app.get(`/api/getTable`, (req, res) => {
    console.log('api/getTable called! ' + date.toUTCString());
    db.createTableDemo();
    db.getTable(result => {
        res.json(result.rows);
    });
})

app.get(`/api/getTableBySearch/`, (req, res) => {
    console.log('api/getTableBySearch called! ' + date.toUTCString());
    console.log(req.query.search + "\n")
    db.getTableWithSearch(result => {
        res.json(result.rows);
    }, req.query.search);
})

app.get(`/api/getTableByFilter/`, (req, res) => {
    console.log('api/getTableByFilter called! ' + date.toUTCString());
    console.log(req.query.tagFilter + "\n")
  /*  db.getTableWithSearch(result => {
        res.json(result.rows);
    }, req.query.tagFilter); */
})

app.get(`/`, (req,res) => {
    res.sendFile(path.join(__dirname, '../client/build/index.html'));
});

app.listen(port, () => {
    console.log('Server listening on port::${port} ' + date.toUTCString());
});