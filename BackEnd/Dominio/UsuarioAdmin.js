
const Usuario = require('./Usuario');
//const GestorIncidenciasUsuarioAdmin = require('./GestorIncidenciasUsuarioAdmin');

class UsuarioAdmin extends Usuario {

    constructor(Email, Passwd) {

        super(Email, Passwd);
        this.admin = true;
    }

    isAdmin() {
        return true;
    }

}

module.exports = UsuarioAdmin;