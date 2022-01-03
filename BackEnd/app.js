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
const API4me4u = require('./Dominio/API4Me4You');

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
    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
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

    result.password = "AAAAAHHHHH, querias mirar mi contraseña eh?";

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
})

//app.post('/usuario/new', async (req, res) => {
app.post('/usuarios', async (req, res) => {

    var email = req.body.email;
    var psswd = req.body.password;

    var result = await GestorUsuarios.createUsuario(email, psswd);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
})

app.put('/usuarios/:email', async (req, res) => {

    var email = req.params.email;
    var psswd = req.body.password;
    var gravedad = req.body.gravedad;
    var radioefecto = req.body.radioEfecto;

    var result = await GestorUsuarios.updateUsuario(email, psswd, gravedad, radioefecto);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
})

app.put('/usuarios/:emailUsr/ban', async (req, res) => {

    var emailUsr = req.params.emailUsr;
    var email = req.body.email;
    var psswd = req.body.password;

    var result = await GestorUsuarios.banUsuario(email, psswd, emailUsr);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
})

app.delete('/usuarios/:email', async (req, res) => {

    var email = req.params.email;
    var psswd = req.query.password;

    var result = await GestorUsuarios.deleteUsuario(email, psswd);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
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

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
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

app.post('/usuariosEstandar', async (req, res) => {

    var email = req.body.email;
    var password = req.body.password;
    var result = await GestorUsuarios.getUsuarios(email, password);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
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

    var result = await GestorIncidencias.createIncidencia(latitud, longitud, fecha, hora, nombreFenomeno, false, false, email, password, 0);
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
    var medida = req.body.medida;
    var radioEfecto = req.body.radioEfecto;

    var result = await GestorIncidencias.updateIncidencia(email, password, id, gravedad, radioEfecto, medida);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
})

app.post('/usuarios/:email/incidenciasAdmin', async (req, res) => {

    var email = req.params.email;
    var pssword = req.body.password;
    var valido = req.body.valido;

    var result = await GestorIncidencias.getIncidenciasAdmin(email, pssword, valido);

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

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
})

app.get('/refugios', async (req, res) => {
    
    var latitud = req.query.latitud;
    var longitud = req.query.longitud;

    var result = await GestorRefugios.getRefugioByLoc(latitud, longitud);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
})

app.delete('/refugios/:nombre', async (req, res) => {

    var Email = req.query.email;
    var Password = req.query.password;
    var Nombre = req.params.nombre;

    var result = await GestorRefugios.deleteRefugio(Email, Password, Nombre);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
})

app.post('/allRefugios', async (req, res) => {

    var Email = req.body.email;
    var Password = req.body.password;

    var result = await GestorRefugios.getRefugios(Email, Password);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
})


//Comentarios

app.post('/comentarios', async (req, res) => {

    var Email = req.body.email;
    var Password = req.body.password;
    var Incfenid = req.body.incfenid;
    var ComentResponseId = req.body.commentresponseid;
    var Contenido = req.body.contenido;
    var Fecha = req.body.fecha;
    var Hora = req.body.hora;

    var result = await GestorComentarios.createComentario(Email, Password, Incfenid, ComentResponseId, Contenido, Fecha, Hora);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
})

app.get('/comentarios', async (req, res) => {

    var email = req.query.email;

    var result = await GestorComentarios.getComentariosUsuario(email);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
})


app.get('/comentarios/:commentid/respuestas', async (req, res) => {

    var commentid = req.params.commentid;

    var result = await GestorComentarios.getComentariosComentario(commentid);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
})

app.get('/comentarios/:commentid', async (req, res) => {

    var commentid = req.params.commentid;

    var result = await GestorComentarios.getComentario(commentid);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
})

app.get('/incidenciasFenomeno/:incfenid/comentarios', async (req, res) => {

    var incfenid = req.params.incfenid;

    var result = await GestorComentarios.getComentariosIncidenciaFenomeno(incfenid);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
})

app.put('/comentarios/:commentid/delete', async (req, res) => {

    var commentid = req.params.commentid;
    var email = req.body.email;
    var password = req.body.password;

    var result = await GestorComentarios.deleteComentario(commentid, email, password);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
})

app.put('/comentarios/:commentid', async (req, res) => {

    var commentid = req.params.commentid;
    var email = req.body.email;
    var password = req.body.password;
    var contenido = req.body.contenido;

    var result = await GestorComentarios.editComentario(commentid, contenido, email, password);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }
})

app.post('/usuarios/:emailBuscado/estadisticosIncidencias', async (req, res) => {


    var emailBuscado = req.params.emailBuscado;
    var email = req.body.email;
    var password = req.body.password;
    var filtro = req.body.filtro;


    var result = await GestorIncidencias.getIncidenciasByCreador(email, password, emailBuscado, filtro);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }

})


app.post('/usuarios/:emailBuscado/estadisticosComentarios', async (req, res) => {


    var emailBuscado = req.params.emailBuscado;
    var email = req.body.email;
    var password = req.body.password;
    var filtro = req.body.filtro;

    var result = await GestorComentarios.getComentariosByCreador(email, password, emailBuscado, filtro);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).json(result);
    }

})

//4me4u

app.post('/4me4u/products', async (req, res) => {


    var productName = req.body.productName;

    result = await API4me4u.getObjectPrestado(productName);

    if (typeof result == 'number') {

        var message = checkReturnCode(result);
        res.status(result).json(message);
    } else {

        res.status(200).send(result);
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

        case 500:

            message = "Internal Server Error";
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

