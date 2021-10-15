
var localizacion = require('./Localizacion');


function Incidencia(Radio, Gravedad) {


    this.radio = Radio;
    this.gravedad = Gravedad;
    this.localizacion = new localizacion.Localizacion(41.377349232394856, 2.1437691962266148);
    //var setGravedad = function(valueGravedad){ this.gravedad = valueGravedad;}
}



module.exports = { Incidencia };