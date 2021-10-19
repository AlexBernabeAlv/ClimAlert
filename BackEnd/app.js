const externalEvents = require('./ExternalEvents/ExternalEvents')
const express = require('express');
const incidenciaFenomeno = require('./Dominio/IncidenciaFenomeno');

const app = express();

//app.get
app.get('/', (req, res) => {
    console.log('GET request recived');
    res.status(200).send('Home Page');
})

app.get('/notificacion', (req, res) => {
    console.log('GET request recived');
    var IncidenciaFenomeno = new incidenciaFenomeno.IncidenciaFenomeno("15/10/2021", "17:09");
    //var notif = notificacion.create()
    //notif.incidenciaFenomeno.incidencia.setGravedad(2);
    var Notificacion = IncidenciaFenomeno.getNotificacion();
    res.status(200).json({ "Notificacion": Notificacion });
})

//app.all
app.all('*', (req, res) => {
    res.status(404).send('<h1>404 Not Found</h1>')
})

app.listen(process.env.PORT, () => {
    console.log('server is ready on port 5000.')
})

//app.post
//app.puto
//app.delete
//app.use
//app.listen

