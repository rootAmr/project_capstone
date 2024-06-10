const pool = require('../config/database');

const getAllRating = () =>{
    const SQLQuery = 'SELECT * FROM ratings'

    return pool.execute(SQLQuery);  
}

const createNewRating = (body) =>{
    const SQLQuery = `  INSERT INTO ratings (user_id, food_id, rate) 
                        VALUES('${body.user_id}','${body.food_id}','${body.rate}')`;
    
    return pool.execute(SQLQuery); 
}

const updateRating = (body, idRating) =>{
    const SQLQuery = `UPDATE ratings
                        SET user_id='${body.user_id}', food_id='${body.food_id}', rate='${body.rate}'
                        WHERE id=${idRating}`;

    return pool.execute(SQLQuery); 
}

const deleteRating = (idRating) =>{
    const SQLQuery = `DELETE FROM ratings
                        WHERE id=${idRating}`

    return pool.execute(SQLQuery);
}

module.exports = {
    getAllRating,
    createNewRating,
    updateRating,
    deleteRating
}