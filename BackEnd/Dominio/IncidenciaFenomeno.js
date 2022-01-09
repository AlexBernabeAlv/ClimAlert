
const FenomenoMeteo = require('./FenomenoMeteo');
const Incidencia = require('./Incidencia');
const Notificacion = require('./Notificacion');
//const GestorIncidencias = require('./GestorIncidencias');
const ConsultorRefugios = require('./ConsultorRefugios');
const AdapterPrestamos = require('./AdapterPrestamos');

class IncidenciaFenomeno{

    constructor(Id, Fecha, Hora, NombreFenomeno, Descripcion, Radio, Gravedad, Latitud, Longitud, crea, med)
    {
        this.id = Id;
        this.valido = false;
        this.API = false;
        this.fecha = Fecha;
        this.hora = Hora;
        this.incidencia = new Incidencia(Radio, Gravedad, Latitud, Longitud);
        this.fenomenoMeteo = new FenomenoMeteo(NombreFenomeno, Descripcion, null, null);
        this.creador = crea;
        this.medida = med;

    }

    setValido(){
        this.valido = true;
    }

    setAPI() {
        this.API = true;
    }

    setMedida(Medida) {
        this.medida = Medida;
    }

    setCreador(Creador) {
        this.creador = Creador;
    }
}

module.exports = IncidenciaFenomeno;