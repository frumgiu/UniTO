port = 3080;
const express = require('express');
const path = require('path');
const app = express(), bodyParser = require("body-parser");
const db = require('./database');
const date = new Date();

let dataAll = [];

app.use(bodyParser.json());
app.use(express.static(path.join(__dirname, '../client/build')));

/*
* Fa una richiesta d'interrogazione per il db con i dati che gli sono stati passati. Vengono distinti due casi:
* quello in cui è presente una parola chiave e quello il filtraggio avviene solo attraverso il menu di scelta
*/
app.get(`/api/getTable/`, (req, res) => {
    console.log('api/getTable called! ' + date.toUTCString());
    if (req.query.search === '') {
        console.log('Tag list: ' + req.query.tagsList + " in " + req.query.min + " - " + req.query.max + "\n")
        db.getTableWithFilters(result => {
            console.log("result rows: " + typeof result.rows);
            res.json(result.rows);
            }, req.query.tagsList, req.query.min, req.query.max);
    } else {
        console.log('Search: ' + req.query.search + ' , tag list: ' + req.query.tagsList + " in " + req.query.min + " - " + req.query.max + "\n")
        db.getTableWithSearch(result => {
            console.log("result rows: " + result.rows.length);
            dataAll = result.rows;
            res.json(result.rows);
            }, req.query.search, req.query.tagsList, req.query.min, req.query.max);
    }
})

/*
* Rimanda al client i dati aggiornati rispetto alla nuova view
*/
app.get(`/api/updateClientData`, (req, res) => {
    console.log('api/updateClientData called! ' + date.toUTCString());
    let sendData = selectData(req.query.bb);
    res.json(sendData);
})

/* TODO: check i valori di bb */
function selectData(bb) {
    let sendData = [];
    for (const data of dataAll) {
        if ((data.log >= bb[0][0] && data.log <= bb[1][0]) && (data.lat >= bb[0][1] && data.lat <= bb[1][1])) {
            sendData += data;
        }
    }
    //console.log("EXAMPLE DATA: " + dataAll.length);
    //console.log("send data length: " + sendData.length);
    return sendData;
}

/*
* Fa una richiesta d'interrogazione del db sapendo che la parola chiave passata è un luogo (città o stato).
*/
app.get(`/api/getTableWithBBox/`, (req, res) => {
    console.log('api/getTableWithBBox called! ' + date.toUTCString());
    db.getTableWithSearch(result => { res.json(result.rows);},
        req.query.search, req.query.tagsList, req.query.min, req.query.max, req.query.bbox, req.query.infoPlace);
})

app.get(`/`, (req,res) => {
    res.sendFile(path.join(__dirname, '../client/build/index.html'));
});

app.listen(port, () => {
    console.log('Server listening on port::${port} ' + date.toUTCString());
});