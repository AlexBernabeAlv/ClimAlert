
const Usuario = require('./Usuario');
const GestorIncidencias = require('./GestorIncidencias');

class UsuarioEstandar extends Usuario{

    constructor(Email) {
        super(Email);
    }

    setEmail(Email) {
        this.email = Email;
        
    }

}

module.exports = UsuarioEstandar;