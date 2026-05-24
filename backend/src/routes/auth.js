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
    const [row] = await knex('users')
      .insert({
        email: email.toLowerCase(),
        password_hash: passwordHash,
        name,
        username,
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

module.exports = router;
