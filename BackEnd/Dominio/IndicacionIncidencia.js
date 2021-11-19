
const Refugio = require('./Refugio');

class IndicacionIncidencia {

    constructor(Indicacion, Refugio, Latitud, Longitud){
        this.indicacion = Indicacion;
        this.refugio = new Refugio(Refugio, Latitud, Longitud);
    }
}

module.exports = IndicacionIncidencia;