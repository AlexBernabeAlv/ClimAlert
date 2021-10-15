
var fenomenoMeteo = require('./FenomenoMeteo');
var incidencia = require('./Incidencia');
var notificacion = require('./Notificacion');


function IncidenciaFenomeno(Fecha, Hora) {
    this.valido = false;
    this.fecha = Fecha;
    this.hora = Hora;
    this.incidencia = new incidencia.Incidencia(1, 1);
    this.fenomenoMeteo = new fenomenoMeteo.FenomenoMeteo('Diluvio Universal');

    var notif = new notificacion.Notificacion(this);

    this.getNotificacion = function() {
        return notif;
    }

    this.setValido = function(){
        this.valido = true;
    }
}

function getNotificacion() {
    return notif;
}

module.exports = { IncidenciaFenomeno };