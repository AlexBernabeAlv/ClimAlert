const express = require('express');
const IncidenciaFenomeno = require('./Dominio/IncidenciaFenomeno');
const GestorIncidencias = require('./Dominio/GestorIncidencias');
const Notificacion = require('./Dominio/Notificacion');
const UsuarioEstandar = require('./Dominio/UsuarioEstandar');
const UsuarioAdmin = require('./Dominio/UsuarioAdmin');
const Localizacion = require('./Dominio/Localizacion');
const gestorUsuarios = require('./Dominio/GestorUsuarios');


const multer = require('multer');
const upload = multer();

const app = express();

// for parsing application/json
app.use(express.json());

// for parsing application/x-www-form-urlencoded
app.use(express.urlencoded({ extended: true }));

// for parsing multipart/form-data
app.use(upload.array());
app.use(express.static('public'));

const dataController = require('./BD/DataController');

//Inicializar base datos

app.post('/BD/reset', (req, res) => {

    dataController.resetBD(res);
})

//app.get
app.get('/', (req, res) => {

    console.log('GET request recived');
    res.status(200).send('Home Page');
})

//LLamadas api usuarios
app.get('/usuario/:email', (req, res) => {

    var email = req.params.email;
    gestorUsuarios.getUsuario(email, res);
})

app.post('/usuario/new', (req, res) => {
    
    var email = req.body.email;
    var psswd = req.body.password;

    gestorUsuarios.createUsuario(email, psswd, res);
})

app.put('/usuario/:email/update', (req, res) => {

    var email = req.params.email;
    var psswd = req.body.password;
    var gravedad = req.body.gravedad;
    var radioefecto = req.body.radioEfecto;

    var usu = new UsuarioEstandar(email, psswd);
    usu.setFiltro(gravedad, radioefecto);

    
    dataController.updateUsuario(usu, res);
})

app.delete('/usuario/:email/delete', (req, res) => {

    var email = req.params.email;
    var psswd = req.body.password;

    gestorUsuarios.deleteUsuario(email, psswd, res);
    
})



//llamadas api notificaciones

app.post('/incidencia/new', async (req, res) => {

    var nombreFenomeno = req.body.nombreFenomeno;
    var latitud = req.body.latitud;
    var longitud = req.body.longitud;
    var fecha = req.body.fecha;
    var hora = req.body.hora;

    var result = await GestorIncidencias.createIncidencia(latitud, longitud, fecha, hora, nombreFenomeno, res);

    res.status(200).send(result);
})


/*
app.get('/notificacion', (req, res) => {
    console.log('GET request recived');
    var incidenciaFenomeno = new IncidenciaFenomeno("15/10/2021", "17:09");

    let usu = new UsuarioEstandar("tostusmuertos.joputa@gmail.com", "password");

    var loc1 = new Localizacion(41.38792802563911, 2.1136743796936894);
    var loc2 = new Localizacion(41.42503546418125, 1.957847309078282);

    var incid1 = new IncidenciaFenomeno("26/10/21", "19.30", "Incendio", 1, 1, loc1);
    var incid2 = new IncidenciaFenomeno("26/10/21", "19.20", "Diluvio", 2, 2, loc2);

    usu.setFiltro(1, 1);

    usu.setNotificacion(incid1.getNotificacion());
    usu.setNotificacion(incid2.getNotificacion());

    dataController.createUsuario(usu);

    res.status(200).json(usu);
    //var usu2 = dataController.getUsuario("tostusmuertos.joputa@gmail.com", res);
})
*/

//app.all
app.all('*', (req, res) => {
    res.status(404).send('<h1>404 Not Found</h1>')
})

app.listen(5000, () => {
    console.log('server is ready on port 5000.')
})

//const externalEvents = require('./ExternalEvents/ExternalEvents')

//app.post
//app.puto
//app.delete
//app.use
//app.listen

