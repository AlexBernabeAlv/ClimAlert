
const Localizacion = require('./Localizacion');

class Refugio {

    constructor(Nombre, Latitud, Longitud) {
        this.nombre = Nombre;
        this.localizacion = new Localizacion(Latitud, Longitud);
    }
}

module.exports = Refugio;