const express = require('express');

const userController = require('../controller/users');

const router = express.Router();

//CREATE - POST
router.post('/', userController.createNewUsers);

// READ - GET
router.get('/', userController.getAllUsers);

// UPDATE - PATCH
router.patch('/:idUsers', userController.updateUsers);

// DELETE - DELETE
router.delete('/:idUsers', userController.deleteUsers);

module.exports = router;