
const UsuarioEstandar = require('./UsuarioEstandar');
const UsuarioAdmin = require('./UsuarioAdmin');
const dataController = require('../BD/DataController');

//SINGLETON NO LLAMAR CONSTRUCTOR
class GestorUsuarios {

    constructor() {
    }


    async getUsuario(Email) {
        
        var res = await dataController.getUsuario(Email);



        var usuario;

        if (res.rows.length == 0) {

            return false;

        } else if(res.rows[0].isAdmin){

            usuario = new UsuarioAdmin(res.rows[0].email, res.rows[0].password);

        } else {

            usuario = new UsuarioEstandar(res.rows[0].email, res.rows[0].password);
        }

        usuario.setFiltro(res.rows[0].gravedad, res.rows[0].radioefecto);
        usuario.filtro.setLocalizacion1(res.rows[0].latitud, res.rows[0].longitud);
        usuario.filtro.setLocalizacion2(res.rows[1].latitud, res.rows[1].longitud);


        return usuario;
    }

    async createUsuario(Email, Password) {

        var usu = new UsuarioEstandar(Email, Password);
        return await dataController.createUsuario(usu).catch(error => { console.error(error) });
    }

    async updateUsuario(Email, Password, Gravedad, RadioEfecto) {

        var usu = new UsuarioEstandar(Email, Password);
        usu.setFiltro(Gravedad, RadioEfecto);

        return await dataController.updateUsuario(usu).catch(error => { console.error(error) });
    }

    async deleteUsuario(Email, Password) {

        return await dataController.deleteUsuario(Email, Password).catch(error => { console.error(error) });
    }

    async createLocalizacionUsuario(email, psswd, lat1, lon1, lat2, lon2) {

        var usuario = await this.getUsuario(email);

        if (usuario && usuario.password == psswd) {

            var result = await dataController.createLocalizacionesUsuario(email, lat1, lon1, lat2, lon2);
        } else {

            var result = "Usuario no existe";
        }

        return result;

    }

    async updateLocalizacionesUsuario(email, psswd, lat1, lon1, lat2, lon2) {

        var usuario = await this.getUsuario(email);

        if (usuario && usuario.password == psswd) {

            var result = await dataController.updateLocalizacionesUsuario(email, lat1, lon1, lat2, lon2);

        } else {

            var result = "Usuario no existe";
        }

        return result;

    }
}

module.exports = new GestorUsuarios;