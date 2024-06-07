const pool = require('../config/database');

const getAllUsers = () =>{
    const SQLQuery = 'SELECT * FROM users'

    return pool.execute(SQLQuery);  
}

const createNewUsers = (body) =>{
    const SQLQuery = `  INSERT INTO users (name, email) 
                        VALUES('${body.name}','${body.email}')`;
    
    return pool.execute(SQLQuery); 
}

const updateUsers = (body, idUser) =>{
    const SQLQuery = `UPDATE users
                        SET name='${body.name}', email='${body.email}'
                        WHERE id=${idUser}`;

    return pool.execute(SQLQuery); 
}

const deleteUsers = (idUser) =>{
    const SQLQuery = `DELETE FROM users
                        WHERE id=${idUser}`

    return pool.execute(SQLQuery);
}

module.exports = {
    getAllUsers,
    createNewUsers,
    updateUsers,
    deleteUsers
}