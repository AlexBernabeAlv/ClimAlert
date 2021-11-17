
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

    setLocalizacion1(latitud, longitud) {

        this.localizacion1 = new Localizacion(latitud, longitud);
    }

    setLocalizacion2(latitud, longitud) {

        this.localizacion2 = new Localizacion(latitud, longitud);

    }
        
}

module.exports = Filtro;