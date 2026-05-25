'use strict';

const path = require('path');
require('dotenv').config({ path: path.resolve(__dirname, '../../.env') });

const databaseUrl =
  process.env.DATABASE_URL ||
  'postgres://gymbuddy:gymbuddy@localhost:5432/gymbuddy';

// Render-managed Postgres requires SSL; local docker setup does not.
const needsSsl = /render\.com|amazonaws\.com|herokuapp\.com/.test(databaseUrl) ||
  process.env.PGSSL === 'true' || process.env.NODE_ENV === 'production';

module.exports = {
  client: 'pg',
  connection: needsSsl
    ? { connectionString: databaseUrl, ssl: { rejectUnauthorized: false } }
    : databaseUrl,
  pool: { min: 2, max: 10 },
  migrations: {
    directory: path.resolve(__dirname, 'migrations'),
    tableName: 'knex_migrations',
  },
  seeds: {
    directory: path.resolve(__dirname, 'seeds'),
  },
};
