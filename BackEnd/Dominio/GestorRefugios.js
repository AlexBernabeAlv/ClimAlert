
const Refugio = require('./Refugio');
const dataController = require('../BD/DataController');
const GestorUsuarios = require('./GestorUsuarios');
const Usuario = require('./Usuario');
const UsuarioEstandar = require('./UsuarioEstandar');
const UsuarioAdmin = require('./UsuarioAdmin');



//SINGLETON NO LLAMAR CONSTRUCTOR
class GestorRefugios {

    constructor() {
    }

    async createRefugio(Email, Password, Nombre, Latitud, Longitud) {

        var usu = await GestorUsuarios.getUsuario(Email);
        if (usu.isAdmin && usu.password == Password) {

            var refugio = new Refugio(Nombre, Latitud, Longitud);
            var result = await dataController.createRefugio(refugio).catch(error => { console.error(error) });

            if (!result) return 400;
            return result;

        } else {

            return 401;
        }
    }

    async deleteRefugio(Email, Password, Nombre) {

        if (!Password) return 401;

        var usu = await GestorUsuarios.getUsuario(Email);
        if (usu.isAdmin && usu.password == Password) {

            return await dataController.deleteRefugio(Nombre).catch(error => { console.error(error) });
        } else {

            return 401;
        }
    }

    async getRefugioByLoc(Latitud, Longitud) {

        return await dataController.getRefugioByLoc(Latitud, Longitud).catch(error => { console.error(error) });
    }

    async getRefugios(Email, Password) {

        if (!Password) return 401;

        var usu = await GestorUsuarios.getUsuario(Email);

        if (!usu.isAdmin) return 403;

        else if (usu.password == Password) {

            var refugios = await dataController.getRefugios().catch(error => { console.error(error) });

            var refs = [];

            for (var i = 0; i < refugios.rows.length; i++) {

                refs.push(new Refugio(refugios.rows[i].nombre, refugios.rows[i].latitud, refugios.rows[i].longitud));
            }

            return refs;

        } else {

            return 401;
        }
    }
}

module.exports = new GestorRefugios;