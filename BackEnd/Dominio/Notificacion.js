
var indicacionIncidencia = require('./IndicacionIncidencia');


function Notificacion(incid) {
    this.incidenciaFenomeno = incid;
    this.indicacionIncidencia = new indicacionIncidencia.IndicacionIncidencia('huye');
    //anadir indicaciones
}


module.exports = { Notificacion }