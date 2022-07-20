const express = require('express');
const path = require('path');
const app = express(),
    bodyParser = require("body-parser");
const db = require('./database');
port = 3080;

app.use(bodyParser.json());
app.use(express.static(path.join(__dirname, '../client/build')));

app.get(`/api/getTable`, (req, res) => {
    console.log('api/getTable called!');
    db.getTable(result => {
        res.json(result.rows);
    });
})

app.get(`/api/getTableWithSearch/`, (req, res) => {
    console.log('api/getTableWithSearch called!');
    console.log(req.params.search + "\n")
    //db.getTableWithSearch(result => {
    //    res.json(result.rows);
    //}, req.data.args);
})

app.get(`/`, (req,res) => {
    res.sendFile(path.join(__dirname, '../client/build/index.html'));
});

app.listen(port, () => {
    console.log(`Server listening on port::${port}`);
});