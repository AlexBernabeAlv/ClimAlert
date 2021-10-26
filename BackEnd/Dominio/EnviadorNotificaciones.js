
const Notificacion = require('./Notificacion');

//SINGLETON NO LLAMAR CONSTRUCTOR
class EnviadorNotificaciones {

    constructor(notif) {
        this.notificacion = notif;
    }

    static getInstance() {

        if (!this.instance) {
            this.instance = new EnviadorNotificaciones();
        }
        return this.instance;
    }
}

module.exports = EnviadorNotificaciones;