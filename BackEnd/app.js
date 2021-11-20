const express = require('express');
const IncidenciaFenomeno = require('./Dominio/IncidenciaFenomeno');
const GestorIncidencias = require('./Dominio/GestorIncidencias');
const Notificacion = require('./Dominio/Notificacion');
const UsuarioEstandar = require('./Dominio/UsuarioEstandar');
const UsuarioAdmin = require('./Dominio/UsuarioAdmin');
const Localizacion = require('./Dominio/Localizacion');
const GestorUsuarios = require('./Dominio/GestorUsuarios');
const EnviadorNotificaciones = require('./Dominio/EnviadorNotificaciones')


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

const DataController = require('./BD/DataController');

//Inicializar base datos

app.post('/BD/reset', async (req, res) => {

    var result = await DataController.resetBD().catch(error => { console.error(error) });
    res.status(200).send(result);
})

//app.get
app.get('/', (req, res) => {

    console.log('GET request recived');
    res.status(200).send('Home Page');
})

//LLamadas api usuarios
app.get('/usuario/:email', async (req, res) => {

    var email = req.params.email;
    var result = await GestorUsuarios.getUsuario(email);

    if (result) {

        res.status(200).send(result);

    } else {

        res.status(404).send("Usuario no existe");

    }

})

app.post('/usuario/new', async (req, res) => {
    
    var email = req.body.email;
    var psswd = req.body.password;

    var result = await GestorUsuarios.createUsuario(email, psswd);

    if (result) {

        res.status(200).send(result);

    } else {

        res.status(404).send("Usuario ya existe");

    }
})

app.put('/usuario/:email/update', async (req, res) => {

    var email = req.params.email;
    var psswd = req.body.password;
    var gravedad = req.body.gravedad;
    var radioefecto = req.body.radioEfecto;

    var result = await GestorUsuarios.updateUsuario(email, psswd, gravedad, radioefecto);

    if (result) {

        res.status(200).send(result);

    } else {

        res.status(404).send("Usuario no existe");

    }
})

app.delete('/usuario/:email/delete', async (req, res) => {

    var email = req.params.email;
    var psswd = req.body.password;

    var result = await GestorUsuarios.deleteUsuario(email, psswd);

    if (result) {

        res.status(200).send(result);

    } else {

        res.status(404).send("Usuario no existe");

    }
})


//localizaciones usuario

app.post('/usuario/:email/localizaciones/new', async (req, res) => {

    var email = req.params.email;
    var psswd = req.body.password;
    var lat1 = req.body.latitud1;
    var lon1 = req.body.longitud1;
    var lat2 = req.body.latitud2;
    var lon2 = req.body.longitud2;


    var result = await GestorUsuarios.createLocalizacionUsuario(email, psswd, lat1, lon1, lat2, lon2);

    if (result) {

        res.status(200).send(result);

    } else {

        res.status(404).send("Usuario no existe");

    }
})

app.put('/usuario/:email/localizaciones/update', async (req, res) => {

    var email = req.params.email;
    var psswd = req.body.password;
    var lat1 = req.body.latitud1;
    var lon1 = req.body.longitud1;
    var lat2 = req.body.latitud2;
    var lon2 = req.body.longitud2;

    var result = await GestorUsuarios.updateLocalizacionesUsuario(email, psswd, lat1, lon1, lat2, lon2);

    if (result) {

        res.status(200).send(result);

    } else {

        res.status(404).send("Usuario no existe");

    }
})




//llamadas api notificaciones

app.post('/incidencia/new', async (req, res) => {

    var nombreFenomeno = req.body.nombreFenomeno;
    var latitud = req.body.latitud;
    var longitud = req.body.longitud;
    var fecha = req.body.fecha;
    var hora = req.body.hora;

    var result = await GestorIncidencias.createIncidencia(latitud, longitud, fecha, hora, nombreFenomeno, res);

    if (result) {

        res.status(200).send(result);

    } else {

        res.status(404).send(result);

    }
})

app.get('/usuario/:email/notificaciones', async (req, res) => {

    var email = req.params.email;
    var pssword = req.body.password;
    var lat = req.body.latitud;
    var lon = req.body.longitud;

    console.log(email);
    console.log(pssword);
    console.log(lat);
    console.log(lon);

    var result = await EnviadorNotificaciones.getNotificaciones(email, pssword, lat, lon);

    if (result) {

        console.log("return result");
        res.status(200).send({result});

    } else {

        console.log("return error");
        res.status(404).send(result);

    }
})


//app.all
app.all('*', (req, res) => {
    res.status(404).send('<h1>404 Not Found</h1>')
})

app.listen(process.env.PORT || 5000, () => {
    console.log('server is ready on port 5000.')
})

//const externalEvents = require('./ExternalEvents/ExternalEvents')

//app.post
//app.puto
//app.delete
//app.use
//app.listen

