
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
}

module.exports = new GestorUsuarios;