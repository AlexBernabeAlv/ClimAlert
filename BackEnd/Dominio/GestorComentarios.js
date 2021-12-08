
const dataController = require('../BD/DataController');
const GestorUsuarios = require('./GestorUsuarios')
const Comentario = require('./Comentario')


//SINGLETON NO LLAMAR CONSTRUCTOR
class GestorComentarios {

    constructor() {
    }


    async createComentario(Email, Password, Incfenid, ComentResponseId, Contenido) {

        var usu = await GestorUsuarios.getUsuario(Email);

        if (usu && usu.password == Password) {

            var c;

            if (ComentResponseId) {

                c = await this.getComentario(ComentResponseId);
            }

            if (!ComentResponseId || c) {

                console.log("He entrado");
                var com = new Comentario(0, Email, Incfenid, ComentResponseId, Contenido);
                console.log(com);
                var id = await dataController.createComentario(com).catch(error => { console.error(error) });
                console.log(id);
                if (id == null) return false;
                var result = new Comentario(id, Email, Incfenid, ComentResponseId, Contenido);
                console.log(result);
                return result;
            }

            
        }
        console.log("No he entrado");
        return false;
    }

    async getComentario(Id) {

        var comentario = await dataController.getComentario(Id).catch(error => { console.error(error) });
        console.log(comentario.rows.length);
        if (comentario.rows.length > 0) {

            return new Comentario(comentario.rows[0].id, comentario.rows[0].emailusr, comentario.rows[0].incfenid, comentario.rows[0].comentresponseid, comentario.rows[0].contenido);
        }
        return false;
    }

    async getComentariosIncidenciaFenomeno(Incfenid) {

        var comentarios = await dataController.getComentariosByIncFenId(Incfenid).catch(error => { console.error(error) });

        var result = [];

        for (var i = 0; i < comentarios.rows.length; i++) {

            result.push(new Comentario(comentarios.rows[i].id, comentarios.rows[i].emailusr, comentarios.rows[i].incfenid, comentarios.rows[i].comentresponseid, comentarios.rows[i].contenido));
        }

        return result;
    }

    async getComentariosUsuario(Email) {
        
        var comentarios = await dataController.getComentariosByUsuario(Email).catch(error => { console.error(error) });

        var result = [];

        for (var i = 0; i < comentarios.rows.length; i++) {

            result.push(new Comentario(comentarios.rows[i].id, comentarios.rows[i].emailusr, comentarios.rows[i].incfenid, comentarios.rows[i].comentresponseid, comentarios.rows[i].contenido));
        }

        return result;
    }

    async getComentariosComentario(CommentId) {

        var comentarios = await dataController.getComentariosByComentario(CommentId).catch(error => { console.error(error) });

        var result = [];

        for (var i = 0; i < comentarios.rows.length; i++) {

            result.push(new Comentario(comentarios.rows[i].id, comentarios.rows[i].emailusr, comentarios.rows[i].incfenid, comentarios.rows[i].comentresponseid, comentarios.rows[i].contenido));
        }

        return result;
    }

    async deleteComentario(CommentId, Email, Password) {

        var usu = await GestorUsuarios.getUsuario(Email);

        if (usu && usu.password == Password) {

            var numedeleted = await dataController.updateComentario(CommentId, "[deleted]", usu.email).catch(error => { console.error(error) });

            if (numedeleted == 0) {
                return "Ningun comentario borrado";
            }
            return "Comentario borrado";
        }

        return "Usuario incorrecto";
    }

    async editComentario(CommentId, Contenido, Email, Password) {

        var usu = await GestorUsuarios.getUsuario(Email);

        if (usu && usu.password == Password) {

            var numedited = await dataController.updateComentario(CommentId, Contenido, usu.email).catch(error => { console.error(error) });

            if (numedited == 0) {
                return "Ningun comentario editado";
            }
            return "Comentario editado";
        }

        return "Usuario incorrecto";
    }
    
}

module.exports = new GestorComentarios;
