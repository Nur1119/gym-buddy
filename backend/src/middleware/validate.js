'use strict';

const { ZodError } = require('zod');

function validate(schema) {
  return (req, res, next) => {
    try {
      req.body = schema.parse(req.body);
      next();
    } catch (err) {
      if (err instanceof ZodError) {
        return res.status(400).json({
          error: {
            code: 'validation_error',
            message: 'Request body failed validation',
            details: err.errors,
          },
        });
      }
      next(err);
    }
  };
}

function validateQuery(schema) {
  return (req, res, next) => {
    try {
      req.query = schema.parse(req.query);
      next();
    } catch (err) {
      if (err instanceof ZodError) {
        return res.status(400).json({
          error: {
            code: 'validation_error',
            message: 'Query parameters failed validation',
            details: err.errors,
          },
        });
      }
      next(err);
    }
  };
}

module.exports = { validate, validateQuery };
