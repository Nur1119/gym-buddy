# GymBuddy Backend

Node.js + Express + PostgreSQL backend for the GymBuddy fitness/matching app.
Consumed by the Swift iOS and Kotlin Android clients.

## Stack

- **Runtime:** Node.js 20+
- **Framework:** Express 4
- **Database:** PostgreSQL 16 (via Knex.js)
- **Auth:** JWT (7-day expiry), bcrypt-hashed passwords
- **Validation:** zod
- **Realtime:** ws (WebSocket) for chat
- **Uploads:** multer, files written to `./uploads/` and served as static

## Quick start

```bash
# 1. Bring up Postgres
docker compose up -d

# 2. Configure environment
cp .env.example .env
# (defaults match docker-compose.yml — usually no edit needed for dev)

# 3. Install deps, migrate, seed
npm install
npm run migrate
npm run seed

# 4. Start the server
npm start
```

Server listens on `http://localhost:3000` (configurable via `PORT`).
WebSocket chat endpoint: `ws://localhost:3000/api/v1/matches/:id/stream?token=<jwt>`.

### Demo login

```
email: alex@gymbuddy.app
password: password123
```

(All seeded users use `password123`.)

## Smoke test

```bash
# Log in
TOKEN=$(curl -s -X POST localhost:3000/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"alex@gymbuddy.app","password":"password123"}' \
  | jq -r .token)

curl localhost:3000/api/v1/discover/feed -H "Authorization: Bearer $TOKEN"
curl localhost:3000/api/v1/exercises    -H "Authorization: Bearer $TOKEN"
curl localhost:3000/api/v1/matches      -H "Authorization: Bearer $TOKEN"
```

## Layout

```
backend/
  src/
    index.js              # entry — boots HTTP + WS
    config.js             # env config (PORT, DATABASE_URL, JWT_SECRET, ...)
    db/
      knex.js
      knexfile.js
      migrations/         # 20260524000000_init.js — full schema
      seeds/              # 01_seed.js — fixture data
    middleware/
      auth.js             # JWT verification + token signing
      error.js            # notFound + errorHandler
      validate.js         # zod-based body/query validation
    routes/
      auth.js             # register, login, refresh, me
      users.js            # /users/me, photo upload, public profile
      discover.js         # feed, filters, swipe, rewind, boost
      matches.js          # list, unmatch
      messages.js         # per-match message history + send
      workouts.js         # CRUD + finish + stats
      exercises.js        # built-in + custom library
      routines.js         # CRUD
      friends.js          # list, requests, accept/decline
      leaderboard.js      # global / friends scope
      nutrition.js        # day summary, add/delete meals
      medals.js           # achievements
      quests.js           # daily / weekly quests
      calendar.js         # monthly workout view
    services/
      serializers.js      # row → API contract shape
      matching.js         # swipe + match creation
      xp.js               # workout XP formula + leveling
    ws/
      chat.js             # WebSocket upgrade + room broadcast
  uploads/                # photo storage (served at /uploads/<file>)
  docker-compose.yml      # Postgres 16
  .env.example
  package.json
```

## Endpoints

All endpoints from `docs/API.md` are implemented under `/api/v1`. Auth is via
`Authorization: Bearer <jwt>` header (except `POST /auth/register`,
`POST /auth/login`).

