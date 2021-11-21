const eventQueries = require('./EventQueries');
const externalApis = require('./ExternalApis');

const locs = [
	'41.61674,0.62218',				// les borges blanques
	'38.2699329,-0.7125608',		// elx
	'-27.1166662,-109.3666652'		// rapa nui
]

const apis = [
	externalApis.WeatherApiComCurrent,
	externalApis.FirmsViirsSnppNrt,
	//externalApis.SeismicPortalEu
]

for (const loc of locs) {
	for (const api of apis) {
		eventQueries.checkEventos(loc, api);
	}
};