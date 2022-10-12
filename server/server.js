port = 3080;
const express = require('express');
const path = require('path');
const app = express(), bodyParser = require("body-parser");
const db = require('./database');
const date = new Date();

app.use(bodyParser.json());
app.use(express.static(path.join(__dirname, '../client/build')));

/*
* Fa una richiesta d'interrogazione per il db con i dati che gli sono stati passati. Vengono distinti due casi:
* quello in cui è presente una parola chiave e quello il filtraggio avviene solo attraverso il menu di scelta
*/
app.get(`/api/getTable/`, (req, res) => {
    console.log('api/getTable called! ' + date.toUTCString());
    if (req.query.search === '' || typeof req.query.search === 'undefined') {
        //console.log('Tag list: ' + req.query.tagsList + " in " + req.query.min + " - " + req.query.max + " bbox " + req.query.bbox+ "\n");
        db.getTableWithFilters(result => { res.json(result.rows);}, req.query.tagsList, req.query.min, req.query.max, req.query.bboxMin, req.query.bboxMax);
    } else {
        //console.log('Search: ' + req.query.search + ' , tag list: ' + req.query.tagsList + " in " + req.query.min + " - " + req.query.max + " bbox " + req.query.bbox + "\n")
        db.getTableWithSearch(result => { res.json(result.rows);}, req.query.search, req.query.tagsList, req.query.min, req.query.max,  req.query.bboxMin, req.query.bboxMax);
    }
})

app.get(`/`, (req,res) => {
    res.sendFile(path.join(__dirname, '../client/build/index.html'));
});

app.listen(port, () => {
    console.log('Server listening on port::${port} ' + date.toUTCString());
});