'use strict';

const knex = require('../db/knex');

/**
 * Find an existing match between two users (in either order), regardless of unmatched flag.
 */
async function findMatch(userA, userB) {
  return knex('matches')
    .where(function () {
      this.where({ user_a: userA, user_b: userB }).orWhere({ user_a: userB, user_b: userA });
    })
    .first();
}

/**
 * Record a swipe and (if mutual like) create a match.
 * Returns { matched: bool, match: row|null }.
 */
async function recordSwipe(userId, targetUserId, direction) {
  // Upsert swipe
  await knex('swipes')
    .insert({ user_id: userId, target_user_id: targetUserId, direction })
    .onConflict(['user_id', 'target_user_id'])
    .merge({ direction, created_at: knex.fn.now() });

  if (direction === 'pass') return { matched: false, match: null };

  // Did the target already swipe like/superlike on us?
  const reciprocal = await knex('swipes')
    .where({ user_id: targetUserId, target_user_id: userId })
    .whereIn('direction', ['like', 'superlike'])
    .first();
  if (!reciprocal) return { matched: false, match: null };

  // Create a match (or reuse existing one)
  let match = await findMatch(userId, targetUserId);
  if (!match) {
    const [created] = await knex('matches')
      .insert({ user_a: userId, user_b: targetUserId })
      .returning('*');
    match = created;
  } else if (match.is_unmatched) {
    // re-activate the match
    await knex('matches').where({ id: match.id }).update({ is_unmatched: false, unmatched_by: null });
    match = await knex('matches').where({ id: match.id }).first();
  }
  return { matched: true, match };
}

module.exports = { findMatch, recordSwipe };
