
//SINGLETON NO LLAMAR CONSTRUCTOR
class AdapterPrestamos {

    constructor() {
        
    }

    static getInstance() {

        if (!this.instance) {
            this.instance = new AdapterPrestamos();
        }
        return this.instance;
    }
}

module.exports = AdapterPrestamos;