
const IncidenciaFenomeno = require('./IncidenciaFenomeno')

//SINGLETON NO LLAMAR CONSTRUCTOR
class GestorIncidencias {

    constructor() {

        this.incidenciasFenomenos = [];
    }

    static getInstance() {

        if (!this.instance) {
            this.instance = new GestorIncidencias();
        }
        return this.instance;
    }
}

module.exports = GestorIncidencias;