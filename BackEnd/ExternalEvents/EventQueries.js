const https = require('https');
const fs = require('fs');
const incidenciaFenomeno = require('../Dominio/IncidenciaFenomeno');

async function checkEventos(api, incidencias) {
	const urls = api.getUrls();
	for (let i = 0; i < urls.length; i++) {
		url = urls[i];
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
				const latitud = api.getLatitud(evento);
				const longitud = api.getLongitud(evento);
				if (gravedad != 'inocuo') {
					const grave = (gravedad == 'critico');
					const radio = 1;
					//Fecha, Hora, NombreFenomeno, Descripcion, Radio, Gravedad, Latitud, Longitud
					const incidencia = new incidenciaFenomeno(fecha, hora, fenomeno, null, radio, grave, latitud, longitud);
					//let name = api.name;
					incidencias.push(incidencia);
				}
			}
		}
	}
	return incidencias;
}

function callApi(api, url) {
	return new Promise((resolve, reject) => {
		https.get(url, resp => {
			let data = '';
			resp.on('data', chunk => {
				data += chunk;
			})
			resp.on('end', () => {
				console.log('callApi: ' + data);
				resolve(data);
			});
		})
		.on('error', err => {
			console.log('Error: ' + err.message);
			reject(err);
		});
	});
}


module.exports = {
	checkEventos
}