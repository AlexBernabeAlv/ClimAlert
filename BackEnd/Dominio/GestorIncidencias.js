
const IncidenciaFenomeno = require('./IncidenciaFenomeno')
const dataController = require('../BD/DataController');
const AdapterAPIs = require('../ExternalEvents/EventQueries');

//SINGLETON NO LLAMAR CONSTRUCTOR
class GestorIncidencias {

    constructor() {
    }

    /*
    getIncidencia(l, respuesta) {

        dataController.getUsuario(Email, respuesta);
    }
    */

    async createIncidencia(Latitud, Longitud, Fecha, Hora, NombreFenomeno, Api) {

        var nombre = await dataController.getFenomeno(NombreFenomeno).catch(error => { console.error(error) });


        if (nombre == NombreFenomeno) {
            
            var result = await dataController.createIncidencia(Latitud, Longitud, Fecha, Hora, nombre, Api).catch(error => { console.error(error) });
           
        } else {
            result = "Fenomeno no existe";
        }

        return result;
    }

    async getIncidencias(Latitud, Longitud, Gravedad, RadioEfecto) {

        var incidenciasfenomeno = [];

        var incid = await dataController.getIncidencias(Latitud, Longitud, Gravedad, RadioEfecto).catch(error => { console.error(error) });
        console.log(incid);

        if (incid) {
            console.log("hay incidencia");
            for ( var i = 0; i < incid.rows.length; i++) {
                //Fecha, Hora, NombreFenomeno, Descripcion, Radio, Gravedad, Latitud, Longitud
                var inc = new IncidenciaFenomeno(incid.rows[i].fecha, incid.rows[i].hora, incid.rows[i].nombrefen, incid.rows[i].descripcion, incid.rows[i].radioefecto, incid.rows[i].gravedad, incid.rows[i].latitud, incid.rows[i].longitud);
                incidenciasfenomeno.push(inc);
            }
        }
        console.log("incidencias:");
        console.log(incidenciasfenomeno);
        return incidenciasfenomeno;
    }

    async getIncidenciasFromAPIs() {

        await dataController.deleteIncidenciasFromAPIs();

        var incidenciasFenomeno = ExternalEvents.checkEventos();

        for (var i = 0; i < incidenciasFenomeno.length; i++) {

            var Latitud = incidenciasFenomeno[i].latitud;
            var Longitud = incidenciasFenomeno[i].longitud;
            var Fecha = incidenciasFenomeno[i].fecha;
            var Hora = incidenciasFenomeno[i].hora;
            var NombreFenomeno = incidenciasFenomeno[i].nombreFenomeno;


            createIncidencia(Latitud, Longitud, Fecha, Hora, NombreFenomeno, true);
        }

    }
}

module.exports = new GestorIncidencias;