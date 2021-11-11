
const IndicacionIncidencia = require('./IndicacionIncidencia');
const IncidenciaFenomeno = require('./IncidenciaFenomeno');

class Notificacion {

    constructor(incid, indicacion){
        this.incidenciaFenomeno = incid;
        this.indicacionIncidencia = [];
        indicacion = new IndicacionIncidencia(indicacion, null);
        indicacionIncidencia.push(indicacion);
    }
}


module.exports = Notificacion;