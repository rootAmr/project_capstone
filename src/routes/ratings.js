const express = require('express');

const ratingsController = require('../controller/ratings');

const router = express.Router();

//CREATE - POST
router.post('/', ratingsController.createNewRating);

// READ - GET
router.get('/', ratingsController.getAllRating);

// UPDATE - PATCH
router.patch('/:idRating', ratingsController.updateRating);

// DELETE - DELETE
router.delete('/:idRating', ratingsController.deleteRating);

module.exports = router;