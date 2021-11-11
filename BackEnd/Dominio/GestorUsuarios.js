
const UsuarioEstandar = require('./UsuarioEstandar');
const UsuarioAdmin = require('./UsuarioAdmin');
const dataController = require('../BD/DataController');

//SINGLETON NO LLAMAR CONSTRUCTOR
class GestorUsuarios {

    constructor() {
    }


    getUsuario(Email, respuesta) {

        dataController.getUsuario(Email, respuesta);
    }

    createUsuario(Email, Password, respuesta) {

        var usu = new UsuarioEstandar(Email, Password);
        dataController.createUsuario(usu, respuesta);
    }

    deleteUsuario(Email, Password, respuesta) {

        dataController.deleteUsuario(Email, Password, respuesta);
    }
}

module.exports = new GestorUsuarios;