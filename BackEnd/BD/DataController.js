const { Pool } = require('pg');
const UsuarioEstandar = require('../Dominio/UsuarioEstandar');
const UsuarioAdmin = require('../Dominio/UsuarioAdmin');


const connectionString =
    'postgres://qjsfwqmfowyyqi:880ddc879215c58013d8a54fb6d804418052f61f05045d2ec6085d77c6f5a6b4@ec2-52-211-158-144.eu-west-1.compute.amazonaws.com:5432/d2gb6gkivm6r90';

const pool = new Pool({
    connectionString,
    ssl: {
        rejectUnauthorized: false
    }
})

//Esta clase es singleton
class DataController{
    constructor(){

        pool.connect();
    }

    resetBD(respuesta) {

        //Crear FenomenoMeteo

        pool.query("INSERT INTO fenomenometeo(nombre, descripcion) VALUES('incdendio', 'cosa que quema');", (err, res) => {

            if (res.rowCount == 1) {

                respuesta.status(200).send("FenomenoMeteoCreat");
            } else {
                respuesta.status(400).send("Error al crear FenomenoMeteo");
            }

        });

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

    updateUsuario(usuario, respuesta) {

        pool.query("UPDATE usuario SET password = $2, gravedad = $3, radioEfecto = $4 WHERE email = $1;", [usuario.email, usuario.password, usuario.filtro.gravedad, usuario.filtro.radioEfecto], (err, res) => {

            if (err) {

                respuesta.status(409).send("Usuario no existe");
            } else {

                if (res.rowCount == 0) {
                    respuesta.status(409).send("Usuario no existe");
                }
                else if (res.rowCount == 1) {
                    respuesta.status(200).json(usuario);
                }
            }
        });

    }

    deleteUsuario(Email, Password, respuesta) {

        pool.query("DELETE FROM usuario WHERE email = $1 AND password = $2", [Email, Password], (err, res) => {

            if (err) {

                respuesta.status(409).send("Usuario no existe");
            } else {

                respuesta.status(200).send("SOS PUTO");
            }
        });
    }

    async getFenomeno(NombreFenomeno) {

        var promise = new Promise((resolve, reject) => {

            pool.query("SELECT * FROM fenomenometeo f WHERE f.nombre = $1", [NombreFenomeno], (err, res) => {

                if (err) {

                    console.log(err);
                    reject(err);
                } else {
                    
                    resolve(res.rows[0].nombre);
                }

            })

        })

        return promise;
    }

    createIncidencia(Latitud, Longitud, Fecha, Hora, NombreFenomeno) {

        var promise = new Promise((resolve, reject) => {

            pool.query("INSERT INTO incidenciafenomeno(valido, fecha, hora, nombrefen) VALUES($1, $2, $3, $4) RETURNING incfenid;", [false, Fecha, Hora, NombreFenomeno], (err, res) => {

                if (err) {

                    console.log(err);

                } else {

                    

                    if (res.rowCount == 1) {

                        pool.query("INSERT INTO incidencia(id, radioefecto, gravedad, latitud, longitud) VALUES($1, $2, $3, $4, $5)", [res.rows[0].incfenid, 1, 0, Latitud, Longitud], (err, res) => {

                            if (err) {

                                reject(err);

                            } else {

                                if (res.rowCount == 1) {

                                    var result = "Incidencia creada";
                                    
                                    resolve(result);
                                } 

                            }

                        });

                    }

                }


            });

        });

        return promise;
    }

    getIncidencia() {

    }

}


module.exports = new DataController();