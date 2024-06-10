const pool = require('../config/database');

const getAllBookmark = () =>{
    const SQLQuery = 'SELECT * FROM bookmarks'

    return pool.execute(SQLQuery);  
}

const createNewBookmark = (body) =>{
    const SQLQuery = `  INSERT INTO bookmarks (user_id, food_id) 
                        VALUES('${body.user_id}','${body.food_id}')`;
    
    return pool.execute(SQLQuery); 
}

const updateBookmark = (body, idBookmark) =>{
    const SQLQuery = `UPDATE bookmarks
                        SET user_id='${body.user_id}', food_id='${body.food_id}'
                        WHERE id=${idBookmark}`;

    return pool.execute(SQLQuery); 
}

const deleteBookmark = (idBookmark) =>{
    const SQLQuery = `DELETE FROM bookmarks
                        WHERE id=${idBookmark}`

    return pool.execute(SQLQuery);
}

module.exports = {
    getAllBookmark,
    createNewBookmark,
    updateBookmark,
    deleteBookmark
}