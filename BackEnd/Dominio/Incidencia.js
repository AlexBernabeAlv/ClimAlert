
const Localizacion = require('./Localizacion');


class Incidencia {

    constructor(Radio, Gravedad, Loc) {

        this.radio = Radio;
        this.gravedad = Gravedad;
        this.localizacion = Loc;
        //var setGravedad = function(valueGravedad){ this.gravedad = valueGravedad;}
    }
}



module.exports = Incidencia;