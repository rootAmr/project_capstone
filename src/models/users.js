const pool = require('../config/database');

const getAllUsers = () => {
    const SQLQuery = 'SELECT * FROM users';
    return pool.execute(SQLQuery);
}

const createNewUsers = (body) => {
    const SQLQuery = `INSERT INTO users (name, password, email, profile) 
                      VALUES('${body.name}', '${body.password}', '${body.email}', '${body.profile}')`;
    return pool.execute(SQLQuery);
}

const updateUsers = (body, idUser) => {
    const SQLQuery = `UPDATE users
                      SET name='${body.name}', password='${body.password}', email='${body.email}', profile='${body.profile}'
                      WHERE id=${idUser}`;
    return pool.execute(SQLQuery);
}

const deleteUsers = (idUser) => {
    const SQLQuery = `DELETE FROM users WHERE id=${idUser}`;
    return pool.execute(SQLQuery);
}

const getUserByEmail = (email) => {
    const SQLQuery = `SELECT * FROM users WHERE email='${email}'`;
    return pool.execute(SQLQuery);
}

module.exports = {
    getAllUsers,
    createNewUsers,
    updateUsers,
    deleteUsers,
    getUserByEmail
}
