const { Pool } = require('pg');
const UsuarioEstandar = require('../Dominio/UsuarioEstandar');
const UsuarioAdmin = require('../Dominio/UsuarioAdmin');
const Usuario = require('../Dominio/Usuario');


const connectionString =
    'postgres://qjsfwqmfowyyqi:880ddc879215c58013d8a54fb6d804418052f61f05045d2ec6085d77c6f5a6b4@ec2-52-211-158-144.eu-west-1.compute.amazonaws.com:5432/d2gb6gkivm6r90';

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