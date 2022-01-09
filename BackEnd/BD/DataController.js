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

            var fenomenosMeteos = ["Incendio", "Terremoto", "Tornado", "Inundacion", "Avalancha", "Lluvia Acida", "Erupcion Volcanica", "Gota fria", "Calor Extremo", "Granizo", "Tormenta electrica", "Tormenta invernal", "Tsunami"];
            var descripcionesFenomenosMeteo = [

                "Ocurrencia de fuego no controlada que puede afectar o abrasar algo que no está destinado a quemarse. Puede afectar a estructuras y a seres vivos.",
                "Sacudida brusca y pasajera de la corteza terrestre producida por la liberación de energía acumulada en forma de ondas sísmicas. Los más comunes se producen por la actividad de fallas geológicas.",
                "Columna de aire con alta velocidad angular cuyo extremo está tocando la Tierra. Se trata del fenómeno atmosférico ciclónico de mayor densidad energética de la Tierra.",
                "Ocupación por parte del agua de zonas que habitualmente están libres de esta, por desbordamiento de ríos, torrentes o ramblas, por lluvias torrenciales, deshielo, maremotos, huracanes, entre otros.",
                "Desplazamiento de una capa de nieve en una zona en pendiente que se dirige ladera abajo con velocidad variable. Se puede incorporar parte del sustrato y de la cobertura vegetal de la pendiente.",
                "Lluvia formada cuando la humedad del aire se combina con óxidos de nitrógeno, dióxido de azufre o trióxido de azufre emitidos por la consumición de petróleo que contenga azufre.",
                "Fenómeno geológico caracterizado por la emisión violenta en la superficie terrestre, por un volcán, de lavas y/o tefras acompañadas de gases volcánicos.",
                "Evento de lluvia de alto impacto durante el otoño a lo largo del Mediterráneo occidental y puede ser análogo al Episodio Cevenol francés o Episodio Mediterráneo.",
                "Altas temperaturas",
                "Precipitación sólida que se compone de bolas o grumos irregulares de hielo, cada uno de los cuales se refiere como una piedra de granizo.",
                "Fenómeno meteorológico asociado al desarrollo vertical de nubosidad acompañado de descargas eléctricas o rayos y, habitualmente, precipitación y rachas de viento intensas en superficie.",
                "Eventos dominados por precipitación formada a bajas temperaturas como nieve o lluvia congelada. Pueden durar varios días y estar acompañadas de fuertes vientos y temperaturas extremadamente frías.",
                "Evento producido cuando se desplaza una gran masa de agua por algún fenómeno extraordinario, como un terremoto, erupción volcánica, desprendimientos de hielo glaciar, impacto de meteoritos, etc."
            ];

            pool.query("INSERT INTO fenomenometeo(nombre, descripcion) VALUES($1, $2), ($3, $4), ($5, $6), ($7, $8), ($9, $10), ($11, $12), ($13, $14), ($15, $16), ($17, $18), ($19, $20), ($21, $22), ($23, $24), ($25, $26);", [fenomenosMeteos[0], descripcionesFenomenosMeteo[0], fenomenosMeteos[1], descripcionesFenomenosMeteo[1], fenomenosMeteos[2], descripcionesFenomenosMeteo[2], fenomenosMeteos[3], descripcionesFenomenosMeteo[3], fenomenosMeteos[4], descripcionesFenomenosMeteo[4], fenomenosMeteos[5], descripcionesFenomenosMeteo[5], fenomenosMeteos[6], descripcionesFenomenosMeteo[6], fenomenosMeteos[7], descripcionesFenomenosMeteo[7], fenomenosMeteos[8], descripcionesFenomenosMeteo[8], fenomenosMeteos[9], descripcionesFenomenosMeteo[9], fenomenosMeteos[10], descripcionesFenomenosMeteo[10], fenomenosMeteos[11], descripcionesFenomenosMeteo[11], fenomenosMeteos[12], descripcionesFenomenosMeteo[12]], (err, res) => {

                if (err) {

                    reject(err);
                }  
            });

            pool.query("INSERT INTO refugio(nombre, latitud, longitud) VALUES($1, $2, $3);", ['Camp Nou', 41.38099258140189, 2.1228197983623125], (err, res) => {

                if (err) {

                    reject(err);
                }
            });

            pool.query("INSERT INTO usuario(email, password, admin, gravedad, radioEfecto, banned) VALUES('yo@gmail.com', '1234', true, '0', '200', false);", (err, res) => {

                if (err) {

                    reject(err);
                }
            });

            pool.query("INSERT INTO usuario(email, password, admin, gravedad, radioEfecto, banned) VALUES('Weather Api Current', '0', true, '0', '1', false);", (err, res) => {

                if (err) {

                    reject(err);
                }
            });

            pool.query("INSERT INTO usuario(email, password, admin, gravedad, radioEfecto, banned) VALUES('Fire Information for Resource Management System', '0', true, '0', '1', false);", (err, res) => {

                if (err) {

                    reject(err);
                }
            });

            pool.query("INSERT INTO usuario(email, password, admin, gravedad, radioEfecto, banned) VALUES('Seismic Portal EU', '0', true, '0', '1', false);", (err, res) => {

                if (err) {

                    reject(err);
                }
            });

            pool.query("INSERT INTO usuario(email, password, admin, gravedad, radioEfecto, banned) VALUES('ecomute', 'ecomute1234', false, '0', '50', false);", (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    pool.query("INSERT INTO incidenciafenomeno(valido, fecha, hora, nombrefen, api, email, medida) VALUES($1, $2, $3, $4, $5, 'yo@gmail.com', 0) RETURNING incfenid;", [false, "2021/12/25", "00:00", "Incendio", false], (err, res) => {

                        if (err) {

                            reject(err);

                        } else {



                            if (res.rowCount == 1) {

                                pool.query("INSERT INTO incidencia(id, radioefecto, gravedad, latitud, longitud, api) VALUES($1, $2, $3, $4, $5, $6)", [res.rows[0].incfenid, 1, 0, 41.3879, 2.16992, false], (err, res) => {

                                    if (err) {

                                        reject(err);

                                    } else {

                                        if (res.rowCount == 1) {

                                            resolve(200);
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

            pool.query("INSERT INTO usuario(email, password, admin, gravedad, radioEfecto, banned) VALUES($1, $2, $3, $4, $5, $6);", [usuario.email, usuario.password, usuario.isAdmin(), usuario.filtro.gravedad, usuario.filtro.radioEfecto, false], (err, res) => {

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

    getUsuarios() {
        var promise = new Promise((resolve, reject) => {

            pool.query("SELECT * FROM usuario WHERE admin = false;", (err, res) => {

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

                        reject(401);
                    }
                    else if (res.rowCount == 1) {
                        resolve(200);
                    }
                }
            });
        });

        return promise;
    }

    banUsuarrio(EmailUsr) {

        var promise = new Promise((resolve, reject) => {

            pool.query("UPDATE usuario SET banned = NOT banned WHERE email = $1 AND admin = false;", [EmailUsr], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    if (res.rowCount == 0) {

                        reject(401);
                    }
                    else if (res.rowCount == 1) {
                        resolve(200);
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

                    if (lat1 != null && lon1 != null) {

                        pool.query("INSERT INTO localizacionusuario(emailusr, latitud, longitud) VALUES($1, $2, $3);", [email, lat1, lon1], (err, res) => {
                            if (err) {

                                reject(err);
                            } else {

                                resolve(200);
                            }
                        });
                    }

                    if (lat2 != null && lon2 != null) {

                        pool.query("INSERT INTO localizacionusuario(emailusr, latitud, longitud) VALUES($1, $2, $3);", [email, lat2, lon2], (err, res) => {

                            if (err) {

                                reject(err);
                            } else {

                                resolve(200);
                            }
                        });
                    }

                    resolve(200);

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

    createIncidencia(Latitud, Longitud, Fecha, Hora, NombreFenomeno, Valido, Api, Email, Medida) {

        var promise = new Promise((resolve, reject) => {

            pool.query("INSERT INTO incidenciafenomeno(valido, fecha, hora, nombrefen, api, email, medida) VALUES($1, $2, $3, $4, $5, $6, $7) RETURNING incfenid;", [Valido, Fecha, Hora, NombreFenomeno, Api, Email, Medida], (err, res) => {

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

                    resolve(res);
                }

            });
        });

        return promise;
    }

    getIncidenciasAdmin(Valido) {

        var promise = new Promise((resolve, reject) => {
            pool.query("SELECT * FROM incidencia i INNER JOIN incidenciafenomeno if ON i.id = if.incfenid INNER JOIN fenomenometeo f ON if.nombrefen = f.nombre WHERE valido = $1", [Valido], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve(res);
                }

            });
        });

        return promise;
    }

    getIncidenciasByCreadorGroupDia(Email) {

        var promise = new Promise((resolve, reject) => {

            pool.query("SELECT fecha, COUNT(*) FROM incidenciafenomeno WHERE email = $1 GROUP BY fecha;", [Email], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve(res.rows);
                }
            });
        });

        return promise;
    }

    getIncidenciasByCreadorGroupMinuto(Email) {

        var promise = new Promise((resolve, reject) => {

            pool.query("SELECT fecha, hora, COUNT(*) FROM incidenciafenomeno WHERE email = $1 GROUP BY fecha, hora;", [Email], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve(res.rows);
                }
            });
        });

        return promise;
    }

    updateIncidencia(Id, Gravedad, RadioEfecto, Medida) {

        var promise = new Promise((resolve, reject) => {

            pool.query("UPDATE incidenciafenomeno SET valido = $2 WHERE incfenid = $1;", [Id, true], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    pool.query("UPDATE incidencia SET gravedad = $2, radioefecto = $3, medida = $4 WHERE id = $1;", [Id, Gravedad, RadioEfecto, Medida], (err, res) => {

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

    deleteRefugio(Nombre) {

        var promise = new Promise((resolve, reject) => {

            pool.query("DELETE FROM refugio WHERE nombre = $1", [Nombre], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    if (res.rowCount == 0) {

                        resolve(400);
                    }
                    else if (res.rowCount == 1) {
                        resolve(200);
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

                        reject(404);
                    } else {

                        resolve(res.rows[0]);
                    }
                }
            });
        });

        return promise;
    }

    getRefugios() {

        var promise = new Promise((resolve, reject) => {

            pool.query("SELECT * FROM refugio;", (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve(res);
                }
            });
        });

        return promise;
    }

    //Aqui Comentarios

    createComentario(Comentario) {

        var promise = new Promise((resolve, reject) => {

            pool.query("INSERT INTO comentario(emailusr, incfenid, contenido, comentresponseid, fecha, hora) VALUES($1, $2, $3, $4, $5, $6) RETURNING id;", [Comentario.email, Comentario.incfenid, Comentario.contenido, Comentario.idresponsecomment, Comentario.fecha, Comentario.hora], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve(res.rows[0].id);
                }
            });
        });


        return promise;
    }

    getComentario(Id) {

        var promise = new Promise((resolve, reject) => {

            pool.query("SELECT id, emailusr, incfenid, contenido, comentresponseid, fecha, hora FROM comentario WHERE id = $1", [Id], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve(res);
                }
            });
        });

        return promise;
    }

    getComentariosByIncFenId(Incfenid) {

        var promise = new Promise((resolve, reject) => {

            pool.query("SELECT * FROM incidenciafenomeno WHERE incfenid = $1", [Incfenid], (err, res) => {
                console.log(res.rows);
                if (err) {

                    reject(err);
                } else {

                    if (res.rows.length == 0) reject(404);

                    else {

                        pool.query("SELECT id, emailusr, incfenid, contenido, comentresponseid, fecha, hora FROM comentario WHERE incfenid = $1 AND comentresponseid IS NULL", [Incfenid], (err, res) => {

                            console.log(res.rows);
                            if (err) {

                                reject(err);
                            } else {

                                resolve(res);
                            }
                        });
                    }
                }
            });

            
        });

        return promise;
    }

    getComentariosByUsuario(Email) {

        var promise = new Promise((resolve, reject) => {

            pool.query("SELECT id, emailusr, incfenid, contenido, comentresponseid, fecha, hora FROM comentario WHERE emailusr = $1", [Email], (err, res) => {

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

            pool.query("SELECT id, emailusr, incfenid, contenido, comentresponseid, fecha, hora FROM comentario WHERE comentresponseid = $1", [CommentId], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve(res);
                }
            });
        });
        
        return promise;
    }

    getComentariosByCreadorGroupDia(Email) {

        var promise = new Promise((resolve, reject) => {

            pool.query("SELECT fecha, COUNT(*) FROM comentario WHERE emailusr = $1 GROUP BY fecha;", [Email], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve(res.rows);
                }
            });
        });

        return promise;
    }

    getComentariosByCreadorGroupMinuto(Email) {

        var promise = new Promise((resolve, reject) => {

            pool.query("SELECT fecha, hora, COUNT(*) FROM comentario WHERE emailusr = $1 GROUP BY fecha, hora;", [Email], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve(res.rows);
                }
            });
        });

        return promise;
    }

    updateComentario(CommentId, Contenido, Email) {

        var promise = new Promise((resolve, reject) => {

            pool.query("UPDATE comentario SET contenido = $2 WHERE id = $1 AND emailusr = $3 AND contenido != '[deleted]';", [CommentId, Contenido, Email], (err, res) => {

                if (err) {

                    reject(err);
                } else {

                    resolve(res.rowCount);
                }
            });
        });

        return promise;
    }
}


module.exports = new DataController();