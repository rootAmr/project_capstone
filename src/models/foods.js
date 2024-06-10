const pool = require('../config/database');

const getAllFood = () =>{
    const SQLQuery = 'SELECT * FROM foods'

    return pool.execute(SQLQuery);  
}

const createNewFood = (body) =>{
    const SQLQuery = `  INSERT INTO foods (foodName, ingredients, steps, category, url, image, rating) 
                        VALUES('${body.foodName}','${body.ingredients}','${body.steps}','${body.category}','${body.url}','${body.image}','${body.rating}')`;
    
    return pool.execute(SQLQuery); 
}

const updateFood = (body, idFood) =>{
    const SQLQuery = `UPDATE foods
                        SET foodName='${body.foodName}', ingredients='${body.ingredients}', steps='${body.steps}', category='${body.category}', url='${body.url}',image='${body.image}', rating='${body.rating}'
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