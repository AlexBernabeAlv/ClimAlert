
const IndicacionIncidencia = require('./IndicacionIncidencia');
const IncidenciaFenomeno = require('./IncidenciaFenomeno');

class Notificacion {

    constructor(incid, Indicacion, ObjetoPrestado){
        this.incidenciaFenomeno = incid;
        this.indicacionIncidencia = Indicacion;
        this.objetoPrestado = ObjetoPrestado;
        //anadir indicaciones
    }
}


module.exports = Notificacion;