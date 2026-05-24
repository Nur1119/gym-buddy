'use strict';

const knex = require('../db/knex');

/**
 * Compute XP from a finished workout based on volume and number of completed sets.
 * Simple deterministic formula — feel free to tune.
 */
function computeWorkoutXp({ totalVolumeKg, completedSets }) {
  const volumeXp = Math.floor(totalVolumeKg / 25);
  const setXp = completedSets * 10;
  return Math.max(50, volumeXp + setXp);
}

/**
 * Add XP to a user, handling level-up (every 5000 total XP = 1 stat_level, simple rule).
 */
async function awardXp(userId, amount) {
  const user = await knex('users').where({ id: userId }).first();
  if (!user) return null;

  let xp = user.stat_xp + amount;
  let totalXp = user.stat_total_xp + amount;
  let level = user.stat_level;
  let xpToNext = user.stat_xp_to_next;

  while (xp >= xpToNext) {
    xp -= xpToNext;
    level += 1;
    xpToNext = Math.floor(xpToNext * 1.1);
  }

  await knex('users').where({ id: userId }).update({
    stat_xp: xp,
    stat_total_xp: totalXp,
    stat_level: level,
    stat_xp_to_next: xpToNext,
  });
  return { xp, totalXp, level, xpToNext };
}

module.exports = { computeWorkoutXp, awardXp };
