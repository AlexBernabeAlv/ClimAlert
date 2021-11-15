
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

    async createIncidencia(Latitud, Longitud, Fecha, Hora, NombreFenomeno, respuesta) {

        var nombre = await dataController.getFenomeno(NombreFenomeno);


        if (nombre == NombreFenomeno) {
            
           var result = await dataController.createIncidencia(Latitud, Longitud, Fecha, Hora, nombre);
           
        } else {
            result = "Fenomeno no existe";
        }

        return result;
    }
}

module.exports = new GestorIncidencias;