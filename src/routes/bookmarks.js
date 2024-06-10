const express = require('express');

const bookmarksController = require('../controller/bookmarks');

const router = express.Router();

//CREATE - POST
router.post('/', bookmarksController.createNewBookmark);

// READ - GET
router.get('/', bookmarksController.getAllBookmark);

// UPDATE - PATCH
router.patch('/:idBookmark', bookmarksController.updateBookmark);

// DELETE - DELETE
router.delete('/:idBookmark', bookmarksController.deleteBookmark);

module.exports = router;