
const Usuario = require('./Usuario');

class UsuarioEstandar extends Usuario {

    constructor(Email, Passwd) {

        super(Email, Passwd);
        this.admin = false;
    }

    isAdmin() {
        return false;
    }

}

module.exports = UsuarioEstandar;