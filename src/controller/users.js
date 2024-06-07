const usersModel = require('../models/users');

const getAllUsers = async (req, res) =>{

    try {
        const [data] = await usersModel.getAllUsers();
    
        res.json({
            message: 'get all users success',
            data: data
        })
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        })
    }

}

const createNewUsers = async (req, res) => {
    const {body} = req;
    try {
        await usersModel.createNewUsers(body);
        res.json({
            message: 'create new user success',
            data: body
        });
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        })
    }
}

const updateUsers = async (req, res) =>{
    const {idUsers} = req.params;
    const {body} = req;

    try {
        await usersModel.updateUsers(body, idUsers);
        res.json({
            message: 'Update User Berhasil',
            data: {
                id: idUsers,
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

const deleteUsers = async (req, res) =>{
    const {idUsers} = req.params;
    try {
        await usersModel.deleteUsers(idUsers);
        res.json({
            message: 'delete user success',
        })
    } catch (error) {
        res.status(500).json({
            message: 'server error',
            serverMessage: error,
        })
    }
}

module.exports = {
    getAllUsers,
    createNewUsers,
    updateUsers,
    deleteUsers,
}