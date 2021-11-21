const fs = require('fs');

const keyPath = '../../climalertKeys/';

function getKey(fileName) {
	let keyFile = fs.readFileSync(keyPath + fileName + '.txt', 'utf8');
	return keyFile;
}

const WeatherApiComCurrent = {
	name: 'WeatherApiComCurrent',
	key: getKey('WeatherApiCom'),
	baseUrl: 'https://api.weatherapi.com/v1/current.json?q=',
	getUrl(loc) {
		return this.baseUrl + loc + '&key=' + this.key;
	},
	fenomenos: [
		'inundacion',
		'insolacion'
	],
	getEventos(respuesta) {
		let eventos = [];
		if (respuesta != '') eventos.push(JSON.parse(respuesta));
		return eventos;
	},
	getLoc(evento) {
		return evento.location.lat + ',' + evento.location.lon;
	},
	getFecha(evento) {
		return evento.location.localtime.split(' ')[0];
	},
	getHora(evento) {
		return evento.location.localtime.split(' ')[1];
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
		//const north = latitud + 0.015;
		//const south = latitud - 0.015;
		//const east = longitud + 0.02;
		//const west = longitud - 0.02;
		const north = latitud + 16.015;
		const south = latitud - 16.015;
		const east = longitud + 16.02;
		const west = longitud - 16.02;
		const area = west + ',' + south + ',' + east + ',' + north;
		const date = new Date().getFullYear() + '-' + (new Date().getMonth() + 1).toString().padStart(2, "0") + '-' + new Date().getDate().toString().padStart(2, "0");
		return this.baseUrl + this.key + '/VIIRS_SNPP_NRT/' + area + '/2/' + date;
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
	getLoc(evento) {
		return evento.latitude + ',' + evento.longitude;
	},
	getFecha(evento) {
		return evento.time;
	},
	getHora(evento) {
		return '00:00';
	},
	getGravedad(evento, fenomeno) {
		return 'critico';
	}
}

const SeismicPortalEu = {
	name: 'SeismicPortalEu',
	baseUrl: 'www.seismicportal.eu/fdsnws/event/1/query?limit=10',
	getUrl(loc) {
		const latitud = '&lat=' + loc.split(',')[0];
		const longitud = '&lon=' + loc.split(',')[1];
		const start = '&start=' + new Date().getFullYear() + '-' + (new Date().getMonth() + 1).toString().padStart(2, "0") + '-' + new Date().getDate().toString().padStart(2, "0");
		const radius = '&maxradius=' + 80;
		return this.baseUrl + latitud + longitud + radius + '&format=json';
	},
	fenomenos: [
		'terremoto'
	],
	getEventos(respuesta) {
		let eventos = [];
		if (respuesta != '') {
			const features = JSON.parse(respuesta).features;
			for (let i = 0; i < features.length; i++) {
				eventos.push(features.i.properties);
			}
		}
		return eventos;
	},
	getLoc(evento) {
		return evento.lat + ',' + evento.lon;
	},
	getFecha(evento) {
		return evento.time.split('T')[0];
	},
	getHora(evento) {
		const time = evento.time.split('T')[1];
		return time.split(':')[0] + ':' + time.split(':')[1];
	},
	getGravedad(evento, fenomeno) {
		if (evento.mag >= 7) return 'critico';
		if (evento.mag >= 5) return 'noCritico';
		return 'inocuo';
	}
}

module.exports = {
	WeatherApiComCurrent,
	FirmsViirsSnppNrt,
	SeismicPortalEu
};