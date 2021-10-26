const fs = require('fs');

const keyPath = '../../climalertKeys/';
const dataPath = './ApisResults/';

function getKey(apiName) {
	let keyFile = fs.readFileSync(keyPath + apiName + '.txt', 'utf8');
	return keyFile;
}

function getLastData(apiName) {
	try {
		let dataFile = fs.readFileSync(dataPath + apiName + '.json', 'utf8');
		return JSON.parse(dataFile.toString());
	} catch (err) {
		//console.log(err);
		return '';
	}
}

const WeatherApiCom = {
	'name': 'WeatherApiCom',
	'key': getKey('WeatherApiCom'),
	'lastData': getLastData('WeatherApiCom'),
	'timestamp': '',
	'url': 'https://api.weatherapi.com/v1/forecast.json?q=Barcelona&key=', //forecast
	//'url': 'https://api.weatherapi.com/v1/current.json?q=Barcelona&key=' //current
}

module.exports = {
	WeatherApiCom,
	dataPath
}