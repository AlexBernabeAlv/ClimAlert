
const Localizacion = require('./Localizacion');

class Refugio {

    constructor(Nombre, Localizacion) {
        this.nombre = Nombre;
        this.localizacion = Localizacion;
    }
}

module.exports = Refugio;