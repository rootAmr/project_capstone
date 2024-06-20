const bookmarksModel = require('../models/bookmarks');

const getAllBookmark = async (req, res) => {
    try {
        const data = await bookmarksModel.getAllBookmark();
        res.json({
            message: 'Get all bookmarks success',
            data: data
        });
    } catch (error) {
        res.status(500).json({
            message: 'Server error',
            serverMessage: error.message,
        });
    }
};

const createNewBookmark = async (req, res) => {
    const { body } = req;
    const userId = req.user.user_id; // Assuming user ID is available in req.user
    try {
        const newBookmark = await bookmarksModel.createNewBookmark({ ...body, user_id: userId });
        res.status(201).json({
            message: 'Create new bookmark success',
            data: newBookmark
        });
    } catch (error) {
        res.status(500).json({
            message: 'Server error',
            serverMessage: error.message,
        });
    }
};

const deleteBookmark = async (req, res) => {
    const { idBookmark } = req.params;
    const userId = req.user.user_id; // Assuming user ID is available in req.user
    try {
        await bookmarksModel.deleteBookmark(userId, parseInt(idBookmark));
        res.json({
            message: 'Delete bookmark success',
        });
    } catch (error) {
        res.status(500).json({
            message: 'Server error',
            serverMessage: error.message,
        });
    }
};

module.exports = {
    getAllBookmark,
    createNewBookmark,
    deleteBookmark
};


const updateBookmark = async (req, res) => {
    const { idBookmark } = req.params;
    const { body } = req;

    try {
        const updatedBookmark = await bookmarksModel.updateBookmark(body, idBookmark);
        res.status(200).json({
            message: 'Update bookmark success',
            data: updatedBookmark,
        });
    } catch (error) {
        res.status(500).json({
            message: 'Server error',
            serverMessage: error.message,
        });
    }
}
module.exports = {
    getAllBookmark,
    createNewBookmark,
    updateBookmark,
    deleteBookmark
}
