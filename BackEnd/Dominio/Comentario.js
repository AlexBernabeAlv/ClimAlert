
class Comentario {

    constructor(Id, Email, Incfenid, IdResponseComment, Contenido, Fecha, Hora) {

        this.id = Id;
        this.email = Email;
        this.incfenid = Incfenid;
        this.idresponsecomment = IdResponseComment;
        this.contenido = Contenido;
        this.fecha = Fecha;
        this.hora = Hora;
    }

}

module.exports = Comentario;