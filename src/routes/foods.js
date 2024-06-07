const express = require('express');

const foodsController = require('../controller/foods');

const router = express.Router();

//CREATE - POST
router.post('/', foodsController.createNewFood);

// READ - GET
router.get('/', foodsController.getAllFood);

// UPDATE - PATCH
router.patch('/:idFood', foodsController.updateFood);

// DELETE - DELETE
router.delete('/:idFood', foodsController.deleteFood);

module.exports = router;