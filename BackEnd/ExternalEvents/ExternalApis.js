const fs = require('fs');

function getKey(apiName) {
	fs.readFile('../../climalertKeys/' + apiName + '.txt', 'utf8', (err, file) => {
		//console.log(file);
		if(err) console.log(err);
		else return file;
	});
}

const WeatherApiCom = {
	'key': getKey('WeatherApiCom'),
	'lastData': '',
	'timestamp': '',
	'url': 'https://api.weatherapi.com/v1/forecast.json?q=Barcelona', //forecast
	//'url': 'https://api.weatherapi.com/v1/current.json?q=Barcelona' //current
}

module.exports = {
	WeatherApiCom
}