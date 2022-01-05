
const Localizacion = require('./Localizacion');


class Incidencia {

    constructor(Radio, Gravedad, Latitud, Longitud) {

        this.radio = Radio;
        this.gravedad = Gravedad;
        this.localizacion = new Localizacion(Latitud, Longitud);
        //var setGravedad = function(valueGravedad){ this.gravedad = valueGravedad;}
    }
}



module.exports = Incidencia;