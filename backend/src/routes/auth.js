'use strict';

const express = require('express');
const bcrypt = require('bcrypt');
const { z } = require('zod');
const knex = require('../db/knex');
const { signToken, authRequired } = require('../middleware/auth');
const { validate } = require('../middleware/validate');
const { serializeUser } = require('../services/serializers');

const router = express.Router();

const registerSchema = z.object({
  email: z.string().email(),
  password: z.string().min(6),
  name: z.string().min(1),
  age: z.number().int().min(13).max(120).optional(),
});

const loginSchema = z.object({
  email: z.string().email(),
  password: z.string().min(1),
});

router.post('/register', validate(registerSchema), async (req, res, next) => {
  try {
    const { email, password, name, age } = req.body;
    const existing = await knex('users').where({ email: email.toLowerCase() }).first();
    if (existing) {
      return res.status(409).json({
        error: { code: 'email_taken', message: 'Email already registered' },
      });
    }
    const passwordHash = await bcrypt.hash(password, 10);
    const username = '@' + name.toLowerCase().replace(/[^a-z0-9]/g, '').slice(0, 12) +
      Math.floor(Math.random() * 1000);
    const { c: uCount } = await knex('users').count('id as c').first();
    const userHandle = 'gb_' + String(parseInt(uCount) + 1).padStart(8, '0');
    const [row] = await knex('users')
      .insert({
        email: email.toLowerCase(),
        password_hash: passwordHash,
        name,
        username,
        user_handle: userHandle,
        age: age || null,
        goal: 'Hypertrophy',
        level: 'Beginner',
      })
      .returning('*');
    const token = signToken(row.id);
    const user = await serializeUser(row, { includeEmail: true });
    res.status(201).json({ token, user });
  } catch (err) {
    next(err);
  }
});

router.post('/login', validate(loginSchema), async (req, res, next) => {
  try {
    const { email, password } = req.body;
    const row = await knex('users').where({ email: email.toLowerCase() }).first();
    if (!row) {
      return res.status(401).json({
        error: { code: 'invalid_credentials', message: 'Invalid email or password' },
      });
    }
    const ok = await bcrypt.compare(password, row.password_hash);
    if (!ok) {
      return res.status(401).json({
        error: { code: 'invalid_credentials', message: 'Invalid email or password' },
      });
    }
    const token = signToken(row.id);
    const user = await serializeUser(row, { includeEmail: true });
    res.json({ token, user });
  } catch (err) {
    next(err);
  }
});

router.post('/refresh', authRequired, async (req, res) => {
  const token = signToken(req.userId);
  res.json({ token });
});

router.get('/me', authRequired, async (req, res, next) => {
  try {
    const row = await knex('users').where({ id: req.userId }).first();
    if (!row) {
      return res.status(404).json({ error: { code: 'not_found', message: 'User not found' } });
    }
    const user = await serializeUser(row, { includeEmail: true });
    res.json(user);
  } catch (err) {
    next(err);
  }
});

const googleSchema = z.object({
  idToken: z.string().min(1),
});

router.post('/google', validate(googleSchema), async (req, res, next) => {
  try {
    const { idToken } = req.body;
    const infoRes = await fetch(`https://oauth2.googleapis.com/tokeninfo?id_token=${encodeURIComponent(idToken)}`);
    if (!infoRes.ok) {
      return res.status(401).json({ error: { code: 'invalid_token', message: 'Invalid Google token' } });
    }
    const payload = await infoRes.json();
    if (!payload.email || payload.error) {
      return res.status(401).json({ error: { code: 'invalid_token', message: 'Token verification failed' } });
    }
    const email = payload.email.toLowerCase();
    let row = await knex('users').where({ email }).first();
    if (!row) {
      const name = payload.name || email.split('@')[0];
      const username = '@' + name.toLowerCase().replace(/[^a-z0-9]/g, '').slice(0, 12) +
        Math.floor(Math.random() * 1000);
      const { c: gCount } = await knex('users').count('id as c').first();
      const gHandle = 'gb_' + String(parseInt(gCount) + 1).padStart(8, '0');
      [row] = await knex('users')
        .insert({ email, name, username, user_handle: gHandle, google_id: payload.sub, goal: 'Hypertrophy', level: 'Beginner' })
        .returning('*');
    } else if (!row.google_id) {
      await knex('users').where({ id: row.id }).update({ google_id: payload.sub });
      row = await knex('users').where({ id: row.id }).first();
    }
    const token = signToken(row.id);
    const user = await serializeUser(row, { includeEmail: true });
    res.json({ token, user });
  } catch (err) {
    next(err);
  }
});

module.exports = router;
