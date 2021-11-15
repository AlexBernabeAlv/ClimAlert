
const IncidenciaFenomeno = require('./IncidenciaFenomeno')
const dataController = require('../BD/DataController');

//SINGLETON NO LLAMAR CONSTRUCTOR
class GestorIncidencias {

    constructor() {
    }

    /*
    getIncidencia(l, respuesta) {

        dataController.getUsuario(Email, respuesta);
    }
    */

    createIncidencia(Latitud, Longitud, Fecha, Hora, NombreFenomeno, respuesta) {

        var nombre = dataController.getFenomeno(NombreFenomeno);

        console.log(nombre);
        console.log(NombreFenomeno);

        if (nombre == NombreFenomeno) {
            dataController.createIncidencia(Latitud, Longitud, Fecha, Hora, nombre, respuesta);
        } else {
            respuesta.status(400).send("Fenomeno no existe");
        }
    }
}

module.exports = new GestorIncidencias;