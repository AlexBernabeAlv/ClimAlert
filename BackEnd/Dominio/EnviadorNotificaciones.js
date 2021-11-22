
const Notificacion = require('./Notificacion');
const GestorUsuarios = require('./GestorUsuarios');
const GestorIncidencias = require('./GestorIncidencias');

//SINGLETON NO LLAMAR CONSTRUCTOR
class EnviadorNotificaciones {

    constructor() {
    }

    async getNotificaciones(Email, Password, Latitud, Longitud) {

        var usuario = await GestorUsuarios.getUsuario(Email);
        console.log(usuario);
        console.log(Password);
        var notificaciones = [];
        
        if (usuario && usuario.password == Password) {
            console.log("buscando incidencias");
            var incidencias0 = await GestorIncidencias.getIncidencias(Latitud, Longitud, usuario.filtro.gravedad, usuario.filtro.radioEfecto);

            if (usuario.filtro.localizacion1 && usuario.filtro.localizacion1.latitud && usuario.filtro.localizacion1.longitud) {

                var incidencias1 = await GestorIncidencias.getIncidencias(usuario.filtro.localizacion1.latitud, usuario.filtro.localizacion1.longitud, usuario.filtro.gravedad, usuario.filtro.radioEfecto);
            }

            if (usuario.filtro.localizacion2 && usuario.filtro.localizacion2.latitud && usuario.filtro.localizacion2.longitud) {

                var incidencias2 = await GestorIncidencias.getIncidencias(usuario.filtro.localizacion2.latitud, usuario.filtro.localizacion2.longitud, usuario.filtro.gravedad, usuario.filtro.radioEfecto);
            }


            if (incidencias0) {
                for (var i = 0; i < incidencias0.length; i++) {
                    notificaciones.push(new Notificacion(incidencias0[i]));
                }
            }
            if (incidencias1) {
                for (var i = 0; i < incidencias1.length; i++) {
                    notificaciones.push(new Notificacion(incidencias1[i]));
                }
            }
            if (incidencias2) {
                for (var i = 0; i < incidencias2.length; i++) {
                    notificaciones.push(new Notificacion(incidencias2[i]));
                }
            }

            console.log("notificaciones:");
            console.log(notificaciones);
            return notificaciones;
        }

    }
}

module.exports = new EnviadorNotificaciones;