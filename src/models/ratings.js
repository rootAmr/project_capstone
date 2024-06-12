const pool = require('../config/database');

const getAllRating = () =>{
    const SQLQuery = 'SELECT * FROM ratings'

    return pool.execute(SQLQuery);  
}

const createNewRating = (body) => {
    const { user_id, food_id, rate } = body;

    // Periksa apakah pengguna sudah memberikan rating sebelumnya untuk makanan tertentu
    const checkQuery = `SELECT COUNT(*) AS count FROM ratings WHERE user_id = ${user_id} AND food_id = ${food_id}`;
    return pool.execute(checkQuery)
        .then(([rows, fields]) => {
            const count = rows[0].count;
            if (count > 0) {
                // Jika pengguna sudah memberikan rating sebelumnya, kembalikan pesan kesalahan
                return Promise.reject(new Error('Anda sudah memberikan rating untuk makanan ini sebelumnya.'));
            } else {
                // Jika pengguna belum memberikan rating sebelumnya, masukkan rating ke dalam database
                const insertQuery = `INSERT INTO ratings (user_id, food_id, rate) VALUES ('${user_id}', '${food_id}', '${rate}')`;
                return pool.execute(insertQuery);
            }
        })
        .catch(error => {
            // Tangani kesalahan, misalnya kembalikan pesan kesalahan
            return Promise.reject("You have already rate it");
        });
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