
const IncidenciaFenomeno = require('./IncidenciaFenomeno')
const dataController = require('../BD/DataController');
const AdapterAPIs = require('../ExternalEvents/ExternalEvents');
const GestorUsuarios = require('./GestorUsuarios')


//SINGLETON NO LLAMAR CONSTRUCTOR
class GestorIncidencias {

    constructor() {
    }

    /*
    getIncidencia(l, respuesta) {

        dataController.getUsuario(Email, respuesta);
    }
    */

    async createIncidencia(Latitud, Longitud, Fecha, Hora, NombreFenomeno, Valido, Api) {

        var nombre = await dataController.getFenomeno(NombreFenomeno).catch(error => { console.error(error) });


        if (nombre == NombreFenomeno) {

            var result = await dataController.createIncidencia(Latitud, Longitud, Fecha, Hora, nombre, Valido, Api)
                .catch(error => { console.error(error) });

        } else {
            result = "Fenomeno no existe";
        }

        return result;
    }

    async getIncidencias(Latitud, Longitud, Gravedad, RadioEfecto, Valido) {

        var incidenciasfenomeno = [];

        var incid = await dataController.getIncidencias(Latitud, Longitud, Gravedad, RadioEfecto, Valido)
            .catch(error => { console.error(error) });

        if (incid) {
            for (var i = 0; i < incid.rows.length; i++) {
                //Fecha, Hora, NombreFenomeno, Descripcion, Radio, Gravedad, Latitud, Longitud
                var inc = new IncidenciaFenomeno(incid.rows[i].fecha,
                    incid.rows[i].hora,
                    incid.rows[i].nombrefen,
                    incid.rows[i].descripcion,
                    incid.rows[i].radioefecto,
                    incid.rows[i].gravedad,
                    incid.rows[i].latitud,
                    incid.rows[i].longitud);
                incidenciasfenomeno.push(inc);
                if (incid.rows[i].valido) {
                    inc.setValido();
                }
            }
        }
        return incidenciasfenomeno;
    }

    async getIncidenciasFromAPIs() {

        await dataController.deleteIncidenciasFromAPIs().catch(error => { console.error(error) });

        var incidenciasFenomeno = await AdapterAPIs.checkEventos();

        for (var i = 0; i < incidenciasFenomeno.length; i++) {

            var Latitud = incidenciasFenomeno[i].incidencia.localizacion.latitud;
            var Longitud = incidenciasFenomeno[i].incidencia.localizacion.longitud;
            var Fecha = incidenciasFenomeno[i].fecha;
            var Hora = incidenciasFenomeno[i].hora;
            var NombreFenomeno = incidenciasFenomeno[i].fenomenoMeteo.nombre;
            var valido = incidenciasFenomeno[i].valido;
            var api = incidenciasFenomeno[i].API;

            await this.createIncidencia(Latitud, Longitud, Fecha, Hora, NombreFenomeno, valido, api)
                .catch(error => { console.error(error) });
        }
    }

    async getIncidenciasNoValidas(Email, Password, Latitud, Longitud) {

        var usuario = await GestorUsuarios.getUsuario(Email).catch(error => { console.error(error) });

        if (usuario.isAdmin && usuario.password == Password) {

            return await this.getIncidencias(Latitud, Longitud, 0, usuario.filtro.radioEfecto, false).catch(error => { console.error(error) });
        }

        return false;
    }

    async updateIncidencia(Email, Password, Id, Gravedad) {

        var usuario = await GestorUsuarios.getUsuario(Email).catch(error => { console.error(error) });

        if (usuario.isAdmin && usuario.password == Password) {

            return await dataController.updateIncidencia(Id, Gravedad).catch(error => { console.error(error) });
        }

        return false;

    }

}

module.exports = new GestorIncidencias;