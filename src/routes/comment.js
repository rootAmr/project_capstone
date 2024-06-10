const express = require('express');

const commentsController = require('../controller/comments');

const router = express.Router();

//CREATE - POST
router.post('/', commentsController.createNewComment);

// READ - GET
router.get('/', commentsController.getAllComment);

// UPDATE - PATCH
router.patch('/:idComment', commentsController.updateComment);

// DELETE - DELETE
router.delete('/:idComment', commentsController.deleteComment);

module.exports = router;