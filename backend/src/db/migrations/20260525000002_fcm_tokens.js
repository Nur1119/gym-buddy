'use strict';

exports.up = async function (knex) {
  await knex.schema.table('users', (t) => {
    t.string('fcm_token').nullable();
  });
};

exports.down = async function (knex) {
  await knex.schema.table('users', (t) => {
    t.dropColumn('fcm_token');
  });
};
