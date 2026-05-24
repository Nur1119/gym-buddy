'use strict';

const { WebSocketServer } = require('ws');
const url = require('url');
const { verifyToken } = require('../middleware/auth');
const knex = require('../db/knex');

// Map of matchId -> Set of WebSocket connections
const rooms = new Map();

function joinRoom(matchId, ws) {
  let room = rooms.get(matchId);
  if (!room) {
    room = new Set();
    rooms.set(matchId, room);
  }
  room.add(ws);
}

function leaveRoom(matchId, ws) {
  const room = rooms.get(matchId);
  if (!room) return;
  room.delete(ws);
  if (!room.size) rooms.delete(matchId);
}

function broadcastToMatch(matchId, payload) {
  const room = rooms.get(matchId);
  if (!room) return;
  const data = JSON.stringify(payload);
  for (const ws of room) {
    if (ws.readyState === ws.OPEN) ws.send(data);
  }
}

const MATCH_STREAM_PATH = /^\/api\/v1\/matches\/([0-9a-f-]{36})\/stream$/i;

function attach(server) {
  const wss = new WebSocketServer({ noServer: true });

  server.on('upgrade', async (req, socket, head) => {
    const { pathname, query } = url.parse(req.url, true);
    const m = pathname && pathname.match(MATCH_STREAM_PATH);
    if (!m) {
      socket.destroy();
      return;
    }
    const matchId = m[1];
    const token = query.token;
    if (!token) {
      socket.write('HTTP/1.1 401 Unauthorized\r\n\r\n');
      socket.destroy();
      return;
    }
    let userId;
    try {
      userId = verifyToken(token).userId;
    } catch {
      socket.write('HTTP/1.1 401 Unauthorized\r\n\r\n');
      socket.destroy();
      return;
    }
    const match = await knex('matches').where({ id: matchId }).first();
    if (!match || (match.user_a !== userId && match.user_b !== userId)) {
      socket.write('HTTP/1.1 403 Forbidden\r\n\r\n');
      socket.destroy();
      return;
    }

    wss.handleUpgrade(req, socket, head, (ws) => {
      ws.userId = userId;
      ws.matchId = matchId;
      joinRoom(matchId, ws);
      ws.send(JSON.stringify({ type: 'connected', matchId }));

      ws.on('message', (raw) => {
        let msg;
        try {
          msg = JSON.parse(raw.toString());
        } catch {
          return;
        }
        if (msg.type === 'typing') {
          broadcastToMatch(matchId, { type: 'typing', userId });
        } else if (msg.type === 'read' && msg.messageId) {
          knex('messages')
            .where({ id: msg.messageId, match_id: matchId })
            .whereNot({ sender_id: userId })
            .whereNull('read_at')
            .update({ read_at: knex.fn.now() })
            .then(() => {
              broadcastToMatch(matchId, { type: 'read', messageId: msg.messageId });
            })
            .catch(() => {});
        }
      });

      ws.on('close', () => leaveRoom(matchId, ws));
      ws.on('error', () => leaveRoom(matchId, ws));
    });
  });
}

module.exports = { attach, broadcastToMatch };
