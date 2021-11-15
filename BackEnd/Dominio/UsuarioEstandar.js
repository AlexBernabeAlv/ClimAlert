
const Usuario = require('./Usuario');

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