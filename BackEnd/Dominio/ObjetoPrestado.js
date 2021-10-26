
const Localizacion = require('./Localizacion');

class ObjetoPrestado {
    constructor(Nombre, Loc) {
        this.nombre = Nombre;
        this.localizacion = Loc;
    }
}

module.exports = ObjetoPrestado;