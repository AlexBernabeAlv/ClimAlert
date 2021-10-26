const express = require('express');
const IncidenciaFenomeno = require('./Dominio/IncidenciaFenomeno');
const GestorIncidencias = require('./Dominio/GestorIncidencias');
const Notificacion = require('./Dominio/Notificacion');
const UsuarioEstandar = require('./Dominio/UsuarioEstandar');
const Localizacion = require('./Dominio/Localizacion');

const app = express();

//app.get
app.get('/', (req, res) => {
    console.log('GET request recived');
    res.status(200).send('Home Page');
})

app.get('/notificacion', (req, res) => {
    console.log('GET request recived');
    var incidenciaFenomeno = new IncidenciaFenomeno("15/10/2021", "17:09");
    //var notif = notificacion.create()
    //notif.incidenciaFenomeno.incidencia.setGravedad(2);
    let usu = new UsuarioEstandar("tostusmuertos.joputa@gmail.com");
 

    var loc1 = new Localizacion(41.38792802563911, 2.1136743796936894);
    var loc2 = new Localizacion(41.42503546418125, 1.957847309078282);

    var incid1 = new IncidenciaFenomeno("26/10/21", "19.30", "Incendio", 1, 1, loc1);
    var incid2 = new IncidenciaFenomeno("26/10/21", "19.20", "Diluvio", 2, 2, loc2);

    usu.setNotificacion(incid1.getNotificacion());
    usu.setNotificacion(incid2.getNotificacion());

    res.status(200).json(usu);
})

//app.all
app.all('*', (req, res) => {
    res.status(404).send('<h1>404 Not Found</h1>')
})

app.listen(5000, () => {
    console.log('server is ready on port 5000.')
})

const externalEvents = require('./ExternalEvents/ExternalEvents')

//app.post
//app.puto
//app.delete
//app.use
//app.listen

