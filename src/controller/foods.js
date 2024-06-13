const { Storage } = require('@google-cloud/storage');
const path = require('path');

const storage = new Storage({
    keyFilename: path.join(__dirname, '../../serviceAccount.json')
});

const bucket = storage.bucket('food-mood-capstone.appspot.com');

const uploadFile = async (file) => {
    return new Promise((resolve, reject) => {
        if (!file) {
            reject('No file provided');
            return;
        }

        const { originalname, buffer } = file;

        const blob = bucket.file(originalname.replace(/ /g, "_"));
        const blobStream = blob.createWriteStream({
            resumable: false
        });

        blobStream.on('finish', () => {
            const publicUrl = `https://storage.googleapis.com/${food-mood-capstone.appspot.com}/${blob.name}`;
            resolve(publicUrl);
        });

        blobStream.on('error', (error) => {
            reject(`Unable to upload file: ${error}`);
        });

        blobStream.end(buffer);
    });
};

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
    const { body, file } = req; // Assuming `file` is the uploaded file object
    const userId = req.user.user_id;

    try {
        let imageUrl = null;

        if (file) {
            imageUrl = await uploadFile(file);
        }

        const newFood = await foodsModel.createNewFood({ ...body, imageUrl }, userId);
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
    deleteFood,
};
