//var nombre
//var descripcion
//var consejos

const ObjetoUtil = require('./ObjetoUtil');

class FenomenoMeteo{

    constructor(Nombre, Descripcion, Consejos, Objetos)
    {
        this.nombre = Nombre;
        this.descripcion = Descripcion
        this.consejos = [];

        for (var i = 0; i < Consejos.length; i++) {

            this.consejos.push(Consejos[i]);
        }
        
        this.objetosUtiles = [];
        for (var i = 0; i < Objetos.length; i++) {

            this.objetosUtiles.push(Consejos[i]);
        }
    }
    
}

module.exports = FenomenoMeteo;