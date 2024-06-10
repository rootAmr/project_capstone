const pool = require('../config/database');

const getAllComment = () =>{
    const SQLQuery = 'SELECT * FROM comments'

    return pool.execute(SQLQuery);  
}

const createNewComment = (body) =>{
    const SQLQuery = `  INSERT INTO comments (user_id, food_id, commentField) 
                        VALUES('${body.user_id}','${body.food_id}','${body.commentField}')`;
    
    return pool.execute(SQLQuery); 
}

const updateComment = (body, idComment) =>{
    const SQLQuery = `UPDATE comments
                        SET user_id='${body.user_id}', food_id='${body.food_id}',commentField='${body.commentField}'
                        WHERE id=${idComment}`;

    return pool.execute(SQLQuery); 
}

const deleteComment = (idComment) =>{
    const SQLQuery = `DELETE FROM comments
                        WHERE id=${idComment}`

    return pool.execute(SQLQuery);
}

module.exports = {
    getAllComment,
    createNewComment,
    updateComment,
    deleteComment
}