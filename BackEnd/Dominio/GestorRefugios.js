
const Refugio = require('./Refugio');

//SINGLETON NO LLAMAR CONSTRUCTOR
class GestorRefugios {

    constructor() {

        this.refugio;
    }

    static getInstance() {

        if (!this.instance) {
            this.instance = new GestorRefugios();
        }
        return this.instance;
    }
}

module.exports = GestorRefugios;