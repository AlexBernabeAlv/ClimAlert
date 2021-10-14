const express = require('express');
const notificacion = require('./Dominio/Notificacion')
const app = express()

//app.get
app.get('/', (req, res) => {
    console.log('GET request recived')
    res.status(200).send('Home Page')
})

app.get('/notificacion', (req, res) => {
    console.log('GET request recived')
    var id = notificacion.getid()
    res.status(200).send(`${id}`)
})

//app.all
app.all('*', (req, res) => {
    res.status(404).send('<h1>404 Not Found</h1>')
})

app.listen(5000, () => {
    console.log('server is ready on port 5000.')
})


//app.post
//app.puto
//app.delete
//app.use
//app.listen