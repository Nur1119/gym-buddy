'use strict';

exports.up = async function (knex) {
  await knex.schema.table('users', (t) => {
    t.string('google_id').nullable().unique();
  });
  // Make password_hash nullable for Google-only accounts
  await knex.raw('ALTER TABLE users ALTER COLUMN password_hash DROP NOT NULL');
};

exports.down = async function (knex) {
  await knex.raw('ALTER TABLE users ALTER COLUMN password_hash SET NOT NULL');
  await knex.schema.table('users', (t) => {
    t.dropColumn('google_id');
  });
};
