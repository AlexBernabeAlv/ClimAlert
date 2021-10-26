const https = require('https');
const fs = require('fs');
const eventTimers = require('./EventTimers');
const externalApis = require('./ExternalApis');

const RainApis = [
	externalApis.WeatherApiCom
]

function isOutdated(timestamp) {
	lapse = Date.now() - timestamp;
	return lapse > eventTimers.RainEventTimer;
}

function checkRainEvents() {
	for (const api of RainApis) {
		//if(api.timestamp == null || isOutdated(api.timestamp))
		callApi(api);
		console.log(api);
		/*
		for (const altert of api.lastData) {
			console.log(alert.severity);
			if(alert.severity == 'Moderate') {
				saveIncidence(api, 'flood');
			} else {
				//deleteIncidence(api, 'flood');
			}
		}*/
	}
}

function callApi(api) {
	https.get(api.url + api.key, resp => {
		let data = "";
		resp.on("data", chunk => {
			data += chunk;
		});
		resp.on("end", () => {
			dataJson = JSON.parse(data);
			api.lastData = dataJson;
			const dataStr = JSON.stringify(dataJson);
			fs.writeFile(externalApis.dataPath + api.name + '.json', dataStr, (err) => {
				if (err) { throw err; }
			});
		});
		api.timestamp = Date.now();
	})
	.on("error", err => {
		console.log("Error: " + err.message);
	});
};

module.exports = {
	checkRainEvents
}