
const Filtro = require('./Filtro');
const GestorUsuarios = require('./GestorUsuarios');
const Notificacion = require('./Notificacion');

class Usuario {

    constructor(Email) {
        this.email = Email;
        this.notificaciones = [];
        
    }

    setNotificacion(notif) {

        this.notificaciones.push(notif);
    }

}

module.exports = Usuario;