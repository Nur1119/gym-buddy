# GymBuddy — Design Tokens

Shared across iOS (Swift), Android (Kotlin), and (for reference) the web prototype.

## Colors

### Light theme
- bg: `#F4F6FB`
- surface: `#FFFFFF`
- surface2: `#F1F4FA`
- border: `#E4E8F0`
- borderStrong: `#D2D8E3`
- text: `#0B1020`
- textMuted: `#5B6478`
- textDim: `#8B92A8`
- chip: `#EEF1F8`
- danger: `#FF3B5C`
- success: `#19C37D`
- warn: `#FFB020`

### Dark theme
- bg: `#0A0E1A`
- surface: `#141A2B`
- surface2: `#1B2238`
- surfaceElevated: `#1F2740`
- border: `#262E47`
- borderStrong: `#39426A`
- text: `#FFFFFF`
- textMuted: `#A2ABC5`
- textDim: `#6A7390`
- chip: `#1F2740`
- danger: `#FF4D6D`
- success: `#3DDC97`
- warn: `#FFC857`

### Accent — Aurora (default)
- p1: `#3DDC97` (green)
- p2: `#00C2FF` (cyan)
- p3: `#7C5CFF` (purple)
- gradient: `135deg p3 → p2 → p1` (stops: 0 / 55 / 100)
- like: `#3DDC97`
- nope: `#FF4D6D`
- superLike: `#00C2FF`
- boost: `#B967FF`

### Accent — Sunset
- p1: `#FFB020`, p2: `#FF4D6D`, p3: `#B967FF`
- gradient: `135deg p1 → p2 → p3`

### Accent — Neon (electric)
- p1: `#00E5FF`, p2: `#39FF14`, p3: `#FFEB3B`
- gradient: `135deg p1 → p2 → p3`

## Typography
- iOS: SF Pro Display (system), Android: Roboto (system)
- Title XL: 34/41, weight 700 (iOS large title)
- Title: 22, weight 800, letter-spacing -0.5
- Heading: 17, weight 700
- Body: 14-15, weight 500-600
- Caption: 11-12, weight 600
- Numeric mono: ui-monospace (timers, set numbers)

## Spacing
- 4, 6, 8, 10, 12, 14, 16, 18, 20, 24, 30 px

## Radii
- 6, 8, 10, 12, 14, 16, 18, 20 (cards), 24, 999 (pill)

## Shadows
- Card: `0 2px 6px rgba(0,0,0,0.06)`
- Elevated: `0 12px 32px rgba(0,194,255,0.25)` (uses p2 with alpha)
- Action button: `0 6px 20px rgba(0,0,0,0.15)` + colored glow at 0 0 30px

## Icons
24px viewBox, 2px stroke, line style. Named: home, dumbbell, flame, spark, heart, heart-fill, user, users, cog, plus, check, close, chevron-right/left/down, arrow-right/left, search, calendar, bell, filter, bolt, rewind, star, send, apple, medal, trophy, chart, play, pause, image, camera, edit, language, moon, sun, location, clock, list, grid, more, globe, sliders.

## Languages
- en (default), ru — same keys as prototype `STRINGS` in i18n.jsx.
