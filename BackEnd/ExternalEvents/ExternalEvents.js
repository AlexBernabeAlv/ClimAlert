const eventQueries = require('./EventQueries');
const externalApis = require('./ExternalApis');

const locs = [
	'41.61674,0.62218',			// les borges blanques
	'38.2699329,-0.7125608'		// elx
]

const apis = [
	externalApis.WeatherApiComCurrent,
	externalApis.FirmsViirsSnppNrt
]

for (const loc of locs) {
	for (const api of apis) {
		console.log('loc: ' + loc);
		eventQueries.checkEventos(loc, api);
	}
};