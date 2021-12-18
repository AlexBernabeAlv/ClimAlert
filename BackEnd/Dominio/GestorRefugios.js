
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
            return await dataController.createRefugio(refugio).catch(error => { console.error(error) });
        } else {

            return false;
        }
    }

    async updateRefugio() {

        var usu = await GestorUsuarios.getUsuario(Email);
        if (usu.isAdmin && usu.password == Password) {

            var refugio = new Refugio(Nombre, Latitud, Longitud);
            return await dataController.updateRefugio(refugio).catch(error => { console.error(error) });
        } else {

            return false;
        }
    }

    async deleteRefugio(Email, Password, Nombre) {

        var usu = await GestorUsuarios.getUsuario(Email);
        if (usu.isAdmin && usu.password == Password) {

            return await dataController.deleteRefugio(Nombre).catch(error => { console.error(error) });
        } else {

            return false;
        }
    }

    async getRefugioByLoc(Latitud, Longitud) {

        return await dataController.getRefugioByLoc(Latitud, Longitud).catch(error => { console.error(error) });
    }
}

module.exports = new GestorRefugios;