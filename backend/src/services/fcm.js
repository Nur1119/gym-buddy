'use strict';

let messaging = null;

function init() {
  const sa = process.env.FIREBASE_SERVICE_ACCOUNT;
  if (!sa || messaging) return;
  try {
    const admin = require('firebase-admin');
    if (!admin.apps.length) {
      admin.initializeApp({ credential: admin.credential.cert(JSON.parse(sa)) });
    }
    messaging = admin.messaging();
  } catch (e) {
    console.warn('FCM init failed:', e.message);
  }
}

async function send(token, title, body, data = {}) {
  init();
  if (!messaging || !token) return;
  try {
    await messaging.send({
      token,
      notification: { title, body },
      data,
      android: { priority: 'high' },
    });
  } catch (e) {
    console.warn('FCM send failed:', e.message);
  }
}

module.exports = { send };
