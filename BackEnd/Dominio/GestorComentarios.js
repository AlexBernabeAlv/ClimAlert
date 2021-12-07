
const dataController = require('../BD/DataController');
const GestorUsuarios = require('./GestorUsuarios')
const Comentario = require('./Comentario')


//SINGLETON NO LLAMAR CONSTRUCTOR
class GestorComentarios {

    constructor() {
    }


    async createComentario(Email, Password, Incfenid, ComentResponseId, Contenido) {
        var usu = GestorUsuarios.getUsuario(Email);

        if (usu && usu.password == Password) {

            //Id, Email, Incfenid, IdComment, Contenido
            var com = new Comentario(0, Email, Incfenid, ComentResponseId, Contenido);

            var coment = await dataController.createComentario(com).catch(error => { console.error(error) });

            var result = new Comentario(coment.rows[0].id, Email, Incfenid, ComentResponseId, Contenido);

            return result;
        }

        return false;
    }
    /*

    async getComentariosIncidenciaFenomeno(Incfenid) {

        var comentarios = await dataController.getComentariosByIncFenId(Incfenid).catch(error => { console.error(error) });

        var result[];

        for (var i = 0; i < comentarios.rows.length; i++) {

            result.push(new Comentario(comentarios.rows[i].id, comentarios.rows[i].emailusr, comentarios.rows[i].incfenid, comentarios.rows[i].comentresponseid, comentarios.rows[i].contenido));
        }

        return result;
    }

    async getComentariosUsuario(Email) {

        var comentarios = await dataController.getComentariosByUsuario(Email).catch(error => { console.error(error) });

        var result[];

        for (var i = 0; i < comentarios.rows.length; i++) {

            result.push(new Comentario(comentarios.rows[i].id, comentarios.rows[i].emailusr, comentarios.rows[i].incfenid, comentarios.rows[i].comentresponseid, comentarios.rows[i].contenido));
        }

        return result;
    }

    async getComentariosComentario(CommentId) {

        var comentarios = await dataController.getComentariosByComentario(CommentId).catch(error => { console.error(error) });

        var result[];

        for (var i = 0; i < comentarios.rows.length; i++) {

            result.push(new Comentario(comentarios.rows[i].id, comentarios.rows[i].emailusr, comentarios.rows[i].incfenid, comentarios.rows[i].comentresponseid, comentarios.rows[i].contenido));
        }

        return result;
    }

    async deleteComentario(CommentId) {

        return await dataController.updateComentario(CommentId, "[deleted]").catch(error => { console.error(error) });
    }

    async editComentario(CommentId, Contenido) {

        return await dataController.updateComentario(CommentId, Contenido).catch(error => { console.error(error) });
    }
    */
}

module.exports = new GestorComentarios;
