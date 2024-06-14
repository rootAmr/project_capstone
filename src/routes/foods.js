const express = require('express');
const foodsController = require('../controller/foods');
const upload = require('../config/multerConfig');

const router = express.Router();

// CREATE - POST
router.post('/', upload.single('image'), foodsController.createNewFood);

// READ - GET
router.get('/', foodsController.getAllFood);

// UPDATE - PATCH
router.patch('/:idFood', upload.single('image'), foodsController.updateFood);

// DELETE - DELETE
router.delete('/:idFood', foodsController.deleteFood);

module.exports = router;
