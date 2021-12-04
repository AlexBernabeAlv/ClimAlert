/*
const assert = require("assert");
let chai = require("chai");
let chaiHttp = require("chai-http");
let server=require("../app");
let should = chai.should();
chai.use(chaiHttp);

---

var expect  = require('chai').expect;
var request = require('request');

it('Main page content', function(done) {
    request('http://localhost:8080' , function(error, response, body) {
        expect(body).to.equal('Hello World');
        done();
    });
});

----
*/

const assert = require('assert').strict;
const externalApis = require('../ExternalEvents/ExternalApis');

const WaccCalorInundacion = {
	location: {
		name: "Figueras",
		region: "Catalonia",
		country: "Spain",
		lat: 42.27,
		lon: 2.97,
		tz_id: "Europe/Madrid",
		localtime_epoch: 1638361673,
		localtime: "2021-12-01 13:27"
	},
	current: {
		last_updated_epoch: 1638360900,
		last_updated: "2021-12-01 13:15",
		temp_c: 16.0,
		temp_f: 60.8,
		is_day: 1,
		condition: {
			text: "Moderate rain",
			icon: "//cdn.weatherapi.com/weather/64x64/day/302.png",
			code: 1189
		},
		wind_mph: 10.5,
		wind_kph: 16.9,
		wind_degree: 190,
		wind_dir: "S",
		pressure_mb: 1012.0,
		pressure_in: 29.88,
		precip_mm: 0.0,
		precip_in: 0.0,
		humidity: 45,
		cloud: 25,
		feelslike_c: 16.0,
		feelslike_f: 60.8,
		vis_km: 10.0,
		vis_miles: 6.0,
		uv: 4.0,
		gust_mph: 8.5,
		gust_kph: 13.7
	}
}

describe("test: WeatherApiComCurrent", function() {
	const api = ExternalApis.WeatherApiComCurrent;
	const evento = WaccCalorInundacion;
	it("deberia crear 1 incidencia de calor extremo y 1 incidencia de inundacion", function() {
		const fecha = api.getFecha(evento);
		const hora = api.getHora(evento);
		for (let fenomeno of api.fenomenos) {
			const gravedad = getGravedad(WaccCalorInundacion);
			const latitud = api.getLatitud(evento);
			const longitud = api.getLongitud(evento);
			if (gravedad != 'inocuo') {
				const grave = (gravedad == 'critico');
				const radio = 1;
				const incidencia = new incidenciaFenomeno(fecha, hora, fenomeno, null, radio, grave, latitud, longitud);
				//let name = api.name;
				incidencias.push(incidencia);
			}
		}
		assert.strictEqual('inocuo', externalApis.WeatherApiComCurrent.getGravedad(eventoInundacion1, 'Inundacion');
	});
	it("should return gravedad", function() {
		const gravedad = ;
		assert.strictEqual('inocuo', externalApis.WeatherApiComCurrent.getGravedad(eventoInundacion2, 'Inundacion');
	});
	it("should return gravedad", function() {
		const gravedad = ;
		assert.strictEqual('inocuo', externalApis.WeatherApiComCurrent.getGravedad(eventoInundacion3, 'Inundacion');
	});
	it("should return gravedad", function() {
		const gravedad = ;
		assert.strictEqual('inocuo', externalApis.WeatherApiComCurrent.getGravedad(eventoInundacion4, 'Inundacion');
	});
});