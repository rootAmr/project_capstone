const foodsModel = require('../models/foods');

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

const createNewFood = async (req, res) => {
    const { body } = req;
    const userId = req.user.user_id;
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
    const userId = req.user.user_id;

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
    const userId = req.user.user_id;
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
    createNewFood,
    updateFood,
    deleteFood
};
