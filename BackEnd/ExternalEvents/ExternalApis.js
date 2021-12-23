const incidenciaFenomeno = require('../Dominio/IncidenciaFenomeno');

const WeatherApiComCurrent = {
	name: 'WeatherApiComCurrent',
	id: 'wacc',
	baseUrl: 'https://api.weatherapi.com/v1/current.json?q=',
	getUrls() {
		return [
			this.baseUrl + '42.2667,2.9667&key=3d0ae8c28ac641dd84f183611211210',
			this.baseUrl + '42.3167,2.3667&key=3d0ae8c28ac641dd84f183611211210',
			this.baseUrl + '41.9489,2.2799&key=3d0ae8c28ac641dd84f183611211210',
			this.baseUrl + '42.22,1.28&key=3d0ae8c28ac641dd84f183611211210',
			this.baseUrl + '41.6167,0.6221&key=3d0ae8c28ac641dd84f183611211210',
			this.baseUrl + '40.7160,0.8823&key=3d0ae8c28ac641dd84f183611211210',
			this.baseUrl + '41.1166,1.25&key=3d0ae8c28ac641dd84f183611211210',
			this.baseUrl + '41.3887,2.1589&key=3d0ae8c28ac641dd84f183611211210',
		]
	},
	fenomenos: [
		'CalorExtremo',
		'Inundacion'
	],
	getEventos(respuesta) {
		let eventos = [];
		if (respuesta != '') eventos.push(JSON.parse(respuesta));
		return eventos;
	},
	getLatitud(evento) {
		return evento.location.lat;
	},
	getLongitud(evento) {
		return evento.location.lon;
	},
	getFecha(evento) {
		return evento.location.localtime.split(' ')[0];
	},
	getHora(evento) {
		return evento.location.localtime.split(' ')[1];
	},
	getGravedad(evento, fenomeno) {
		switch(fenomeno) {
		case 'CalorExtremo':
			if (evento.current.temp_c > 16) return 'critico';
			if (evento.current.temp_c > 15) return 'noCritico';
			return 'inocuo';
		case 'Inundacion':
			if (evento.current.precip_mm > 0.04) return 'critico';
			if (evento.current.precip_mm > 0.03) return 'noCritico';
			return 'inocuo';
		}
	}
}

const FirmsViirsSnppNrt = {
	name: 'FirmsViirsSnppNrt',
	id: 'firms',
	baseUrl: 'https://firms.modaps.eosdis.nasa.gov/api/area/csv/6092ec0d3b6b37225112d6016c6dd223/VIIRS_SNPP_NRT/',
	getUrls() {
		const north = 43;
		const south = 40;
		const east = 3;
		const west = 0;
		const area = west + ',' + south + ',' + east + ',' + north;
		const date = new Date().getFullYear() + '-' + (new Date().getMonth() + 1).toString().padStart(2, "0") + '-' + new Date().getDate().toString().padStart(2, "0");
		return [this.baseUrl + area + '/2/' + date];
	},
	fenomenos: [
		'Incendio'
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
	getLatitud(evento) {
		return evento.latitude;
	},
	getLongitud(evento) {
		return evento.longitude;
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
	id: 'speu',
	baseUrl: 'https://www.seismicportal.eu/fdsnws/event/1/query?limit=20',
	getUrls() {
		const maxlat = '&maxlat=' + 43;
		const minlat = '&minlat=' + 40;
		const maxlon = '&maxlon=' + 3;
		const minlon = '&minlon=' + 0;
		let month = new Date().getMonth();
		if (month == 0) month = 12;
		const start = '&start=' + new Date().getFullYear() - 1 + '-' + month + '-01';
		return [this.baseUrl + maxlat + minlat + maxlon + minlon + '&format=json&minmag=3.4'];
	},
	fenomenos: [
		'Terremoto'
	],
	getEventos(respuesta) {
		let eventos = [];
		if (respuesta != '') {
			const features = JSON.parse(respuesta).features;
			for (let i = 0; i < features.length; i++) {
				const feature = features[i];
				eventos.push(feature.properties);
				i++;
			}
		}
		return eventos;
	},
	getLatitud(evento) {
		return evento.lat;
	},
	getLongitud(evento) {
		return evento.lon;
	},
	getFecha(evento) {
		return evento.time.split('T')[0];
	},
	getHora(evento) {
		const time = evento.time.split('T')[1];
		return time.split(':')[0] + ':' + time.split(':')[1];
	},
	getGravedad(evento, fenomeno) {
		//if (evento.mag >= 7) return 'critico';
		//if (evento.mag >= 5) return 'noCritico';
		//return 'inocuo';
		return 'critico';
	}
}

function getIncidencias(api, evento, incidencias) {
	const fecha = api.getFecha(evento);
	const hora = api.getHora(evento);
	for (let fenomeno of api.fenomenos) {
		const gravedad = api.getGravedad(evento, fenomeno);
		if (gravedad != 'inocuo') {
			const latitud = api.getLatitud(evento);
			const longitud = api.getLongitud(evento);
			const grave = (gravedad == 'critico');
			const radio = 1;
			const id = 0;
			//Id, Fecha, Hora, NombreFenomeno, Descripcion, Radio, Gravedad, Latitud, Longitud
			let incidencia = new incidenciaFenomeno(id, fecha, hora, fenomeno, null, radio, grave, latitud, longitud);
			incidencia.setAPI();
			incidencia.setValido();
			/*--------------------------------------------------------->incidencia.setMedida(medida de API)<---------------------------------------------------------*/
			incidencia.setCreador(api.name);

			incidencias.push(incidencia);
		}
	}
	return incidencias;
}

module.exports = {
	WeatherApiComCurrent,
	FirmsViirsSnppNrt,
	SeismicPortalEu,
	getIncidencias
};