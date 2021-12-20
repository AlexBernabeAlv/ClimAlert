
const UsuarioEstandar = require('./UsuarioEstandar');
const UsuarioAdmin = require('./UsuarioAdmin');
const dataController = require('../BD/DataController');
const Filtro = require('./Filtro');

//SINGLETON NO LLAMAR CONSTRUCTOR
class GestorUsuarios {

    constructor() {
    }

    async getUsuario(Email) {

        if (!Email) return 401;

        var res = await dataController.getUsuario(Email).catch(error => { console.error(error) });


        var usuario;

        if (res.rows.length == 0) {

            return 401;

        } else if(res.rows[0].admin == true){

            usuario = new UsuarioAdmin(res.rows[0].email, res.rows[0].password);
            

        } else {

            usuario = new UsuarioEstandar(res.rows[0].email, res.rows[0].password);
            
        }

        usuario.setFiltro(res.rows[0].gravedad, res.rows[0].radioefecto);

        if (res.rows[0].latitud && res.rows[0].longitud) {

            usuario.filtro.setLocalizacion1(res.rows[0].latitud, res.rows[0].longitud);
        }

        if (res.rows.length > 1 && res.rows[1].latitud && res.rows[1].longitud) {

            usuario.filtro.setLocalizacion2(res.rows[1].latitud, res.rows[1].longitud);
        }

        return usuario;
    }

    async getUsuarios(Email, Password) {

        var usuario = await this.getUsuario(Email).catch(error => { console.error(error) });

        if (typeof usuario == 'number') return usuario;

        if (!usuario.admin) return 403;

        if (usuario.password == Password) {

            var usuarios = [];

            var usus = await dataController.getUsuarios().catch(error => { console.error(error) });

            var usu;

            for (var i = 0; i < usus.rows.length; i++) {

                usu = new UsuarioEstandar(usus.rows[i].email, usus.rows[i].password);
                usuarios.push(usu);
            }
            return usuarios;
        }
        return 401;
    }

    async createUsuario(Email, Password) {

        var usuario = await this.getUsuario(Email);

        var retusu;

        var usu;

        if (usuario && usuario.email == Email) {

            var oldPassword = usuario.password;
            usuario.password = Password;
            retusu = await dataController.updateUsuario(usuario, oldPassword).catch(error => { console.error(error) });
        } else {

            usu = new UsuarioEstandar(Email, Password);
            retusu = await dataController.createUsuario(usu).catch(error => { console.error(error) });
        }
        if (retusu.email == Email && retusu.password == Password) return retusu;
        return 500;
    }

    async updateUsuario(Email, Password, Gravedad, RadioEfecto) {

        if (Gravedad > 1 || Gravedad < 0 || RadioEfecto > 500 || RadioEfecto < 0) return 400;

        var usu = new UsuarioEstandar(Email, Password);
        usu.setFiltro(Gravedad, RadioEfecto);

        var usuantiguo = await this.getUsuario(Email);

        if (typeof usuantiguo == 'number') return usuantiguo;

        var retusu;

        retusu = await dataController.updateUsuario(usu, usuantiguo.password).catch(error => { console.error(error) });
        if  (retusu && retusu.email == Email && retusu.password == Password) return retusu;
        return 500;
    }

    async deleteUsuario(Email, Password) {

        return await dataController.deleteUsuario(Email, Password).catch(error => { console.error(error) });
    }

    async getFiltro(Email, Password) {

        var usuario = await this.getUsuario(Email).catch(error => { console.error(error) });

        if (usuario && usuario.password == Password) {

            return usuario.filtro;

        }

        return usuario;
    }

    async updateLocalizacionesUsuario(email, psswd, lat1, lon1, lat2, lon2) {
        if (lat1 != null && lon1 != null) {

            if (lat1 > 90 || lat1 < -90) return 400;
            if (lon1 > 180 || lon1 < -180) return 400;
        }

        if (lat2 != null && lon2 != null) {

            if (lat2 > 90 || lat2 < -90) return 400;
            if (lon2 > 180 || lon2 < -180) return 400;
        }

        var usuario = await this.getUsuario(email).catch(error => { console.error(error) });

        if (usuario && usuario.password == psswd) {

            return await dataController.updateLocalizacionesUsuario(email, lat1, lon1, lat2, lon2);

        } else {

            var result = 401;
        }

        return result;

    }
}

module.exports = new GestorUsuarios;