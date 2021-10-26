const eventTimers = require('./EventTimers');
const eventQueries = require('./EventQueries');

eventQueries.checkRainEvents();
setInterval(eventQueries.checkRainEvents, eventTimers.RainEventTimer);