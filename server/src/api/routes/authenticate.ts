import express from 'express';
import { login, register } from '../../controllers/user';

export const authenticate = express.Router();

/**
 * @route       POST api/authenticate/register
 * @description Register user of type client.
 * @access      Public
 */
authenticate.post('/register', register);

/**
 * @route       POST api/authenticate/login
 * @description Login user of any type.
 * @access      Public
 */
authenticate.post('/login', login);
