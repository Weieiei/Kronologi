import express from 'express';
import { getAllServices } from '../../controllers/service';

export const services = express.Router();

/**
 * @route       GET api/services
 * @description Get list of services offered.
 * @access      Public
 */
services.get('', getAllServices);
