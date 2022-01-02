
const GestorIncidencias = require('./GestorIncidencias');

//SINGLETON NO LLAMAR CONSTRUCTOR
class GestorIncidenciasUsuarioAdmin extends GestorIncidencias {

    constructor() {
        super();
    }
}

module.exports = new GestorIncidenciasUsuarioAdmin;