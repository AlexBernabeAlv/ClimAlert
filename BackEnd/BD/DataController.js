const { Pool } = require('pg');


const connectionString =
    'postgres://lmnzkvausdfuuu:4b2fade74f887b3ed91ba4efd5ac1238dd9b47909880021b58c80ddb6e2219fa@ec2-54-195-246-55.eu-west-1.compute.amazonaws.com:5432/d3kik8oljojk5u';

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

            var fenomenosMeteos = ["Incendio", "Terremoto", "Tornado", "Inundacion", "Avalancha", "Lluvia Acida", "Erupcion Volcanica", "Gota fria", "Insolacion"];
            var descripcionesFenomenosMeteo = [

                "Cosa que quema",
                "Cosa que tiembla",
                "Cosa que airea",
                "Cosa que llena de agua",
                "Cosa que tira cosas encima de cosas",
                "Cosa que moja, pero poco y mal",
                "Cosa que explota",
                "Cosa que hace canicas los hue..",
                "Cosa que da calor"
            ];

            pool.query("INSERT INTO fenomenometeo(nombre, descripcion) VALUES($1, $2), ($3, $4), ($5, $6), ($7, $8), ($9, $10), ($11, $12), ($13, $14), ($15, $16), ($17, $18);", [fenomenosMeteos[0], descripcionesFenomenosMeteo[0], fenomenosMeteos[1], descripcionesFenomenosMeteo[1], fenomenosMeteos[2], descripcionesFenomenosMeteo[2], fenomenosMeteos[3], descripcionesFenomenosMeteo[3], fenomenosMeteos[4], descripcionesFenomenosMeteo[4], fenomenosMeteos[5], descripcionesFenomenosMeteo[5], fenomenosMeteos[6], descripcionesFenomenosMeteo[6], fenomenosMeteos[7], descripcionesFenomenosMeteo[7], fenomenosMeteos[8], descripcionesFenomenosMeteo[8]], (err, res) => {

                if (err) {

                    reject(err);
                }  
            });

            pool.query("INSERT INTO usuario(email, password, admin, gravedad, radioEfecto) VALUES('yo@gmail.com', '1234', true, '0', '3');", (err, res) => {

                if (err) {

                    reject(err);
                }
            });

            pool.query("INSERT INTO usuario(email, password, admin, gravedad, radioEfecto) VALUES('ecomute', 'ecomute1234', false, '0', '50');", (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    pool.query("INSERT INTO incidenciafenomeno(valido, fecha, hora, nombrefen, api) VALUES($1, $2, $3, $4, $5) RETURNING incfenid;", [false, "2021/12/25", "00:00", "Incendio", false], (err, res) => {

                        if (err) {

                            reject(err);

                        } else {



                            if (res.rowCount == 1) {

                                pool.query("INSERT INTO incidencia(id, radioefecto, gravedad, latitud, longitud, api) VALUES($1, $2, $3, $4, $5, $6)", [res.rows[0].incfenid, 1, 0, 41.3879, 2.16992, false], (err, res) => {

                                    if (err) {

                                        reject(err);

                                    } else {

                                        if (res.rowCount == 1) {

                                            resolve("Reset correcto");
                                        }

                                    }

                                });

                            }

                        }


                    });

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


    updateLocalizacionesUsuario(email, lat1, lon1, lat2, lon2) {

        var promise = new Promise((resolve, reject) => {

            pool.query("DELETE FROM localizacionusuario WHERE emailusr = $1", [email], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    if (lat1 && lon1) {

                        pool.query("INSERT INTO localizacionusuario(emailusr, latitud, longitud) VALUES($1, $2, $3);", [email, lat1, lon1], (err, res) => {

                            if (err) {

                                reject(err);
                            } else {

                                resolve("Cambios en ubicaciones aplicados");
                            }

                            
                        });

                    }

                    if (lat2 && lon2) {

                        pool.query("INSERT INTO localizacionusuario(emailusr, latitud, longitud) VALUES($1, $2, $3);", [email, lat2, lon2], (err, res) => {

                            if (err) {

                                reject(err);
                            } else {

                                resolve("Cambios en ubicaciones aplicados");
                            }
                        });

                    }


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

    createIncidencia(Latitud, Longitud, Fecha, Hora, NombreFenomeno, Valido, Api) {

        var promise = new Promise((resolve, reject) => {

            pool.query("INSERT INTO incidenciafenomeno(valido, fecha, hora, nombrefen, api) VALUES($1, $2, $3, $4, $5) RETURNING incfenid;", [Valido, Fecha, Hora, NombreFenomeno, Api], (err, res) => {

                if (err) {

                    reject(err);

                } else {

                    

                    if (res.rowCount == 1) {

                        pool.query("INSERT INTO incidencia(id, radioefecto, gravedad, latitud, longitud, api) VALUES($1, $2, $3, $4, $5, $6)", [res.rows[0].incfenid, 1, 0, Latitud, Longitud, Api], (err, res) => {

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

    getIncidencias(Latitud, Longitud, Gravedad, RadioEfecto, Valido) {

        var promise = new Promise((resolve, reject) => {

            //calcular TEOREMA DE PITAGORAS
            var sqrRadioEfecto = RadioEfecto * RadioEfecto;
            pool.query("SELECT * FROM incidencia i INNER JOIN incidenciafenomeno if ON i.id = if.incfenid INNER JOIN fenomenometeo f ON if.nombrefen = f.nombre WHERE (((i.latitud * 110.574 - $2 * 110.574) * (i.latitud * 110.574 - $2 * 110.574)) + (((i.longitud * 111.320 * cos(i.latitud - $2) - $3 * 111.320 * cos(i.latitud - $2)) * (i.longitud * 111.320 * cos(i.latitud - $2) - $3 * 111.320 * cos(i.latitud - $2))))) <= $1 AND i.gravedad >= $4 AND valido = $5", [sqrRadioEfecto, Latitud, Longitud, Gravedad, Valido], (err, res) => {
                
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

    updateIncidencia(Id, Gravedad) { 

        var promise = new Promise((resolve, reject) => {

            pool.query("UPDATE incidenciafenomeno SET valido = $2 WHERE incfenid = $1;", [Id, true], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    pool.query("UPDATE incidencia SET gravedad = $2 WHERE id = $1;", [Id, Gravedad], (err, res) => {

                        if (err) {

                            reject(err);
                        } else {

                            resolve(true);
                        }
                    });
                }
            });

        });

        return promise;
    }

    deleteIncidenciasFromAPIs() {

        var promise = new Promise((resolve, reject) => {

            pool.query("DELETE FROM incidencia WHERE api = true;", (err, res) => {
                if (err) {

                    reject(err);
                } else {

                    pool.query("DELETE FROM incidenciafenomeno WHERE api = true;",
                        (err, res) => {
                            if (err) {

                                reject(err);
                            } else {

                                resolve(true);
                            }
                        });
                }

            });
        });

        return promise;
    }

    
    //Refugios

    createRefugio(refugio) {

        var promise = new Promise((resolve, reject) => {

            pool.query("INSERT INTO refugio(nombre, latitud, longitud) VALUES($1, $2, $3);", [refugio.nombre, refugio.localizacion.latitud, refugio.localizacion.longitud], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve(refugio);
                }
            });
        });


        return promise;

    }

    updateRefugio(Refugio) {

    }

    deleteRefugio(Nombre) {

        var promise = new Promise((resolve, reject) => {

            pool.query("DELETE FROM refugio WHERE nombre = $1", [Nombre], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    if (res.rowCount == 0) {

                        reject("Refugio no existe");
                    }
                    else if (res.rowCount == 1) {
                        resolve("Refugio borrado");
                    }
                }
            });
        });

        return promise;
    }

    getRefugioByLoc(Latitud, Longitud) {

        var promise = new Promise((resolve, reject) => {

            pool.query("SELECT nombre, latitud, longitud, (((latitud * 110.574 - $1 * 110.574) * (latitud * 110.574 - $1 * 110.574)) + (((longitud * 111.320 * cos(latitud - $1) - $2 * 111.320 * cos(latitud - $1)) * (longitud * 111.320 * cos(latitud - $1) - $2 * 111.320 * cos(latitud - $1))))) AS distancia FROM refugio WHERE ((((latitud * 110.574 - $1 * 110.574) * (latitud * 110.574 - $1 * 110.574)) + (((longitud * 111.320 * cos(latitud - $1) - $2 * 111.320 * cos(latitud - $1)) * (longitud * 111.320 * cos(latitud - $1) - $2 * 111.320 * cos(latitud - $1))))) <= 2500) ORDER BY distancia ASC", [Latitud, Longitud], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    if (res.rows.length == 0) {

                        reject("Refugio no existe");
                    } else {
                        console.log(res.rows);
                        resolve(res.rows[0]);
                    }
                }
            });
        });

        return promise;
    }

    //Aqui Comentarios

    createComentario(Comentario) {

        var promise = new Promise((resolve, reject) => {

            pool.query("INSERT INTO comentario(emailusr, incfenid, contenido, comentresponseid) VALUES($1, $2, $3, $4) RETURNING id;", [Comentario.email, Comentario.incfenid, Comentario.contenido, Comentario.idresponsecomment], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve(id);
                }
            });
        });


        return promise;
    }

    getComentariosByIncFenId(Incfenid) {

        var promise = new Promise((resolve, reject) => {

            pool.query("SELECT id, emailusr, incfenid, contenido, comentresponseid FROM comentario WHERE incfenid = $1", [Incfenid], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve(res);
                }
            });
        });

        return promise;
    }

    getComentariosByUsuario(Email) {

        var promise = new Promise((resolve, reject) => {

            pool.query("SELECT id, emailusr, incfenid, contenido, comentresponseid FROM comentario WHERE emailusr = $1", [Email], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve(res);
                }
            });
        });

        return promise;
    }

    getComentariosByComentario(CommentId) {

        var promise = new Promise((resolve, reject) => {

            pool.query("SELECT id, emailusr, incfenid, contenido, comentresponseid FROM comentario WHERE comentresponseid = $1", [CommentId], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve(res);
                }
            });
        });

        return promise;
    }

    updateComentario(CommentId, Contenido) {

        var promise = new Promise((resolve, reject) => {

            pool.query("UPDATE comentario SET emailusr = $2, contenido = $2 WHERE id = $1;", [CommentId, Contenido], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve(res);
                }
            });
        });

        return promise;
    }
}


module.exports = new DataController();