
const Usuario = require('./Usuario');
const GestorIncidenciasUsuarioAdmin = require('./GestorIncidenciasUsuarioAdmin');

class UsuarioAdmin extends Usuario {

    constructor(Email, Passwd) {

        super(Email, Passwd);
    }

    setEmail(Email) {
        this.email = Email;
    }

    isAdmin() {
        return true;
    }

}

module.exports = UsuarioAdmin;