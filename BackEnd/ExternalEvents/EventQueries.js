const https = require('https');
const fs = require('fs');
const incidenciaFenomeno = require('../Dominio/IncidenciaFenomeno');

async function checkEventos(loc, api) {
	let incidencias = [];
	const url = api.getUrl(loc);
	console.log('url: ' + url);
	const respuesta = await callApi(api, url);
	//const respuesta = '';
	const eventos = api.getEventos(respuesta);
	console.log('eventos: ' + JSON.stringify(eventos, null, 2));
	for (let i = 0; i < eventos.length; i++) {
		const evento = eventos[i];
		const fecha = api.getFecha(evento);
		const hora = api.getHora(evento);
		for (let fenomeno of api.fenomenos) {
			const gravedad = api.getGravedad(evento, fenomeno);
			const localizacion = api.getLoc(evento);
			if (gravedad != 'inocuo') {
				const grave = (gravedad == 'critico');
				const radio = 1;
				const incidencia = new incidenciaFenomeno(fecha, hora, fenomeno, radio, grave, loc);
				//let name = api.name;
				incidencias.push(incidencia);
			}
		}
	}
	console.log('incidencias: ' + JSON.stringify(incidencias, null, 2));
	//almacenarIncidencias(incidencias)
}

function callApi(api, url) {
	return new Promise((resolve, reject) => {
		https.get(url, resp => {
			let data = ''
			resp.on('data', chunk => {
				data += chunk
			})
			resp.on('end', () => {
				console.log('callApi: ' + data)
				resolve(data)
			})
		})
		.on('error', err => {
			console.log('Error: ' + err.message)
			reject(err)
		})
	})
}


module.exports = {
	checkEventos
}