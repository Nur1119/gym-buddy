# GymBuddy

Fitness app combining workout tracking with a Tinder-style gym-buddy matching feature.

This repository contains three independent codebases plus shared docs:

- **`backend/`** — Node.js + Express + PostgreSQL REST API + WebSocket chat
- **`ios/`** — Native iOS app, Swift + SwiftUI (iOS 17+)
- **`android/`** — Native Android app, Kotlin + Jetpack Compose (Android 8+)
- **`docs/`** — API contract, design tokens, architecture notes
- **`project/`** — Original HTML/CSS/JS design prototype (reference only — not built)
- **`chats/`** — Original handoff transcripts from Claude Design

## Quick start

### 1. Start the backend

```bash
cd backend
cp .env.example .env
docker compose up -d        # starts Postgres on :5432
npm install
npm run migrate
npm run seed
npm start                   # API on :3000
```

Demo account after seeding:
- email: `alex@gymbuddy.app`
- password: `password123`

### 2. Run iOS

Open `ios/GymBuddy/GymBuddy.xcodeproj` in Xcode 15 or newer. Pick an iPhone 15 simulator and ⌘R.
The simulator can reach `localhost:3000` directly.

### 3. Run Android

Open `android/` in Android Studio Iguana or newer. Sync Gradle, then ▶ on an Android 14 emulator.
The emulator reaches the host via `10.0.2.2:3000` — this is already configured.

## Architecture

```
┌──────────────┐     ┌──────────────┐
│  iOS (Swift) │     │ Android (Kt) │
└──────┬───────┘     └──────┬───────┘
       │ HTTPS + WSS         │
       └──────────┬──────────┘
                  │
           ┌──────▼──────┐
           │   Express   │
           │   + WS      │
           └──────┬──────┘
                  │
           ┌──────▼──────┐
           │  Postgres   │
           └─────────────┘
```

- REST for everything except chat
- WebSocket per match for real-time messages
- JWT auth, 7-day tokens, refresh endpoint
- Photos uploaded to local disk in dev; swap to S3 for prod

## What's in scope here

This is the MVP build. See `docs/API.md` for the full API contract and `docs/DESIGN_TOKENS.md` for design system tokens shared across platforms.

Each subdirectory has its own README with platform-specific setup, what's polished vs stubbed, and known TODOs.
