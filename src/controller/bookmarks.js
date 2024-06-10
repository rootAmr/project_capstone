const bookmarksModel = require('../models/bookmarks');

const getAllBookmark = async (req, res) =>{

    try {
        const [data] = await bookmarksModel.getAllBookmark();
    
        res.json({
            message: 'get all Bookmark success',
            data: data
        })
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        })
    }

}

const createNewBookmark = async (req, res) => {
    const {body} = req;
    try {
        await bookmarksModel.createNewBookmark(body);
        res.status(201).json({
            message: 'create new Bookmark success',
            data: body
        });
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        })
    }
}

const updateBookmark = async (req, res) =>{
    const {idBookmark} = req.params;
    const {body} = req;

    try {
        await bookmarksModel.updateBookmark(body, idBookmark);
        res.status(201).json({
            message: 'Update Bookmark Success',
            data: {
                id: idBookmark,
                ...body
            },
        })
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        })
    }
}

const deleteBookmark = async (req, res) =>{
    const {idBookmark} = req.params;
    try {
        await bookmarksModel.deleteBookmark(idBookmark);
        res.json({
            message: 'delete Bookmark success',
        })
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        })
    }
}

module.exports = {
    getAllBookmark,
    createNewBookmark,
    updateBookmark,
    deleteBookmark
}