'use strict';

exports.up = async function (knex) {
  await knex.schema.table('users', (t) => {
    t.string('user_handle', 20).nullable().unique();
  });
  // Backfill existing users
  const users = await knex('users').select('id').orderBy('created_at', 'asc');
  for (let i = 0; i < users.length; i++) {
    await knex('users')
      .where('id', users[i].id)
      .update({ user_handle: 'gb_' + String(i + 1).padStart(8, '0') });
  }
};

exports.down = async function (knex) {
  await knex.schema.table('users', (t) => {
    t.dropColumn('user_handle');
  });
};
