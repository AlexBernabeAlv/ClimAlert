const { Pool } = require('pg');
const UsuarioEstandar = require('../Dominio/UsuarioEstandar');
const UsuarioAdmin = require('../Dominio/UsuarioAdmin');
const Usuario = require('../Dominio/Usuario');


const connectionString =
    'postgres://qtwtxysoehktdh:8ed8bd9ee0226a25039223d258099e1ac3fe09a2737f452ec06d5bf921d06f76@ec2-176-34-222-188.eu-west-1.compute.amazonaws.com:5432/d98umvumg9coc8';

const pool = new Pool({
    connectionString,
    ssl: {
        rejectUnauthorized: false
    }
})


class DataController {

    constructor() {

        pool.connect();
    }


    createUsuario(usuario) {

        pool.query("INSERT INTO usuario(email, password, admin, gravedad, radioEfecto) VALUES($1, $2, $3, $4, $5);", [usuario.email, usuario.password, usuario.isAdmin(), usuario.filtro.gravedad, usuario.filtro.radioEfecto]);
    }

    getUsuario(Email, app) {

        var usuario;
        /*
        const res = await pool.query("SELECT * FROM usuario u WHERE u.email = $1", [Email]);

        if (res.rows[0].admin) {

            usuario = new UsuarioAdmin(res.rows[0].email, res.rows[0].password);

        } else {

            usuario = new UsuarioEstandar(res.rows[0].email, res.rows[0].password);
        }

        usuario.setFiltro(res.rows[0].gravedad, res.rows[0].radioefecto);

        return usuario;
        */

        pool.query("SELECT * FROM usuario u WHERE u.email = $1", [Email], (err, res) => {

            if (err) {
                console.log(err.stack)
            } else {

                //console.log(res.rows[0].email);

                if (res.rows[0].admin) {

                    usuario = new UsuarioAdmin(res.rows[0].email, res.rows[0].password);

                } else {

                    usuario = new UsuarioEstandar(res.rows[0].email, res.rows[0].password);
                }

                usuario.setFiltro(res.rows[0].gravedad, res.rows[0].radioefecto);

                app.status(200).json(usuario);

                //console.log(res.rows[0]);

                //setTimeout(() => console.log(usuario), 10000);
            }
        });
        //return usuario;

/*
        pool
            .query("SELECT * FROM usuario u WHERE u.email = $1", [Email])
            .then(res => {


                if (res.rows[0].admin) {

                    usuario = new UsuarioAdmin(res.rows[0].email, res.rows[0].password);

                } else {

                    usuario = new UsuarioEstandar(res.rows[0].email, res.rows[0].password);
                }

                usuario.setFiltro(res.rows[0].gravedad, res.rows[0].radioefecto);

                return usuario;

            })
            .catch(err => console.log(err.stack));
*/
    }
}


module.exports = DataController;