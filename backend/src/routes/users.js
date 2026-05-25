'use strict';

const express = require('express');
const path = require('path');
const fs = require('fs');
const multer = require('multer');
const { v4: uuidv4 } = require('uuid');
const { z } = require('zod');
const knex = require('../db/knex');
const { authRequired } = require('../middleware/auth');
const { validate } = require('../middleware/validate');
const { serializeUser } = require('../services/serializers');
const config = require('../config');

const router = express.Router();

const updateSchema = z
  .object({
    name: z.string().min(1).optional(),
    age: z.number().int().min(13).max(120).optional(),
    height: z.number().int().min(100).max(250).optional(),
    weight: z.number().int().min(30).max(300).optional(),
    bio: z.string().max(500).optional(),
    goal: z.enum(['Strength', 'Hypertrophy', 'Mobility', 'Calisthenics', 'CrossFit', 'Cardio']).optional(),
    level: z.enum(['Beginner', 'Intermediate', 'Advanced', 'Elite']).optional(),
    gymName: z.string().nullable().optional(),
    gymLat: z.number().nullable().optional(),
    gymLng: z.number().nullable().optional(),
    schedule: z.array(z.number().int().min(0).max(6)).optional(),
    interests: z.array(z.string()).optional(),
    userHandle: z.string().min(3).max(15).regex(/^[a-z][a-z0-9_]*$/, 'Handle must start with a letter, only lowercase letters, numbers, underscores').optional(),
  })
  .strict();

router.get('/me', authRequired, async (req, res, next) => {
  try {
    const row = await knex('users').where({ id: req.userId }).first();
    if (!row) return res.status(404).json({ error: { code: 'not_found', message: 'User not found' } });
    res.json(await serializeUser(row, { includeEmail: true }));
  } catch (err) {
    next(err);
  }
});

router.patch('/me', authRequired, validate(updateSchema), async (req, res, next) => {
  try {
    const map = {
      name: 'name',
      age: 'age',
      height: 'height',
      weight: 'weight',
      bio: 'bio',
      goal: 'goal',
      level: 'level',
      gymName: 'gym_name',
      gymLat: 'gym_lat',
      gymLng: 'gym_lng',
      schedule: 'schedule',
      interests: 'interests',
    };
    const update = { updated_at: knex.fn.now() };
    for (const [k, v] of Object.entries(req.body)) {
      if (map[k]) update[map[k]] = v;
    }
    if (req.body.userHandle) {
      const taken = await knex('users')
        .where({ user_handle: req.body.userHandle })
        .whereNot({ id: req.userId })
        .first();
      if (taken) {
        return res.status(409).json({ error: { code: 'handle_taken', message: 'This handle is already taken' } });
      }
      update.user_handle = req.body.userHandle;
    }
    if (Object.keys(update).length > 1) {
      await knex('users').where({ id: req.userId }).update(update);
    }
    const row = await knex('users').where({ id: req.userId }).first();
    res.json(await serializeUser(row, { includeEmail: true }));
  } catch (err) {
    next(err);
  }
});

// ── Photo upload ───────────────────────────────────────────────
const uploadDir = path.resolve(config.uploadDir);
if (!fs.existsSync(uploadDir)) fs.mkdirSync(uploadDir, { recursive: true });

const storage = multer.diskStorage({
  destination: uploadDir,
  filename: (req, file, cb) => {
    const ext = path.extname(file.originalname) || '.jpg';
    cb(null, `${uuidv4()}${ext}`);
  },
});
const upload = multer({
  storage,
  limits: { fileSize: 8 * 1024 * 1024 },
  fileFilter: (req, file, cb) => {
    if (!/^image\//.test(file.mimetype)) return cb(new Error('Only image uploads allowed'));
    cb(null, true);
  },
});

router.post('/me/photos', authRequired, upload.single('photo'), async (req, res, next) => {
  try {
    if (!req.file) {
      return res.status(400).json({ error: { code: 'no_file', message: 'No file uploaded' } });
    }
    const existing = await knex('user_photos').where({ user_id: req.userId }).count('* as c').first();
    if (parseInt(existing.c, 10) >= 6) {
      fs.unlinkSync(req.file.path);
      return res.status(400).json({
        error: { code: 'too_many_photos', message: 'Maximum 6 photos allowed' },
      });
    }
    const url = `${config.publicUrl}/uploads/${req.file.filename}`;
    const position = parseInt(existing.c, 10);
    const [row] = await knex('user_photos')
      .insert({ user_id: req.userId, url, position })
      .returning('*');
    res.status(201).json({ url, id: row.id, position });
  } catch (err) {
    next(err);
  }
});

router.delete('/me/photos/:photoId', authRequired, async (req, res, next) => {
  try {
    const row = await knex('user_photos')
      .where({ id: req.params.photoId, user_id: req.userId })
      .first();
    if (!row) {
      return res.status(404).json({ error: { code: 'not_found', message: 'Photo not found' } });
    }
    // Try to delete the file from disk; ignore failures.
    try {
      const filename = row.url.split('/').pop();
      const fp = path.join(uploadDir, filename);
      if (fs.existsSync(fp)) fs.unlinkSync(fp);
    } catch {
      /* ignore */
    }
    await knex('user_photos').where({ id: row.id }).del();
    res.json({ ok: true });
  } catch (err) {
    next(err);
  }
});

router.get('/search', authRequired, async (req, res, next) => {
  try {
    const { handle } = req.query;
    if (!handle) {
      return res.status(400).json({ error: { code: 'bad_request', message: 'handle query param required' } });
    }
    const row = await knex('users').where({ user_handle: handle.toLowerCase() }).first();
    if (!row) {
      return res.status(404).json({ error: { code: 'not_found', message: 'User not found' } });
    }
    res.json(await serializeUser(row));
  } catch (err) {
    next(err);
  }
});

router.post('/me/fcm-token', authRequired, async (req, res, next) => {
  try {
    const { fcmToken } = req.body;
    if (!fcmToken) return res.status(400).json({ error: { code: 'bad_request', message: 'fcmToken required' } });
    await knex('users').where({ id: req.userId }).update({ fcm_token: fcmToken });
    res.json({ ok: true });
  } catch (err) {
    next(err);
  }
});

router.get('/:id', authRequired, async (req, res, next) => {
  try {
    const row = await knex('users').where({ id: req.params.id }).first();
    if (!row) return res.status(404).json({ error: { code: 'not_found', message: 'User not found' } });
    res.json(await serializeUser(row));
  } catch (err) {
    next(err);
  }
});

module.exports = router;
