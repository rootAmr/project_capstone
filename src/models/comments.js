const pool = require('../config/database');

const getAllComment = () =>{
    const SQLQuery = 'SELECT * FROM comments'

    return pool.execute(SQLQuery);  
}

const createNewComment = (body) => {
    const { user_id, food_id, commentField } = body;

    // Check if the user has already commented on the specific food item
    const checkQuery = `SELECT COUNT(*) AS count FROM comments WHERE user_id = ${user_id} AND food_id = ${food_id}`;
    return pool.execute(checkQuery)
        .then(([rows, fields]) => {
            const count = rows[0].count;
            if (count > 0) {
                // If the user has already commented on this food item, return an error message
                return Promise.reject(new Error('Anda sudah memberikan komentar untuk makanan ini sebelumnya.'));
            } else {
                // If the user has not commented on this food item, insert the new comment into the database
                const insertQuery = `INSERT INTO comments (user_id, food_id, commentField) VALUES ('${user_id}', '${food_id}', '${commentField}')`;
                return pool.execute(insertQuery);
            }
        })
        .catch(error => {
            // Handle errors, e.g., return an error message
            return Promise.reject("You have already comment");
        });
};


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