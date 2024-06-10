const ratingsModel = require('../models/ratings');

const getAllRating = async (req, res) =>{

    try {
        const [data] = await ratingsModel.getAllRating();
    
        res.json({
            message: 'get all Rating success',
            data: data
        })
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        })
    }

}

const createNewRating = async (req, res) => {
    const {body} = req;
    try {
        await ratingsModel.createNewRating(body);
        res.status(201).json({
            message: 'create new Rating success',
            data: body
        });
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        })
    }
}

const updateRating = async (req, res) =>{
    const {idRating} = req.params;
    const {body} = req;

    try {
        await ratingsModel.updateRating(body, idRating);
        res.status(201).json({
            message: 'Update Rating Success',
            data: {
                id: idRating,
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

const deleteRating = async (req, res) =>{
    const {idRating} = req.params;
    try {
        await ratingsModel.deleteRating(idRating);
        res.json({
            message: 'delete Rating success',
        })
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        })
    }
}

module.exports = {
    getAllRating,
    createNewRating,
    updateRating,
    deleteRating
}