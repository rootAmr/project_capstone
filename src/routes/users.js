const express = require('express');
const multer = require('multer');
const userController = require('../controller/users');

const router = express.Router();
const upload = multer({
    storage: multer.memoryStorage()
});

// CREATE - POST
router.post('/', upload.single('profile'), userController.createNewUsers);

// READ - GET
router.get('/', userController.getAllUsers);

// UPDATE - PATCH
router.patch('/:idUsers', upload.single('profile'), userController.updateUsers);

// DELETE - DELETE
router.delete('/:idUsers', userController.deleteUsers);

// LOGIN - POST
router.post('/login', userController.loginUser);

// REGISTER - POST
router.post('/register', upload.single('profile'), userController.registerUser);

module.exports = router;
