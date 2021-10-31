const fs = require('fs');

const keyPath = '../../climalertKeys/';

function getKey(apiName) {
	let keyFile = fs.readFileSync(keyPath + apiName + '.txt', 'utf8');
	return keyFile;
}

const WeatherApiComCurrent = {
	name: 'WeatherApiComCurrent',
	key: getKey('WeatherApiCom'),
	baseUrl: 'https://api.weatherapi.com/v1/current.json?q=', //current
	getUrl(loc) {
		return this.baseUrl + loc + '&key=' + this.key;
	},
	fenomenos: [
		'inundacion',
		'insolacion'
	],
	getFecha(evento) {
		return evento.location.localtime.split(" ")[0];
	},
	getHora(evento) {
		return evento.location.localtime.split(" ")[1];
	},
	getGravedad(evento, fenomeno) {
		switch(fenomeno) {
		case 'inundacion':
			if (evento.current.precip_mm > 0.04) return 'critico';
			if (evento.current.precip_mm > 0.03) return 'noCritico';
			return 'inocuo';
		case 'insolacion':
			if (evento.current.temp_c > 16) return 'critico';
			if (evento.current.temp_c > 15) return 'noCritico';
			return 'inocuo';
		}
	}
}

	//baseUrl: 'https://api.weatherapi.com/v1/forecast.json?q=', //forecast

module.exports = {
	WeatherApiComCurrent
}