const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();

const getAllFood = async () => {
  return await prisma.food.findMany();
}

const createNewFood = async (body, userId) => {
  return await prisma.food.create({
    data: {
      user_id: userId,
      foodName: body.foodName,
      ingredients: body.ingredients,
      steps: body.steps,
      category: body.category,
      url: body.url,
      image: body.image,
      rating: body.rating,
    }
  });
}

const updateFood = async (body, idFood, userId) => {
  return await prisma.food.updateMany({
    where: { id: idFood, user_id: userId },
    data: {
      foodName: body.foodName,
      ingredients: body.ingredients,
      steps: body.steps,
      category: body.category,
      url: body.url,
      image: body.image,
      rating: body.rating,
    }
  });
}

const deleteFood = async (idFood, userId) => {
  return await prisma.food.deleteMany({
    where: { id: idFood, user_id: userId }
  });
}

module.exports = {
  getAllFood,
  createNewFood,
  updateFood,
  deleteFood
}
