const https = require('https');
const fs = require('fs');
const incidencia = require('../Dominio/IncidenciaFenomeno');

async function checkEventos(loc, api) {
	let incidencias = [];
	let url = api.getUrl(loc);
	let evento = await callApi(api, url);
	console.log("evento: " + JSON.stringify(evento, null, 2));
	let fecha = api.getFecha(evento);
	let hora = api.getHora(evento);
	for (const fenomeno of api.fenomenos) {
		let gravedad = api.getGravedad(evento, fenomeno);
		if(gravedad != 'inocuo') {
			let grave = (gravedad == 'critico');
			let radio = 1;
			let incid = new incidencia(fecha, hora, fenomeno, radio, grave, loc);
			//let name = api.name;
			incidencias.push(incid);
		}
	}
	console.log("incidencias: " + JSON.stringify(incidencias, null, 2));
	//almacenarIncidencias(incidencias)
}

/*
function callApi(api, url) {
    return new Promise((resolve, reject) => {
        https.get(url, resp => {
            let data = ""
            resp.on("data", chunk => {
                data += chunk
            })
            resp.on("end", () => {
                console.log("callApi: " + data)
                resolve(data)
            })
        })
        .on("error", err => {
            console.log("Error: " + err.message)
            reject(err)
        })
    })
}
*/

function callApi(api, url) {
	return {
		"location": {
			"name": "Madrid",
			"region": "Madrid",
			"country":"Spain",
			"lat": 40.4,
			"lon": -3.68,
			"tz_id": "Europe/Madrid",
			"localtime_epoch": 1635618053,
			"localtime": "2021-10-30 20:20"
		},
		"current": {
			"last_updated_epoch": 1635617700,
			"last_updated": "2021-10-30 20:15",
			"temp_c": 17,
			"temp_f": 62.6,
			"is_day": 0,
			"condition": {
				"text": "Partly cloudy",
				"icon": "//cdn.weatherapi.com/weather/64x64/night/116.png",
				"code": 1003
			},
			"wind_mph": 11.9,
			"wind_kph": 19.1,
			"wind_degree": 240,
			"wind_dir": "WSW",
			"pressure_mb": 1011,
			"pressure_in": 29.85,
			"precip_mm": 0.2,
			"precip_in": 0.01,
			"humidity": 83,
			"cloud": 75,
			"feelslike_c": 17,
			"feelslike_f": 62.6,
			"vis_km": 10,
			"vis_miles": 6,
			"uv": 4,
			"gust_mph": 15.4,
			"gust_kph": 24.8
		}
	}
}

module.exports = {
	checkEventos
}