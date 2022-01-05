const https = require('https');
const fs = require('fs');
const externalApis = require('./ExternalApis');

async function checkEventos(api, incidencias) {
	const urls = api.getUrls();
	for (let i = 0; i < urls.length; i++) {
		const respuesta = await callApi(api, urls[i]);
		//const respuesta = '';
		const eventos = api.getEventos(respuesta);
		console.log('eventos: ' + JSON.stringify(eventos, null, 2));
		for (let i = 0; i < eventos.length; i++) {
			incidencias = externalApis.getIncidencias(api, eventos[i], incidencias);
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