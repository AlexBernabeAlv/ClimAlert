
//SINGLETON NO LLAMAR CONSTRUCTOR
class ConsultorRefugios {

    constructor() {
    }

    static getInstance() {

        if (!this.instance) {
            this.instance = new ConsultorRefugios();
        }
        return this.instance;
    }
}

module.exports = ConsultorRefugios;