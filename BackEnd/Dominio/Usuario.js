
const Filtro = require('./Filtro');
//const GestorUsuarios = require('./GestorUsuarios');
const Notificacion = require('./Notificacion');

class Usuario {

    constructor(Email, Passwd) {

        this.email = Email;
        this.password = Passwd;
        this.filtro = new Filtro();
    }

    isAdmin() {
    }

    setFiltro(g, r) {

        this.filtro.setGravedad(g);
        this.filtro.setRadioEfecto(r);
    }

}

module.exports = Usuario;