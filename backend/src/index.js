'use strict';

const path = require('path');
const http = require('http');
const express = require('express');
const cors = require('cors');

const config = require('./config');
const { notFound, errorHandler } = require('./middleware/error');
const ws = require('./ws/chat');

const app = express();
app.use(cors());
app.use(express.json({ limit: '2mb' }));

// Serve uploaded files
app.use('/uploads', express.static(path.resolve(config.uploadDir)));

// Routes — all under /api/v1
const v1 = express.Router();
v1.use('/auth', require('./routes/auth'));
v1.use('/users', require('./routes/users'));
v1.use('/discover', require('./routes/discover'));
v1.use('/matches', require('./routes/matches'));
// Per-match messages: mounted under matches/:id/messages
v1.use('/matches/:id/messages', require('./routes/messages'));
v1.use('/workouts', require('./routes/workouts'));
v1.use('/exercises', require('./routes/exercises'));
v1.use('/routines', require('./routes/routines'));
v1.use('/friends', require('./routes/friends'));
v1.use('/leaderboard', require('./routes/leaderboard'));
v1.use('/nutrition', require('./routes/nutrition'));
v1.use('/medals', require('./routes/medals'));
v1.use('/quests', require('./routes/quests'));
v1.use('/calendar', require('./routes/calendar'));

v1.get('/health', (req, res) => res.json({ ok: true, time: new Date().toISOString() }));

app.use('/api/v1', v1);

app.use(notFound);
app.use(errorHandler);

const server = http.createServer(app);
ws.attach(server);

server.listen(config.port, () => {
  // eslint-disable-next-line no-console
  console.log(`GymBuddy API listening on http://localhost:${config.port}`);
  // eslint-disable-next-line no-console
  console.log(`WebSocket: ws://localhost:${config.port}/api/v1/matches/:id/stream`);
});

module.exports = { app, server };
