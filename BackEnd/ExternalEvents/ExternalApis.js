const fs = require('fs');

const keyPath = '../../climalertKeys/';

function getKey(fileName) {
	let keyFile = fs.readFileSync(keyPath + fileName + '.txt', 'utf8');
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
	getEventos(respuesta) {
		let eventos = [];
		eventos.push(JSON.parse(respuesta));
		return eventos;
	},
	getFecha(evento) {
		return new Date();
		//return evento.location.localtime.split(' ')[0];
	},
	getHora(evento) {
		return new Date();
		//return evento.location.localtime.split(' ')[1];
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

const FirmsViirsSnppNrt = {
	name: 'FirmsViirsSnppNrt',
	key: getKey('NasaGovFirms'),
	baseUrl: 'https://firms.modaps.eosdis.nasa.gov/api/area/csv/',
	getUrl(loc) {
		const latitud = loc.split(',')[0];
		const longitud = loc.split(',')[1];
		console.log('loc: ' + loc);
		console.log('latitud: ' + latitud);
		console.log('longitud: ' + longitud);
		//const north = latitud + 0.015;
		//const south = latitud - 0.015;
		//const east = longitud + 0.02;
		//const west = longitud - 0.02;
		const north = latitud + 2.015;
		const south = latitud - 2.015;
		const east = longitud + 2.02;
		const west = longitud - 2.02;
		const area = west + ',' + south + ',' + east + ',' + north;
		console.log('area: ' + area)
		const date = new Date().getFullYear() + '-' + (new Date().getMonth() + 1).toString().padStart(2, "0") + '-' + new Date().getDate().toString().padStart(2, "0");
		console.log('date: ' + date);
		console.log('type of date: ' + typeof(date));
		return this.baseUrl + this.key + '/VIIRS_SNPP_NRT/' + area + '/1/' + date;
	},
	fenomenos: [
		'incendio'
	],
	getEventos(respuesta) {
		let eventos = [];
		const lineas = respuesta.split('\n');
		const atributos = lineas[0].split(',');
		for (let i = 1; i < lineas.length; i++) {
			let evento = {};
			const linea = lineas[i].split(',');
			for (let j = 0; j < atributos.length; j++) {
				evento[atributos[j]] = linea[j];
			}
			eventos.push(evento);
		}
		return eventos;
	},
	getFecha(evento) {
		return new Date();
	},
	getHora(evento) {
		return new Date();
	},
	getGravedad(evento, fenomeno) {
		return 'critico';
	}
}

module.exports = {
	WeatherApiComCurrent,
	FirmsViirsSnppNrt
};