const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();

const getAllFood = async () => {
  return await prisma.food.findMany();
}

const getFoodById = async (idFood) => {
  return await prisma.food.findUnique({
    where: { id: idFood },
  });
}

const searchFoods = async (query) => {
  try {
    const lowerQuery = query.toLowerCase();
    return await prisma.food.findMany({
      where: {
        OR: [
          {
            foodName: {
              contains: lowerQuery,
            },
          },
          {
            ingredients: {
              contains: lowerQuery,
            },
          },
          {
            steps: {
              contains: lowerQuery,
            },
          },
          {
            category: {
              contains: lowerQuery,
            },
          }
        ],
      },
      take: 10,
    });
  } catch (error) {
    console.error('Error in searchFoods:', error);
    throw error;
  }
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
      bookmark_counts: body.bookmark_counts,  
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
      bookmark_counts: body.bookmark_counts,
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
  getFoodById,
  searchFoods,
  createNewFood,
  updateFood,
  deleteFood
};
