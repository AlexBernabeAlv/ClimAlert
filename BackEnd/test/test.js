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

const EventoWaccCalorInundacionTornado = {
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

const EventoFirmsIncendio = {
    latitude: 41.19487,
    longitude: 1.22852,
    bright_ti4: 305.18,
    scan: 0.4,
    track: 0.44,
    acq_date: '2022-01-01',
    acq_time: 217,
    satellite: 'N',
    instrument: 'VIIRS',
    confidence: 'n',
    version: '2.0NRT',
    bright_ti5: 278.84,
    frp: 1.27,
    daynight: 'N'
}

const EventoSpeuTerremoto = {
    geometry: {
        type: "Point",
        coordinates: [
            1.44,
            42.52,
            -5.0
        ]
    },
    type: "Feature",
    id: "20211011_0000080",
    properties: {
        lastupdate: "2021-10-11T11:59:00.0Z",
        magtype: "ml",
        evtype: "ke",
        lon: 1.44,
        auth: "EMSC",
        lat: 42.52,
        depth: 5.0,
        unid: "20211011_0000080",
        mag: 3.8,
        time: "2021-10-11T08:23:14.4Z",
        source_id: "1046960",
        source_catalog: "EMSC-RTS",
        flynn_region: "PYRENEES"
    }
}

describe('test: fen??menos de APIs', function() {
	let fenomenosWacc = ['CalorExtremo', 'Inundacion', 'Tornado'];
	let fenomenosFirms = ['Incendio'];
	let fenomenosSpeu = ['Terremoto'];
	it('deberia retornar el n??mero indicado de fen??menos de Wacc', function() {
		assert.strictEqual(fenomenosWacc.length, apiWacc.fenomenos.length);
	});
	it('deberia retornar el n??mero indicado de fen??menos de Firms', function() {
		assert.strictEqual(fenomenosFirms.length, apiFirms.fenomenos.length);
	});
	it('deberia retornar el n??mero indicado de fen??menos de Speu', function() {
		assert.strictEqual(fenomenosSpeu.length, apiSpeu.fenomenos.length);
	});
	it('deberia retornar fen??menos de Wacc ordenados alfab??ticamente', function() {
		fenomenosWacc.sort((a, b) => a.normalize().localeCompare(b.normalize()));
		assert.deepEqual(fenomenosWacc, apiWacc.fenomenos);
	});
	it('deberia retornar fen??menos de Firms ordenados alfab??ticamente', function() {
		fenomenosWacc.sort((a, b) => a.normalize().localeCompare(b.normalize()));
		assert.deepEqual(fenomenosFirms, apiFirms.fenomenos);
	});
	it('deberia retornar fen??menos de Speu ordenados alfab??ticamente', function() {
		fenomenosWacc.sort((a, b) => a.normalize().localeCompare(b.normalize()));
		assert.deepEqual(fenomenosSpeu, apiSpeu.fenomenos);
	});
});

describe("test: WeatherApiComCurrent incidencias", function() {
	it("deberia crear 1 incidencia de calor extremo, inundacion y tornado", function() {
		const evento = EventoWaccCalorInundacionTornado;
		const incidencias = externalApis.getIncidencias(apiWacc, evento, []);
		assert.strictEqual(incidencias.length, 3);
		assert.strictEqual(incidencias[0].fenomenoMeteo.nombre, 'CalorExtremo');
		assert.strictEqual(incidencias[1].fenomenoMeteo.nombre, 'Inundacion');
		assert.strictEqual(incidencias[2].fenomenoMeteo.nombre, 'Tornado');
		assert(incidencias[0].API);
		assert(incidencias[1].API);
		assert(incidencias[2].API);
		assert.strictEqual(incidencias[0].creador, 'Weather Api Current');
		assert.strictEqual(incidencias[1].creador, 'Weather Api Current');
		assert.strictEqual(incidencias[2].creador, 'Weather Api Current');
		assert.strictEqual(incidencias[0].medida, evento.current.temp_c);
		assert.strictEqual(incidencias[1].medida, evento.current.precip_mm);
		assert.strictEqual(incidencias[2].medida, evento.current.wind_kph);
	});
});

describe("test: WeatherApiComCurrent gravedad calor extremo", function() {
	it("deberia retornar gravedad inocua", function() {
		const evento = {
			current: {
				temp_c: 0.00
			}
		}
		const gravedad = apiWacc.getGravedad(evento, 'CalorExtremo');
		assert.strictEqual('inocuo', gravedad);
	});

	it("deberia retornar gravedad inocua", function() {
		const evento = {
			current: {
				temp_c: -4.6
			}
		}
		const gravedad = apiWacc.getGravedad(evento, 'CalorExtremo');
		assert.strictEqual('inocuo', gravedad);
	});

	it("deberia retornar gravedad inocua", function() {
		const evento = {
			current: {
				temp_c: 10
			}
		}
		const gravedad = apiWacc.getGravedad(evento, 'CalorExtremo');
		assert.strictEqual('inocuo', gravedad);
	});

	it("deberia retornar gravedad no critica", function() {
		const evento = {
			current: {
				temp_c: 16
			}
		}
		const gravedad = apiWacc.getGravedad(evento, 'CalorExtremo');
		assert.strictEqual('noCritico', gravedad);
	});

	it("deberia retornar gravedad critica", function() {
		const evento = {
			current: {
				temp_c: 17
			}
		}
		const gravedad = apiWacc.getGravedad(evento, 'CalorExtremo');
		assert.strictEqual('critico', gravedad);
	});
});

describe("test: WeatherApiComCurrent gravedad inundacion", function() {
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

describe("test: WeatherApiComCurrent gravedad tornado", function() {
	it("deberia retornar gravedad inocua", function() {
		const evento = {
			current: {
				wind_kph: 0.00
			}
		}
		const gravedad = apiWacc.getGravedad(evento, 'Tornado');
		assert.strictEqual('inocuo', gravedad);
	});

	it("deberia retornar gravedad inocua", function() {
		const evento = {
			current: {
				wind_kph: 15
			}
		}
		const gravedad = apiWacc.getGravedad(evento, 'Tornado');
		assert.strictEqual('inocuo', gravedad);
	});

	it("deberia retornar gravedad no critica", function() {
		const evento = {
			current: {
				wind_kph: 35
			}
		}
		const gravedad = apiWacc.getGravedad(evento, 'Tornado');
		assert.strictEqual('noCritico', gravedad);
	});

	it("deberia retornar gravedad critica", function() {
		const evento = {
			current: {
				wind_kph: 36
			}
		}
		const gravedad = apiWacc.getGravedad(evento, 'Tornado');
		assert.strictEqual('critico', gravedad);
	});
});

describe("test: Firms incidencias", function() {
	it("deberia crear 1 incidencia de incendio forestal", function() {
		const evento = EventoFirmsIncendio;
		const incidencias = externalApis.getIncidencias(apiFirms, evento, []);
		assert.strictEqual(incidencias.length, 1);
		assert.strictEqual(incidencias[0].fenomenoMeteo.nombre, 'Incendio');
		assert(incidencias[0].API);
		assert.strictEqual(incidencias[0].creador, 'Fire Information for Resource Management System');
		assert.strictEqual(incidencias[0].medida, evento.bright_ti4);
	});
});

describe("test: Firms gravedad incendio forestal", function() {
	const evento = EventoFirmsIncendio;
	it("deberia retornar gravedad inocua", function() {
		const evento = {
			bright_ti4: 0.00
		}
		const gravedad = apiFirms.getGravedad(evento, 'Incendio');
		assert.strictEqual('inocuo', gravedad);
	});

	it("deberia retornar gravedad inocua", function() {
		const evento = {
			bright_ti4: 280
		}
		const gravedad = apiFirms.getGravedad(evento, 'Incendio');
		assert.strictEqual('inocuo', gravedad);
	});

	it("deberia retornar gravedad no critica", function() {
		const evento = {
			bright_ti4: 330
		}
		const gravedad = apiFirms.getGravedad(evento, 'Incendio');
		assert.strictEqual('noCritico', gravedad);
	});

	it("deberia retornar gravedad critica", function() {
		const evento = {
			bright_ti4: 360
		}
		const gravedad = apiFirms.getGravedad(evento, 'Incendio');
		assert.strictEqual('critico', gravedad);
	});
});

describe("test: Speu incidencias", function() {
	it("deberia crear 1 incidencia de terremoto", function() {
		const evento = EventoSpeuTerremoto.properties;
		const incidencias = externalApis.getIncidencias(apiSpeu, evento, []);
		assert.strictEqual(incidencias.length, 1);
		assert.strictEqual(incidencias[0].fenomenoMeteo.nombre, 'Terremoto');
		assert(incidencias[0].API);
		assert.strictEqual(incidencias[0].creador, 'Seismic Portal EU');
		assert.strictEqual(incidencias[0].medida, evento.mag);
	});
});

describe("test: Speu gravedad terremoto", function() {
	const evento = EventoSpeuTerremoto.properties;
	it("deberia retornar gravedad inocua", function() {
		const evento = {
			mag: 0.00
		}
		const gravedad = apiSpeu.getGravedad(evento, 'Terremoto');
		assert.strictEqual('inocuo', gravedad);
	});

	it("deberia retornar gravedad inocua", function() {
		const evento = {
			mag: 3.5
		}
		const gravedad = apiSpeu.getGravedad(evento, 'Terremoto');
		assert.strictEqual('inocuo', gravedad);
	});

	it("deberia retornar gravedad no critica", function() {
		const evento = {
			mag: 4
		}
		const gravedad = apiSpeu.getGravedad(evento, 'Terremoto');
		assert.strictEqual('noCritico', gravedad);
	});

	it("deberia retornar gravedad critica", function() {
		const evento = {
			mag: 7
		}
		const gravedad = apiSpeu.getGravedad(evento, 'Terremoto');
		assert.strictEqual('critico', gravedad);
	});
});