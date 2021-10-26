
const Usuario = require('./Usuario');

//SINGLETON NO LLAMAR CONSTRUCTOR
class GestorUsuarios {

    constructor() {

        this.usuario;
    }

    static getInstance() {

        if (!this.instance) {
            this.instance = new GestorUsuarios();
        }
        return this.instance;
    }
}

module.exports = GestorUsuarios;