| Endpoint | Status |
|---|---|
| `POST /auth/register` | works |
| `POST /auth/login` | works |
| `POST /auth/refresh` | works |
| `GET  /auth/me` | works |
| `GET  /users/me` | works |
| `PATCH /users/me` | works |
| `POST /users/me/photos` | works (multipart, local disk) |
| `DELETE /users/me/photos/:photoId` | works |
| `GET  /users/:id` | works |
| `GET  /discover/feed` | works |
| `GET/PUT /discover/filters` | works |
| `POST /discover/swipe` | works — mutual like creates match |
| `POST /discover/rewind` | works (no premium gating) |
| `POST /discover/boost` | stub — returns `expiresAt` only, no actual feed boost |
| `GET  /matches` | works |
| `DELETE /matches/:id` | works (soft unmatch) |
| `GET  /matches/:id/messages` | works (cursor pagination) |
| `POST /matches/:id/messages` | works (text / image / workout_invite) |
| `WS   /matches/:id/stream` | works (broadcasts on REST POST, `typing`, `read` events) |
| `GET  /workouts` | works |
| `POST /workouts` | works |
| `GET  /workouts/:id` | works |
| `PATCH /workouts/:id` | works |
| `POST /workouts/:id/finish` | works (computes volume + awards XP) |
| `GET  /workouts/stats` | works (weekly volume, muscle heatmap, count) |
| `GET  /exercises` | works (built-in + own custom) |
| `POST /exercises` | works (custom) |
| `GET  /exercises/:id` | works |
| `GET/POST/PATCH/DELETE /routines` | works |
| `GET  /friends` | works |
| `POST /friends/requests` | works |
| `GET  /friends/requests` | works (incoming + outgoing) |
| `POST /friends/requests/:id/accept` | works |
| `POST /friends/requests/:id/decline` | works |
| `DELETE /friends/:id` | works (`:id` here is the friend's user id) |
| `GET  /leaderboard` | works (global / friends scope) |
| `GET  /nutrition/day` | works |
| `POST /nutrition/meals` | works |
| `DELETE /nutrition/meals/:id` | works |
| `GET  /medals` | works |
| `GET  /quests` | works |
| `GET  /calendar` | works |
| `GET  /health` | works (sanity check) |

### Not yet implemented / stubs

- **`POST /discover/boost`** — returns `expiresAt` 30 min out, but the discover feed
  ranking does not actually surface the boosted user faster. Add a `boost_expires_at`
  column and order by that in `/discover/feed` when implementing fully.
- **Leaderboard `period=week|month`** — the API accepts the query param but always
  ranks by `users.stat_total_xp`. A real weekly/monthly leaderboard needs an
  `xp_events` audit table; skipped for the MVP.
- **Distance filter in `/discover/feed`** — `maxDistanceKm` is stored on
  `user_filters` but not used in the candidate query (needs PostGIS or haversine).
- **Read receipts via REST** — `read_at` is set when the WebSocket client sends
  `{ type: "read", messageId }`; there is no REST endpoint to mark a thread as read.
- **Quest auto-progression** — quests are seeded with static progress; the
  workout-finish hook does not yet advance quest progress.
- **Medal unlocking** — medals are seeded as already-unlocked for the demo user;
  there is no service that grants new medals based on milestones.
- **Refresh tokens** — `POST /auth/refresh` re-signs the access token; there is no
  separate refresh-token rotation table. Fine for a demo, not production-grade.

## Database schema

All tables are created in a single migration: `src/db/migrations/20260524000000_init.js`.

Notable tables: `users`, `user_photos`, `user_filters`, `swipes`, `matches`,
`messages`, `exercises`, `routines`, `routine_exercises`, `workouts`,
`workout_exercises`, `workout_sets`, `friends`, `friend_requests`, `meals`,
`medals`, `user_medals`, `quests`, `user_quests`.

Indices on hot paths: `swipes(user_id, target_user_id) unique`,
`matches(user_a)`, `matches(user_b)`, `matches(last_activity_at)`,
`messages(match_id, created_at)`, `exercises(muscle)`, `meals(user_id, date)`.

## Seed contents

- 1 demo user (`alex@gymbuddy.app`)
- 10 discover candidates (Alina, Marcus, Sofia, Daniel, Emma, Noah, Maya, Liam, Priya, Kenji)
- 3 friend-only users (Jake T., Lena K., Carlos P.)
- 16 built-in exercises (from `project/data.jsx`)
- 5 routines owned by the demo user
- 3 pre-existing matches (Alex ↔ Alina/Sofia/Emma) with chat history
- 6 medals (4 unlocked for demo user) + 3 quests with partial progress
- 1 completed workout (last-Monday Upper body) for stats

## Environment variables

| Var | Default | Notes |
|---|---|---|
| `PORT` | `3000` | HTTP + WS port |
| `NODE_ENV` | `development` | `production` hides error messages |
| `DATABASE_URL` | `postgres://gymbuddy:gymbuddy@localhost:5432/gymbuddy` | matches docker-compose |
| `JWT_SECRET` | (required for prod) | long random string |
| `JWT_EXPIRES_IN` | `7d` | jsonwebtoken expiry |
| `UPLOAD_DIR` | `./uploads` | photo storage |
| `PUBLIC_URL` | `http://localhost:3000` | used to build photo URLs |

## Scripts

```
npm start          # node src/index.js
npm run dev        # node --watch src/index.js
npm run migrate    # apply pending migrations
npm run migrate:rollback
npm run seed       # wipe + reseed (idempotent)
```
