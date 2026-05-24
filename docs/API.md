# GymBuddy — API Contract

Base URL: `http://localhost:3000/api/v1`

Auth: Bearer JWT in `Authorization: Bearer <token>` header.

---

## Conventions

- All requests/responses are JSON.
- Timestamps are ISO 8601 strings (`2026-05-24T18:00:00Z`).
- IDs are UUIDs.
- Errors: `{ "error": { "code": "string", "message": "string" } }` with appropriate HTTP status.
- Pagination: `?limit=20&cursor=<base64>` returns `{ items: [...], nextCursor: string|null }`.

---

## Auth

### `POST /auth/register`
Request:
```json
{ "email": "alex@example.com", "password": "secret123", "name": "Alex", "age": 28 }
```
Response (201):
```json
{ "token": "jwt...", "user": { /* User */ } }
```

### `POST /auth/login`
Request:
```json
{ "email": "alex@example.com", "password": "secret123" }
```
Response (200):
```json
{ "token": "jwt...", "user": { /* User */ } }
```

### `POST /auth/refresh`
Header: `Authorization: Bearer <token>`
Response: `{ "token": "jwt..." }`

### `GET /auth/me`
Returns the current `User`.

---

## Users / Profile

### `GET /users/me`
Returns full `User` object including stats.

### `PATCH /users/me`
Updates own profile.
```json
{ "name": "Alex", "age": 28, "height": 181, "weight": 82, "bio": "...", "goal": "Hypertrophy", "interests": ["Powerlifting"] }
```

### `POST /users/me/photos`
Multipart upload. Returns `{ "url": "https://..." }`. User has 1..6 photos.

### `DELETE /users/me/photos/:photoId`

### `GET /users/:id`
Public profile of another user.

---

## Discover (Tinder-style)

### `GET /discover/feed?limit=10`
Returns next batch of candidate `User`s based on filters and exclusion (already swiped).
Response: `{ "items": [User, ...] }`

### `GET /discover/filters`
Returns current user's saved filters.
```json
{ "ageMin": 21, "ageMax": 35, "maxDistanceKm": 15, "goals": ["Strength","Hypertrophy"], "levels": ["Intermediate","Advanced"], "scheduleDays": [1,2,3,4,5] }
```

### `PUT /discover/filters`
Updates saved filters (same shape).

### `POST /discover/swipe`
Records a swipe.
```json
{ "targetUserId": "uuid", "direction": "like" | "pass" | "superlike" }
```
Response:
```json
{ "matched": true, "match": { /* Match */ } }
```
or `{ "matched": false }`.

### `POST /discover/rewind`
Undoes the last swipe (premium). Returns the user card to put back on top.

### `POST /discover/boost`
Activates a 30-min boost. Returns `{ "expiresAt": "..." }`.

---

## Matches

### `GET /matches`
Returns list of `Match` ordered by last activity.
```json
{ "items": [{ "id": "uuid", "user": User, "lastMessage": Message|null, "unreadCount": 2, "matchedAt": "..." }] }
```

### `DELETE /matches/:id`
Unmatch.

---

## Chat

### `GET /matches/:id/messages?limit=50&before=<cursor>`
Returns messages, newest first.

### `POST /matches/:id/messages`
```json
{ "text": "Hello", "kind": "text" }
```
or workout invite:
```json
{ "kind": "workout_invite", "payload": { "date": "2026-05-26T07:00:00Z", "gymId": "uuid", "routineId": "uuid" } }
```

### `WS /matches/:id/stream`
WebSocket connection for real-time messages. Server pushes events:
```json
{ "type": "message", "data": Message }
{ "type": "typing", "userId": "..." }
{ "type": "read", "messageId": "..." }
```

---

## Workouts

### `GET /workouts`
List of completed/planned workout sessions for current user.

### `POST /workouts`
Create a new workout (planned or in-progress).
```json
{ "name": "Upper body", "plannedFor": "2026-05-26T18:00:00Z", "routineId": "uuid|null", "exercises": [{ "exerciseId": "uuid", "sets": [{ "weight": 50, "reps": 10 }] }] }
```

### `GET /workouts/:id`
### `PATCH /workouts/:id`
Update sets as they're completed.
### `POST /workouts/:id/finish`
Mark workout completed; awards XP.

