const express = require('express');
const IncidenciaFenomeno = require('./Dominio/IncidenciaFenomeno');
const GestorIncidencias = require('./Dominio/GestorIncidencias');
const Notificacion = require('./Dominio/Notificacion');
const UsuarioEstandar = require('./Dominio/UsuarioEstandar');
const UsuarioAdmin = require('./Dominio/UsuarioAdmin');
const Localizacion = require('./Dominio/Localizacion');
const GestorUsuarios = require('./Dominio/GestorUsuarios');
const EnviadorNotificaciones = require('./Dominio/EnviadorNotificaciones')
const GestorRefugios = require('./Dominio/GestorRefugios');
const GestorComentarios = require('./Dominio/GestorComentarios');

const yaml = require('yamljs');
const swaggerUI = require('swagger-ui-express');
const cors = require('cors');

const multer = require('multer');
const upload = multer();

const app = express();

const swaggerDocs = yaml.load('./api.yaml');
app.use(cors());
app.use('/swagger', swaggerUI.serve, swaggerUI.setup(swaggerDocs));

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
    await ConsultExternalApis();
    //res.status(200).send(result);
	res.json(result);
})

//app.get
app.get('/', (req, res) => {
    console.log('GET request recived');
    res.send('Pong.');
})

//LLamadas api usuarios
app.get('/usuarios/:email', async (req, res) => {

    var email = req.params.email;
    var result = await GestorUsuarios.getUsuario(email);

    result.password = "AAAAAHHHHH, querias mirar mi contraseņa eh?";

    if (result) {

        //res.status(200).send(result);
        res.json(result);

    } else {

        res.status(404).send("Not found: No user with such email.");

    }

})

//app.post('/usuario/new', async (req, res) => {
app.post('/usuarios', async (req, res) => {

    var email = req.body.email;
    var psswd = req.body.password;

    var result = await GestorUsuarios.createUsuario(email, psswd);

    if (result) {

        //res.status(200).send(result);
        res.json(result);

    } else {

        res.status(400).send("Bad request: User with email already exists.");

    }
})

app.put('/usuarios/:email', async (req, res) => {

    var email = req.params.email;
    var psswd = req.body.password;
    var gravedad = req.body.gravedad;
    var radioefecto = req.body.radioEfecto;

    var result = await GestorUsuarios.updateUsuario(email, psswd, gravedad, radioefecto);

    if (result) {

        res.json(result);

    } else {

        res.status(404).send("Not found: No user with such email.");

    }
})

app.delete('/usuarios/:email', async (req, res) => {

    var email = req.params.email;
    var psswd = req.query.password;

    var result = await GestorUsuarios.deleteUsuario(email, psswd);

    if (result) {

        res.status(200).json(result);

    } else {

        res.status(404).json("Not found: No user with such email.");

    }
})


//localizaciones usuario

app.post('/usuarios/:email/localizaciones', async (req, res) => {

    var email = req.params.email;
    var psswd = req.body.password;
    var lat1 = req.body.latitud1;
    var lon1 = req.body.longitud1;
    var lat2 = req.body.latitud2;
    var lon2 = req.body.longitud2;

    var result = await GestorUsuarios.updateLocalizacionesUsuario(email, psswd, lat1, lon1, lat2, lon2);

    if (result) {

        res.status(200).json({ result: result });

    } else {

        res.status(404).json("Usuario no existe");

    }
})

app.post('/usuarios/:email/filtro', async (req, res) => {

    var email = req.params.email;
    var password = req.body.password;
    var result = await GestorUsuarios.getFiltro(email, password);

    if (result) {

        res.status(200).json(result);

    } else {

        res.status(404).json("Usuario no existe");

    }

})


//llamadas api notificaciones

app.post('/incidencias', async (req, res) => {

    var nombreFenomeno = req.body.nombreFenomeno;
    var latitud = req.body.latitud;
    var longitud = req.body.longitud;
    var fecha = req.body.fecha;
    var hora = req.body.hora;
    var email = req.query.email;
    var password = req.body.password;

    var result = await GestorIncidencias.createIncidencia(latitud, longitud, fecha, hora, nombreFenomeno, false, false, email, password);
    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
})

app.put('/incidencias', async (req, res) => {

    var id = req.query.id;
    var email = req.body.email;
    var password = req.body.password;
    var gravedad = req.body.gravedad;

    var result = await GestorIncidencias.updateIncidencia(email, password, id, gravedad);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
})

app.post('/usuarios/:email/incidenciasNoValidas', async (req, res) => {

    var email = req.params.email;
    var pssword = req.body.password;
    var lat = req.body.latitud;
    var lon = req.body.longitud;

    var result = await GestorIncidencias.getIncidenciasNoValidas(email, pssword, lat, lon);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
})

