
const IndicacionIncidencia = require('./IndicacionIncidencia');
const IncidenciaFenomeno = require('./IncidenciaFenomeno');

class Notificacion {

    constructor(IncidenciaFenomeno, Id) {
        this.id = Id;
        this.incidenciaFenomeno = IncidenciaFenomeno;
        this.indicacionIncidencia = [];
    }

    addIndicacion(Indicacion, Refugio) {
        indicacion = new IndicacionIncidencia(Indicacion, Refugio);
        indicacionIncidencia.push(indicacion);
    }
}


module.exports = Notificacion;