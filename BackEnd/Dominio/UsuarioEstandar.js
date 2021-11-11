
const Usuario = require('./Usuario');
const GestorIncidencias = require('./GestorIncidencias');

class UsuarioEstandar extends Usuario {

    constructor(Email, Passwd) {

        super(Email, Passwd);
    }

    setEmail(Email) {
        this.email = Email;
    }

    isAdmin() {
        return false;
    }

}

module.exports = UsuarioEstandar;