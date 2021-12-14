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
const apiWacc = externalApis.WeatherApiComCurrent;
const apiFirms = externalApis.FirmsViirsSnppNrt;
const apiSpeu = externalApis.SeismicPortalEu;

const EventoWaccCalorInundacion = {
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
		temp_c: 17.0,
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
		precip_mm: 0.5,
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

describe('test: fenómenos de APIs', function() {
	let fenomenosWacc = ['CalorExtremo', 'Inundacion'];
	let fenomenosFirms = ['Incendio'];
	let fenomenosSpeu = ['Terremoto'];
	it('deberia retornar el número indicado de fenómenos de Wacc', function() {
		assert.strictEqual(fenomenosWacc.length, apiWacc.fenomenos.length);
	});
	it('deberia retornar el número indicado de fenómenos de Firms', function() {
		assert.strictEqual(fenomenosFirms.length, apiFirms.fenomenos.length);
	});
	it('deberia retornar el número indicado de fenómenos de Speu', function() {
		assert.strictEqual(fenomenosSpeu.length, apiSpeu.fenomenos.length);
	});
	it('deberia retornar fenómenos de Wacc ordenados alfabéticamente', function() {
		fenomenosWacc.sort((a, b) => a.normalize().localeCompare(b.normalize()));
		assert.deepEqual(fenomenosWacc, apiWacc.fenomenos);
	});
	it('deberia retornar fenómenos de Firms ordenados alfabéticamente', function() {
		fenomenosWacc.sort((a, b) => a.normalize().localeCompare(b.normalize()));
		assert.deepEqual(fenomenosFirms, apiFirms.fenomenos);
	});
	it('deberia retornar fenómenos de Speu ordenados alfabéticamente', function() {
		fenomenosWacc.sort((a, b) => a.normalize().localeCompare(b.normalize()));
		assert.deepEqual(fenomenosSpeu, apiSpeu.fenomenos);
	});
});

describe("test: WeatherApiComCurrent", function() {
	it("deberia crear 1 incidencia de calor extremo y 1 incidencia de inundacion", function() {
		const evento = EventoWaccCalorInundacion;
		const incidencias = externalApis.getIncidencias(apiWacc, evento, []);
		assert.strictEqual(2, incidencias.length);
		assert.strictEqual('CalorExtremo', incidencias[0].fenomenoMeteo.nombre);
		assert.strictEqual('Inundacion', incidencias[1].fenomenoMeteo.nombre);
	});

	it("deberia retornar gravedad inocua", function() {
		const evento = {
			current: {
				precip_mm: 0.00
			}
		}
		const gravedad = apiWacc.getGravedad(evento, 'Inundacion');
		assert.strictEqual('inocuo', gravedad);
	});

	it("deberia retornar gravedad inocua", function() {
		const evento = {
			current: {
				precip_mm: 0.03
			}
		}
		const gravedad = apiWacc.getGravedad(evento, 'Inundacion');
		assert.strictEqual('inocuo', gravedad);
	});

	it("deberia retornar gravedad no critica", function() {
		const evento = {
			current: {
				precip_mm: 0.04
			}
		}
		const gravedad = apiWacc.getGravedad(evento, 'Inundacion');
		assert.strictEqual('noCritico', gravedad);
	});

	it("deberia retornar gravedad critica", function() {
		const evento = {
			current: {
				precip_mm: 0.05
			}
		}
		const gravedad = apiWacc.getGravedad(evento, 'Inundacion');
		assert.strictEqual('critico', gravedad);
	});
});