const bcrypt = require('bcrypt');
const usersModel = require('../models/users');

const getAllUsers = async (req, res) => {
    try {
        const [data] = await usersModel.getAllUsers();
        res.json({
            message: 'Get all users success',
            data: data
        });
    } catch (error) {
        res.status(500).json({
            message: 'Server error',
            serverMessage: error,
        });
    }
}

const createNewUsers = async (req, res) => {
    const { body } = req;
    try {
        await usersModel.createNewUsers(body);
        res.status(201).json({
            message: 'Create new user success',
            data: body
        });
    } catch (error) {
        res.status(500).json({
            message: 'Server error',
            serverMessage: error,
        });
    }
}

const updateUsers = async (req, res) => {
    const { idUsers } = req.params;
    const { body } = req;
    try {
        await usersModel.updateUsers(body, idUsers);
        res.status(201).json({
            message: 'Update user success',
            data: {
                id: idUsers,
                ...body
            },
        });
    } catch (error) {
        res.status(500).json({
            message: 'Server error',
            serverMessage: error,
        });
    }
}

const deleteUsers = async (req, res) => {
    const { idUsers } = req.params;
    try {
        await usersModel.deleteUsers(idUsers);
        res.json({
            message: 'Delete user success',
        });
    } catch (error) {
        res.status(500).json({
            message: 'Server error',
            serverMessage: error,
        });
    }
}

const registerUser = async (req, res) => {
    const { body } = req;
    
    if (!body.profile) {
        body.profile = '';
    }

    const hashedPassword = await bcrypt.hash(body.password, 10);
    body.password = hashedPassword;

    try {
        await usersModel.createNewUsers(body);
        res.status(201).json({
            message: 'User registered successfully',
            data: body
        });
    } catch (error) {
        res.status(500).json({
            message: 'Server error',
            serverMessage: error,
        });
    }
}


const loginUser = async (req, res) => {
    const { email, password } = req.body;
    try {
        const [user] = await usersModel.getUserByEmail(email);

        if (user.length === 0) {
            return res.status(400).json({
                message: 'User not found'
            });
        }

        const isPasswordValid = await bcrypt.compare(password, user[0].password);

        if (!isPasswordValid) {
            return res.status(400).json({
                message: 'Invalid password'
            });
        }

        res.json({
            message: 'Login success',
            data: user[0]
        });
    } catch (error) {
        res.status(500).json({
            message: 'Server error',
            serverMessage: error,
        });
    }
}

module.exports = {
    getAllUsers,
    createNewUsers,
    updateUsers,
    deleteUsers,
    registerUser,
    loginUser
}
