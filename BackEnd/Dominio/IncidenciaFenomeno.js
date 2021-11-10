
const FenomenoMeteo = require('./FenomenoMeteo');
const Incidencia = require('./Incidencia');
const Notificacion = require('./Notificacion');
const GestorIncidencias = require('./GestorIncidencias');
const ConsultorRefugios = require('./ConsultorRefugios');
const AdapterPrestamos = require('./AdapterPrestamos');

class IncidenciaFenomeno{

    #notif;

    constructor(Fecha, Hora, NombreFenomeno, Radio, Gravedad, Loc)
    {
        this.valido = false;
        this.fecha = Fecha;
        this.hora = Hora;
        this.incidencia = new Incidencia(Radio, Gravedad, Loc);
        this.fenomenoMeteo = new FenomenoMeteo(NombreFenomeno, "descripcion...");
        this.#notif = new Notificacion(this, "indicaciones");
    }
    

    getNotificacion() {
        return this.#notif;
    }

    setValido(){
        this.valido = true;
    }
}

module.exports = IncidenciaFenomeno;