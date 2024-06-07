const pool = require('../config/database');

const getAllFood = () =>{
    const SQLQuery = 'SELECT * FROM foods'

    return pool.execute(SQLQuery);  
}

const createNewFood = (body) =>{
    const SQLQuery = `  INSERT INTO foods (foodName, recipe, image) 
                        VALUES('${body.foodName}','${body.recipe}','${body.image}')`;
    
    return pool.execute(SQLQuery); 
}

const updateFood = (body, idFood) =>{
    const SQLQuery = `UPDATE foods
                        SET foodName='${body.foodName}', recipe='${body.recipe}',image='${body.image}'
                        WHERE id=${idFood}`;

    return pool.execute(SQLQuery); 
}

const deleteFood = (idFood) =>{
    const SQLQuery = `DELETE FROM foods
                        WHERE id=${idFood}`

    return pool.execute(SQLQuery);
}

module.exports = {
    getAllFood,
    createNewFood,
    updateFood,
    deleteFood
}