
const GestorIncidencias = require('./GestorIncidencias');

//SINGLETON NO LLAMAR CONSTRUCTOR
class GestorIncidenciasUsuarioAdmin extends GestorIncidencias {
    constructor() {
        
    }

    static getInstance() {

        if (!this.instance) {
            this.instance = new GestorIncidenciasUsuarioAdmin();
        }
        return this.instance;
    }
}

module.exports = GestorIncidenciasUsuarioAdmin;