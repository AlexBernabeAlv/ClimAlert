const eventQueries = require('./EventQueries');
const externalApis = require('./ExternalApis');

const apis = [
	externalApis.WeatherApiComCurrent,
	externalApis.FirmsViirsSnppNrt,
	externalApis.SeismicPortalEu
]

async function checkEventos() {
	let incidencias = [];
	for (const api of apis) {
		incidencias = await eventQueries.checkEventos(api, incidencias);
	}
	return incidencias;
}

module.exports = {
	checkEventos
}