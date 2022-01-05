
const dataController = require('../BD/DataController');
const GestorUsuarios = require('./GestorUsuarios')
const Comentario = require('./Comentario')


//SINGLETON NO LLAMAR CONSTRUCTOR
class GestorComentarios {

    constructor() {
    }


    async createComentario(Email, Password, Incfenid, ComentResponseId, Contenido, Fecha, Hora) {

        var usu = await GestorUsuarios.getUsuario(Email);

        if (usu && usu.password == Password) {

            var c;

            if (ComentResponseId) {

                c = await this.getComentario(ComentResponseId);
                if (typeof c == 'number') return c;
            }
            if (!ComentResponseId || c) {

                var com = new Comentario(0, Email, Incfenid, ComentResponseId, Contenido, Fecha, Hora);

                var id = await dataController.createComentario(com).catch(error => { console.error(error) });

                if (!id) return 400;

                var result = new Comentario(id, Email, Incfenid, ComentResponseId, Contenido, Fecha, Hora);
                return result;
            }

            
        }

        return 401;
    }

    async getComentario(CommentId) {

        var comentario = await dataController.getComentario(CommentId).catch(error => { console.error(error) });

        if (comentario.rows.length > 0) {

            return new Comentario(comentario.rows[0].id, comentario.rows[0].emailusr, comentario.rows[0].incfenid, comentario.rows[0].comentresponseid, comentario.rows[0].contenido, comentario.rows[0].fecha, comentario.rows[0].hora);
        }
        return 404;
    }

    async getComentariosIncidenciaFenomeno(Incfenid) {

        var comentarios = await dataController.getComentariosByIncFenId(Incfenid).catch(error => { console.error(error) });

        if (!comentarios) return 404;

        var result = [];

        for (var i = 0; i < comentarios.rows.length; i++) {

            result.push(new Comentario(comentarios.rows[i].id, comentarios.rows[i].emailusr, comentarios.rows[i].incfenid, comentarios.rows[i].comentresponseid, comentarios.rows[i].contenido, comentarios.rows[0].fecha, comentarios.rows[0].hora));
        }

        return result;
    }

    async getComentariosUsuario(Email) {

        var usu = await dataController.getUsuario(Email)

        if (usu.rows.length == 0) return 404;

        var comentarios = await dataController.getComentariosByUsuario(Email).catch(error => { console.error(error) });

        var result = [];

        for (var i = 0; i < comentarios.rows.length; i++) {

            result.push(new Comentario(comentarios.rows[i].id, comentarios.rows[i].emailusr, comentarios.rows[i].incfenid, comentarios.rows[i].comentresponseid, comentarios.rows[i].contenido, comentarios.rows[0].fecha, comentarios.rows[0].hora));
        }

        return result;
    }

    async getComentariosComentario(CommentId) {

        var com = await this.getComentario(CommentId);

        if (typeof com == 'number') return 404;

        var comentarios = await dataController.getComentariosByComentario(CommentId).catch(error => { console.error(error) });

        var result = [];

        for (var i = 0; i < comentarios.rows.length; i++) {

            result.push(new Comentario(comentarios.rows[i].id, comentarios.rows[i].emailusr, comentarios.rows[i].incfenid, comentarios.rows[i].comentresponseid, comentarios.rows[i].contenido, comentarios.rows[0].fecha, comentarios.rows[0].hora));
        }

        return result;
    }

    async getComentariosByCreador(Email, Password, EmailCreador, Filtro) {

        console.log(Email + ' ' + Password + ' ' + EmailCreador);

        var usuario = await GestorUsuarios.getUsuario(Email).catch(error => { console.error(error) });

        if (typeof usuario == 'number') return usuario;

        if (usuario.password == Password) {

            if (usuario.admin || usuario.email == EmailCreador) {

                console.log(usuario.email == EmailCreador);

                console.log(usuario.admin);

                if (Filtro == 'dia') {

                    var result = await dataController.getComentariosByCreadorGroupDia(EmailCreador).catch(error => { console.error(error) });
                }
                else if (Filtro == 'minuto') {

                    var result = await dataController.getComentariosByCreadorGroupMinuto(EmailCreador).catch(error => { console.error(error) });
                }

                if (result) return result;
                return 400;
            }

            return 403;
        }

        return 401;
    }

    async deleteComentario(CommentId, Email, Password) {

        var usu = await GestorUsuarios.getUsuario(Email);

        if (typeof usu == 'number') return usu;

        if (usu.password == Password) {

            var numedeleted = await dataController.updateComentario(CommentId, "[deleted]", usu.email).catch(error => { console.error(error) });

            if (numedeleted == 0) {
                return 404;
            }
            return 200;
        }

        return 401;
    }

    async editComentario(CommentId, Contenido, Email, Password) {

        var usu = await GestorUsuarios.getUsuario(Email);

        if (typeof usu == 'number') return usu;

        if (usu.password == Password) {

            var numedited = await dataController.updateComentario(CommentId, Contenido, usu.email).catch(error => { console.error(error) });

            if (numedited == 0) {
                return 404;
            }
            return 200;
        }

        return 401;
    }
    
}

module.exports = new GestorComentarios;