### `GET /workouts/stats`
Returns aggregate stats: weeklyVolume, muscleHeatmap, workoutsThisWeek.

---

## Exercises

### `GET /exercises?muscle=Chest&search=bench`
Returns paginated list of exercises (built-in + user's custom).

### `POST /exercises`
Create a custom exercise.
```json
{ "name": "Close-grip bench", "muscle": "Chest", "equipment": "Barbell", "notes": "..." }
```

### `GET /exercises/:id`

---

## Routines

### `GET /routines`
User's saved routines.

### `POST /routines`
```json
{ "name": "Upper body", "exercises": [{ "exerciseId": "uuid", "sets": 3, "targetReps": 10 }] }
```

### `GET /routines/:id`
### `PATCH /routines/:id`
### `DELETE /routines/:id`

---

## Friends

### `GET /friends`
### `POST /friends/requests` — `{ "userId": "uuid" }`
### `GET /friends/requests`
### `POST /friends/requests/:id/accept`
### `POST /friends/requests/:id/decline`
### `DELETE /friends/:id`

---

## Leaderboard

### `GET /leaderboard?scope=global|friends&period=week|month|all`
Returns top users with XP.

---

## Nutrition

### `GET /nutrition/day?date=2026-05-24`
Returns meals + macros for a day.

### `POST /nutrition/meals`
```json
{ "kind": "breakfast", "items": [{ "name": "Oats", "kcal": 200, "protein": 8, "carbs": 35, "fats": 4 }], "time": "08:00" }
```

### `DELETE /nutrition/meals/:id`

---

## Achievements / Medals

### `GET /medals`
Returns all medals with unlocked status for current user.

### `GET /quests`
Returns current active quests (daily/weekly) with progress.

---

## Calendar

### `GET /calendar?month=2026-05`
Returns workouts scheduled and completed in the month.

---

## Data Models

### `User`
```ts
{
  id: string,
  email: string,
  name: string,
  username: string,
  age: number,
  height: number,    // cm
  weight: number,    // kg
  bio: string,
  goal: "Strength" | "Hypertrophy" | "Mobility" | "Calisthenics" | "CrossFit" | "Cardio",
  level: "Beginner" | "Intermediate" | "Advanced" | "Elite",
  gymName: string | null,
  gymLat: number | null,
  gymLng: number | null,
  schedule: number[],   // 0=Sun .. 6=Sat
  interests: string[],
  photos: { id: string, url: string, position: number }[],
  stats: {
    level: number, xp: number, xpToNext: number, totalXp: number,
    streak: number, bestStreak: number, coins: number,
    totalWorkouts: number, workoutsThisWeek: number,
  },
  createdAt: string,
}
```

### `Match`
```ts
{
  id: string,
  user: User,          // the other user
  matchedAt: string,
  lastMessage: Message | null,
  unreadCount: number,
  isUnmatched: boolean,
}
```

### `Message`
```ts
{
  id: string,
  matchId: string,
  senderId: string,
  kind: "text" | "image" | "workout_invite",
  text: string | null,
  imageUrl: string | null,
  payload: object | null,    // for workout_invite
  createdAt: string,
  readAt: string | null,
}
```

### `Exercise`
```ts
{
  id: string,
  name: string,
  muscle: "Chest" | "Back" | "Legs" | "Shoulders" | "Arms" | "Core" | "Cardio",
  equipment: "Barbell" | "Dumbbell" | "Machine" | "Cable" | "Bodyweight",
  icon: string,
  notes: string | null,
  isCustom: boolean,
  ownerId: string | null,
}
```

### `Routine`
```ts
{
  id: string,
  name: string,
  exercises: { exerciseId: string, sets: number, targetReps: number, restSec: number }[],
  totalSets: number,
  estimatedDurationMin: number,
  color: string,
  ownerId: string,
  createdAt: string,
}
```

### `Workout`
```ts
{
  id: string,
  name: string,
  startedAt: string | null,
  finishedAt: string | null,
  plannedFor: string | null,
  routineId: string | null,
  exercises: {
    exerciseId: string,
    sets: { weight: number, reps: number, completed: boolean, restSec: number }[],
  }[],
  totalVolumeKg: number,
  xpAwarded: number,
}
```
