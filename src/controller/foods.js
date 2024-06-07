const foodsModel = require('../models/foods');

const getAllFood = async (req, res) =>{

    try {
        const [data] = await foodsModel.getAllFood();
    
        res.json({
            message: 'get all food success',
            data: data
        })
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        })
    }

}

const createNewFood = async (req, res) => {
    const {body} = req;
    try {
        await foodsModel.createNewFood(body);
        res.status(201).json({
            message: 'create new food success',
            data: body
        });
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        })
    }
}

const updateFood = async (req, res) =>{
    const {idFood} = req.params;
    const {body} = req;

    try {
        await foodsModel.updateFood(body, idFood);
        res.status(201).json({
            message: 'Update Food Success',
            data: {
                id: idFood,
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

const deleteFood = async (req, res) =>{
    const {idFood} = req.params;
    try {
        await foodsModel.deleteFood(idFood);
        res.json({
            message: 'delete Food success',
        })
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        })
    }
}

module.exports = {
    getAllFood,
    createNewFood,
    updateFood,
    deleteFood
}