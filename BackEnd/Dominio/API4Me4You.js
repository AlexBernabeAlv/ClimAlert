const https = require('https');
const fs = require('fs');


async function getObjectPrestado(productName) {

   return await callApi4me4u(productName);

}

function callApi4me4u(productName) {
	return new Promise((resolve, reject) => {
		url = "https://app4me4u.herokuapp.com/api/filter/products?exchange=provide&productName=" + productName;
		https.get(url, resp => {
			let data = '';
			resp.on('data', chunk => {
				data += chunk;
			})
			resp.on('end', () => {
				console.log('callApi: ' + data);
				resolve(data);
			});
		})
			.on('error', err => {
				console.log('Error: ' + err.message);
				reject(err);
			});
	});
}


module.exports = {
	getObjectPrestado
}