
const ObjetoPrestado = require('./ObjetoPrestado');
const Refugio = require('./Refugio');

class IndicacionIncidencia {

    constructor(Indicacion, ObjetoPrestado, Refugio){
        this.indicacion = Indicacion;
        this.objetoPrestado = ObjetoPrestado;
        this.refugio = Refugio;
    }
}

module.exports = IndicacionIncidencia;