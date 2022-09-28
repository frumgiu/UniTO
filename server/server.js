const express = require('express');
const path = require('path');
const app = express(), bodyParser = require("body-parser");
const db = require('./database');
port = 3080;
const date = new Date();

app.use(bodyParser.json());
app.use(express.static(path.join(__dirname, '../client/build')));

/*
* Fa una richiesta di interrogazione per il db con i dati che gli sono stati passati. Vengono distinti due casi:
* quello in cui e' presente una parola chiave e quello il filtraggio avviene solo attraverso il menu di scelta
*/
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

/*
* Fa una richiesta di interrogazione del db sapendo che la parola chiave passata e' un luogo (citta' o stato).
*/
app.get(`/api/getTableWithBBox/`, (req, res) => {
    console.log('api/getTableWithBBox called! ' + date.toUTCString());
    db.getTableWithSearch(result => {res.json(result.rows);}, req.query.search, req.query.tagsList, req.query.min, req.query.max, req.query.bbox, req.query.infoPlace);

})

app.get(`/`, (req,res) => {
    res.sendFile(path.join(__dirname, '../client/build/index.html'));
});

app.listen(port, () => {
    console.log('Server listening on port::${port} ' + date.toUTCString());
});