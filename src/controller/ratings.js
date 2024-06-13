const ratingsModel = require('../models/ratings');

const getAllRating = async (req, res) => {
    try {
        const data = await ratingsModel.getAllRating();
        res.json({
            message: 'get all Rating success',
            data: data
        });
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error.message,
        });
    }
};

const createNewRating = async (req, res) => {
    const { body } = req;
    const userId = req.user.user_id; // Assuming you want to use userId as well

    try {
        const newRating = await ratingsModel.createNewRating(body, userId); // Pass userId to the model if needed
        res.status(201).json({
            message: 'create new Rating success',
            data: newRating
        });
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error.message,
        });
    }
};

const updateRating = async (req, res) => {
    const { idRating } = req.params;
    const { body } = req;

    try {
        await ratingsModel.updateRating(body, idRating);
        res.status(201).json({
            message: 'Update Rating Success',
            data: {
                id: idRating,
                ...body
            },
        });
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error.message,
        });
    }
};

const deleteRating = async (req, res) => {
    const { idRating } = req.params;
    try {
        await ratingsModel.deleteRating(idRating);
        res.json({
            message: 'delete Rating success',
        });
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error.message,
        });
    }
};

module.exports = {
    getAllRating,
    createNewRating,
    updateRating,
    deleteRating
};
