const foodsModel = require('../models/foods');
const uploadFileToGCS = require('../config/gcsConfig');

const getAllFood = async (req, res) => {
    try {
        const data = await foodsModel.getAllFood();
        res.json({
            message: 'get all food success',
            data: data
        });
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        });
    }
};

const getFoodById = async (req, res) => {
    const { idFood } = req.params;
    try {
        const food = await foodsModel.getFoodById(parseInt(idFood));
        if (food) {
            res.json({
                message: 'get food by id success',
                data: food
            });
        } else {
            res.status(404).json({
                message: 'food not found'
            });
        }
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        });
    }
};

const searchFoods = async (req, res) => {
    const { query } = req.body;
    if (!query || typeof query !== 'string') {
        return res.status(400).json({
            message: 'Invalid query',
            serverMessage: 'Query parameter is required and must be a string',
        });
    }

    try {
        const foods = await foodsModel.searchFoods(query);
        res.json({
            message: 'search foods success',
            data: foods
        });
    } catch (error) {
        console.error(`Error searching foods: ${error}`);
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        });
    }
};

const createNewFood = async (req, res) => {
    const { body } = req;
    const userId = req.user.uid; // Updated to use Firebase uid
    if (req.file) {
        try {
            const publicUrl = await uploadFileToGCS(req.file);
            body.image = publicUrl;
        } catch (error) {
            return res.status(500).json({
                message: 'Failed to upload image to GCS',
                serverMessage: error.message,
            });
        }
    }
    try {
        const newFood = await foodsModel.createNewFood(body, userId);
        res.status(201).json({
            message: 'create new food success',
            data: newFood
        });
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        });
    }
};

const updateFood = async (req, res) => {
    const { idFood } = req.params;
    const { body } = req;
    const userId = req.user.uid; // Updated to use Firebase uid
    if (req.file) {
        try {
            const publicUrl = await uploadFileToGCS(req.file);
            body.image = publicUrl;
        } catch (error) {
            return res.status(500).json({
                message: 'Failed to upload image to GCS',
                serverMessage: error.message,
            });
        }
    }

    try {
        const updatedFood = await foodsModel.updateFood(body, parseInt(idFood), userId);
        res.status(201).json({
            message: 'Update Food Success',
            data: {
                id: idFood,
                ...body
            },
        });
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        });
    }
};

const deleteFood = async (req, res) => {
    const { idFood } = req.params;
    const userId = req.user.uid; // Updated to use Firebase uid
    try {
        const deletedFood = await foodsModel.deleteFood(parseInt(idFood), userId);
        if (deletedFood.count === 0) {
            res.status(404).json({ message: 'Food not found or not authorized' });
        } else {
            res.json({
                message: 'delete Food success',
            });
        }
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        });
    }
};

module.exports = {
    getAllFood,
    getFoodById,
    searchFoods, // Ensure this is exported
    createNewFood,
    updateFood,
    deleteFood
};
