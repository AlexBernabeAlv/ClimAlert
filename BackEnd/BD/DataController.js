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


    resetBD() {

        var promise = new Promise((resolve, reject) => {

            //Crear FenomenosMeteo

            var fenomenosMeteos = ["Incendio", "Terremotos", "Tornados", "Tsunami", "Avalancha", "Lluvia Acida", "Erupcion Volcanica", "Gota fria"];
            var descripcionesFenomenosMeteo = [

                "Cosa que quema",
                "Cosa que tiembla",
                "Cosa que airea",
                "Cosa que moja",
                "Cosa que tira cosas encima de cosas",
                "Cosa que moja, pero poco y mal",
                "Cosa que explota",
                "Cosa que hace canicas los hue.."
            ];

            pool.query("INSERT INTO fenomenometeo(nombre, descripcion) VALUES($1, $2), ($3, $4), ($5, $6), ($7, $8), ($9, $10), ($11, $12), ($13, $14), ($15, $16);", [fenomenosMeteos[0], descripcionesFenomenosMeteo[0], fenomenosMeteos[1], descripcionesFenomenosMeteo[1], fenomenosMeteos[2], descripcionesFenomenosMeteo[2], fenomenosMeteos[3], descripcionesFenomenosMeteo[3], fenomenosMeteos[4], descripcionesFenomenosMeteo[4], fenomenosMeteos[5], descripcionesFenomenosMeteo[5], fenomenosMeteos[6], descripcionesFenomenosMeteo[6], fenomenosMeteos[7], descripcionesFenomenosMeteo[7]], (err, res) => {

                if (err) {

                    reject(err);
                }  
            });


            pool.query("INSERT INTO usuario(email, password, admin, gravedad, radioEfecto) VALUES('yo@gmail.com', '1234', false, '0', '3');", (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve("Reset correcto");
                }
            });
            
        });

        return promise;
    }

    createUsuario(usuario) {

        var promise = new Promise((resolve, reject) => {

            pool.query("INSERT INTO usuario(email, password, admin, gravedad, radioEfecto) VALUES($1, $2, $3, $4, $5);", [usuario.email, usuario.password, usuario.isAdmin(), usuario.filtro.gravedad, usuario.filtro.radioEfecto], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve(usuario);
                }
            });
        });

        
        return promise;
    }

    getUsuario(Email) {

        var promise = new Promise((resolve, reject) => {
            
            pool.query("SELECT * FROM usuario u LEFT OUTER JOIN localizacionusuario l ON u.email = l.emailusr WHERE u.email = $1;", [Email], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve(res);
                }
            });
        });

        return promise;
    }

    updateUsuario(usuario, oldPassword) {

        var promise = new Promise((resolve, reject) => {

            pool.query("UPDATE usuario SET password = $2, gravedad = $3, radioEfecto = $4 WHERE email = $1 AND password = $5;", [usuario.email, usuario.password, usuario.filtro.gravedad, usuario.filtro.radioEfecto, oldPassword], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    if (res.rowCount == 0) {

                        reject(err);
                    }
                    else if (res.rowCount == 1) {
                        resolve(usuario);
                    }
                }
            });
        });

        return promise;
    }

    deleteUsuario(Email, Password) {

        var promise = new Promise((resolve, reject) => {

            pool.query("DELETE FROM usuario WHERE email = $1 AND password = $2", [Email, Password], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    if (res.rowCount == 0) {

                        reject("Usuario no existe");
                    }
                    else if (res.rowCount == 1) {
                        resolve("Usuario borrado");
                    }
                }
            });
        });

        return promise;
    }


    createLocalizacionesUsuario(email, lat1, lon1, lat2, lon2) {


        var promise = new Promise((resolve, reject) => {

            pool.query("INSERT INTO localizacionusuario(emailusr, latitud, longitud) VALUES($1, $2, $3), ($1, $4, $5);", [email, lat1, lon1, lat2, lon2], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve("Localizaciones Creadas");
                }
            });
        });

        return promise;
    }

    updateLocalizacionesUsuario(email, lat1, lon1, lat2, lon2) {

        var promise = new Promise((resolve, reject) => {

            pool.query("DELETE FROM localizacionusuario WHERE emailusr = $1", [email], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    if (res.rowCount == 0) {

                        reject("Usuario no existe");
                    }
                    
                }
            });


            pool.query("INSERT INTO localizacionusuario(emailusr, latitud, longitud) VALUES($1, $2, $3), ($1, $4, $5);", [email, lat1, lon1, lat2, lon2], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve("Localizaciones Modificadas");
                }
            });
        });

        return promise;

    }


    getFenomeno(NombreFenomeno) {

        var promise = new Promise((resolve, reject) => {

            pool.query("SELECT * FROM fenomenometeo f WHERE f.nombre = $1", [NombreFenomeno], (err, res) => {

                if (err) {

                    reject(err);

                } else {

                    if (res.rows.length == 0) {

                        reject(false);
                    } else {

                        resolve(res.rows[0].nombre);
                    }
                    
                }

            });
        });

        return promise;
    }

    createIncidencia(Latitud, Longitud, Fecha, Hora, NombreFenomeno) {

        var promise = new Promise((resolve, reject) => {

            pool.query("INSERT INTO incidenciafenomeno(valido, fecha, hora, nombrefen) VALUES($1, $2, $3, $4) RETURNING incfenid;", [false, Fecha, Hora, NombreFenomeno], (err, res) => {

                if (err) {

                    reject(err);

                } else {

                    

                    if (res.rowCount == 1) {

                        pool.query("INSERT INTO incidencia(id, radioefecto, gravedad, latitud, longitud) VALUES($1, $2, $3, $4, $5)", [res.rows[0].incfenid, 1, 0, Latitud, Longitud], (err, res) => {

                            if (err) {

                                reject(err);

                            } else {

                                if (res.rowCount == 1) {

                                    resolve("Incidencia creada");
                                }

                            }

                        });

                    }

                }


            });

        });

        return promise;
    }

    getIncidencias(Latitud, Longitud, Gravedad, RadioEfecto) {

        var promise = new Promise((resolve, reject) => {

            //calcular TEOREMA DE PITAGORAS
            var sqrRadioEfecto = RadioEfecto * RadioEfecto;
            pool.query("SELECT * FROM incidencia i INNER JOIN incidenciafenomeno if ON i.id = if.incfenid INNER JOIN fenomenometeo f ON if.nombrefen = f.nombre WHERE (((i.latitud * 110.574 - $2 * 110.574) * (i.latitud * 110.574 - $2 * 110.574)) + (((i.longitud * 111.320 * cos(i.latitud - $2) - $3 * 111.320 * cos(i.latitud - $2)) * (i.longitud * 111.320 * cos(i.latitud - $2) - $3 * 111.320 * cos(i.latitud - $2))))) <= $1 AND i.gravedad = $4", [sqrRadioEfecto, Latitud, Longitud, Gravedad], (err, res) => {
                console.log("Radio*Radio: " + sqrRadioEfecto + " Latitud: " + Latitud + " Longitud: " + Longitud + " Gravedad: " + Gravedad);
                console.log("incidencias en BD:");
                console.log(res.rows);
                if (err) {

                    reject(err);
                } else {

                    if (res.rows.length == 0) {

                        reject(false);
                    } else {

                        resolve(res);
                    }

                }

            });
        });

        return promise;
    }

}


module.exports = new DataController();