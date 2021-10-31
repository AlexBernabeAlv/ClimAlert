const eventQueries = require('./EventQueries');
const externalApis = require('./ExternalApis');

const locs = [
	'barcelona',
	'madrid'
]

const apis = [
	externalApis.WeatherApiComCurrent
]

for (const loc of locs) {
	for (const api of apis) {
		eventQueries.checkEventos(loc, api);
	}
};