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


    createUsuario(usuario, respuesta) {

        pool.query("INSERT INTO usuario(email, password, admin, gravedad, radioEfecto) VALUES($1, $2, $3, $4, $5);", [usuario.email, usuario.password, usuario.isAdmin(), usuario.filtro.gravedad, usuario.filtro.radioEfecto], (err, res) => {

            if (err) {

                respuesta.status(409).send("Usuario ya existe");
            } else {

                respuesta.status(200).json(usuario);
            }
        });
    }

    getUsuario(Email, respuesta) {

        var usuario;

        pool.query("SELECT * FROM usuario u WHERE u.email = $1", [Email], (err, res) => {

            if (err) {
                console.log(err.stack)
            } else {


                if (res.rows.length == 0) {

                    respuesta.status(404).send("Usuario no existe");
                    return;
                }
                if (res.rows[0].admin) {

                    usuario = new UsuarioAdmin(res.rows[0].email, res.rows[0].password);

                } else {

                    usuario = new UsuarioEstandar(res.rows[0].email, res.rows[0].password);
                }

                usuario.setFiltro(res.rows[0].gravedad, res.rows[0].radioefecto);

                respuesta.status(200).json(usuario);

            }
        });

    }
}


module.exports = DataController;