app.post('/usuarios/:email/notificaciones', async (req, res) => {

    var email = req.params.email;
    var pssword = req.body.password;
    var lat = req.body.latitud;
    var lon = req.body.longitud;

    var result = await EnviadorNotificaciones.getNotificaciones(email, pssword, lat, lon);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
})

function ConsultExternalApis()
{

    GestorIncidencias.getIncidenciasFromAPIs();
}

//Refugios

app.post('/refugios', async (req, res) => {

    var Email = req.body.email;
    var Nombre = req.body.nombre;
    var Password = req.body.password;
    var Latitud = req.body.latitud;
    var Longitud = req.body.longitud;

    var result = await GestorRefugios.createRefugio(Email, Password, Nombre, Latitud, Longitud);

    if (result) {

        res.status(200).json(result);

    } else {

        res.status(404).json("No puedes crear este refugio" );

    }
})

app.get('/refugios', async (req, res) => {
    
    var latitud = req.query.latitud;
    var longitud = req.query.longitud;

    var result = await GestorRefugios.getRefugioByLoc(latitud, longitud);

    if (result) {

        res.json(result);

    } else {

        res.status(404).json("Not found: No shelters at such coordinates.");

    }
})

app.delete('/refugios/:nombre', async (req, res) => {

    var Email = req.query.email;
    var Password = req.query.password;
    var Nombre = req.params.nombre;

    var result = await GestorRefugios.deleteRefugio(Email, Password, Nombre);

    if (result) {

        res.status(200).json({ result: result });

    } else {

        res.status(404).json({ result: "No puedes destruir este refugio" });

    }
})

app.put('/refugios/:nombre', async (req, res) => {

    var email = req.body.email;
    var psswd = req.body.password;
    var Nombre = req.params.nombre;

    var result = await GestorRefugios.updateRefugio(email, psswd, Nombre, radioefecto);

    if (result) {

        res.status(200).json(result);

    } else {

        res.status(404).json({ result: "Usuario no Admin" });

    }
})

//Comentarios

app.post('/comentarios', async (req, res) => {

    var Email = req.body.email;
    var Password = req.body.password;
    var Incfenid = req.body.incfenid;
    var ComentResponseId = req.body.comentresponseid;
    var Contenido = req.body.contenido;

    var result = await GestorComentarios.createComentario(Email, Password, Incfenid, ComentResponseId, Contenido);

    if (result) {

        res.status(200).json(result);

    } else {

        res.status(404).json("No puedes crear este comentario");

    }
})

app.get('/comentarios', async (req, res) => {

    var email = req.query.email;

    var result = await GestorComentarios.getComentariosUsuario(email);

    if (result) {

        res.status(200).json(result);

    } else {

        res.status(404).json("No se ha encontrado comentarios");

    }
})


app.get('/comentarios/:commentid/respuestas', async (req, res) => {

    var commentid = req.params.commentid;

    var result = await GestorComentarios.getComentariosComentario(commentid);

    if (result) {

        res.status(200).json(result);

    } else {

        res.status(404).json("No se ha encontrado comentarios");

    }
})



app.get('/incidenciasFenomeno/:incfenid/comentarios', async (req, res) => {

    var incfenid = req.params.incfenid;

    var result = await GestorComentarios.getComentariosIncidenciaFenomeno(incfenid);

    if (result) {

        res.status(200).json(result);

    } else {

        res.status(404).json("No se ha encontrado comentarios");

    }
})

app.delete('/comentarios/:commentid', async (req, res) => {

    var commentid = req.params.commentid;
    var email = req.query.email;
    var password = req.query.password;

    var result = await GestorComentarios.deleteComentario(commentid, email, password);

    if (result) {

        res.status(200).json(result);

    } else {

        res.status(404).json("No puedes borrar este comentario");

    }
})

app.put('/comentarios/:commentid', async (req, res) => {

    var commentid = req.params.commentid;
    var contenido = req.body.contenido;
    var email = req.body.email;
    var password = req.body.password;

    var result = await GestorComentarios.editComentario(commentid, contenido, email, password);

    if (result) {

        res.status(200).json(result);

    } else {

        res.status(404).json("No puedes editar este comentario");

    }
})

function checkReturnCode(Code) {

    var message;

    switch (Code) {

        case 400:

            message = "Bad Request";
            break;

        case 401:

            message = "Unauthorized";
            break;

        case 403:

            message = "Forbidden";
            break;

        case 404:

            message = "Not Found";
            break;

        default:

            message = "OK";
            break;
    }

    return message;

}

//app.all
app.all('*', (req, res) => {
    res.status(404).send('<h1>404 Not Found</h1>')
})

app.listen(process.env.PORT || 5000, () => {
    console.log('server is ready on port 5000.')

    //setInterval(ConsultExternalApis, 43200000);
    //ConsultExternalApis();
})



//const externalEvents = require('./ExternalEvents/ExternalEvents')

//externalEvents.checkEventos();

//app.post
//app.puto
//app.delete
//app.use
//app.listen

