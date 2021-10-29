
const Localizacion = require('./Localizacion');

class Filtro {

    localizacion1;
    localizacion2;

    constructor()
    {
        this.gravedad = 0;
        this.radioEfecto = 1;
    }

    setGravedad(g) {

        this.gravedad = g;
    }

    setRadioEfecto(r) {

        this.radioEfecto = r;
    }
        
}

module.exports = Filtro;