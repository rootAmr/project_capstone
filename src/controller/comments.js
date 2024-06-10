const commentsModel = require('../models/comments');

const getAllComment = async (req, res) =>{

    try {
        const [data] = await commentsModel.getAllComment();
    
        res.json({
            message: 'get all Comment success',
            data: data
        })
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        })
    }

}

const createNewComment = async (req, res) => {
    const {body} = req;
    try {
        await commentsModel.createNewComment(body);
        res.status(201).json({
            message: 'create new Comment success',
            data: body
        });
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        })
    }
}

const updateComment = async (req, res) =>{
    const {idComment} = req.params;
    const {body} = req;

    try {
        await commentsModel.updateComment(body, idComment);
        res.status(201).json({
            message: 'Update Comment Success',
            data: {
                id: idComment,
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

const deleteComment = async (req, res) =>{
    const {idComment} = req.params;
    try {
        await commentsModel.deleteComment(idComment);
        res.json({
            message: 'delete Comment success',
        })
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        })
    }
}

module.exports = {
    getAllComment,
    createNewComment,
    updateComment,
    deleteComment
}