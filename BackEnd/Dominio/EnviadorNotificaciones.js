
const Notificacion = require('./Notificacion');
const GestorUsuarios = require('./GestorUsuarios');
const GestorIncidencias = require('./GestorIncidencias');

//SINGLETON NO LLAMAR CONSTRUCTOR
class EnviadorNotificaciones {

    constructor() {

    }

    async getNotificaciones(Email, Password, Latitud, Longitud) {

        var usuario = await GestorUsuarios.getUsuario(Email);

        var notificaciones = [];

        var setNotificaciones = new Set();

        if (usuario && usuario.password == Password) {

            var incidencias0 = await GestorIncidencias.getIncidencias(Latitud, Longitud, usuario.filtro.gravedad, usuario.filtro.radioEfecto, true);

            if (usuario.filtro.localizacion1 && usuario.filtro.localizacion1.latitud && usuario.filtro.localizacion1.longitud) {

                var incidencias1 = await GestorIncidencias.getIncidencias(usuario.filtro.localizacion1.latitud, usuario.filtro.localizacion1.longitud, usuario.filtro.gravedad, usuario.filtro.radioEfecto, true);
            }

            if (usuario.filtro.localizacion2 && usuario.filtro.localizacion2.latitud && usuario.filtro.localizacion2.longitud) {

                var incidencias2 = await GestorIncidencias.getIncidencias(usuario.filtro.localizacion2.latitud, usuario.filtro.localizacion2.longitud, usuario.filtro.gravedad, usuario.filtro.radioEfecto, true);
            }


            if (incidencias0) {
                for (var i = 0; i < incidencias0.length; i++) {

                    if (!setNotificaciones.has(incidencias0[i].id)) {

                        setNotificaciones.add(incidencias0[i].id);
                        notificaciones.push(new Notificacion(incidencias0[i]));
                        
                    }

                }
            }
            if (incidencias1) {
                for (var i = 0; i < incidencias1.length; i++) {

                    if (!setNotificaciones.has(incidencias1[i].id)) {

                        setNotificaciones.add(incidencias1[i].id);
                        notificaciones.push(new Notificacion(incidencias1[i]));

                    }
                }
            }
            if (incidencias2) {
                for (var i = 0; i < incidencias2.length; i++) {

                    if (!setNotificaciones.has(incidencias2[i].id)) {

                        setNotificaciones.add(incidencias2[i].id);
                        notificaciones.push(new Notificacion(incidencias2[i]));

                    }
                }
            }

            return notificaciones;
        }

    }
}

module.exports = new EnviadorNotificaciones;