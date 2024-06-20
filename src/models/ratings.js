const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();

const getAllRating = async () => {
    return await prisma.rating.findMany();
}

const createNewRating = async (data, userId) => {
    const { food_id, rate } = data;

    // Check if the user has already rated the specific food item
    const existingRating = await prisma.rating.findFirst({
        where: {
            user_id: userId,
            food_id: food_id
        }
    });

    if (existingRating) {
        throw new Error('Anda sudah memberikan rating untuk makanan ini sebelumnya.');
    }

    const newRating = await prisma.rating.create({
        data: {
            user_id: userId,
            food_id: food_id,
            rate: rate
        }
    });

    // Update the average rating
    await updateAverageRating(food_id);

    return newRating;
};

const updateRating = async (data, idRating) => {
    const { user_id, food_id, rate } = data;

    const updatedRating = await prisma.rating.update({
        where: { id: parseInt(idRating) },
        data: {
            user_id: user_id,
            food_id: food_id,
            rate: rate
        }
    });

    // Update the average rating
    await updateAverageRating(food_id);

    return updatedRating;
};

const deleteRating = async (idRating) => {
    const rating = await prisma.rating.delete({
        where: { id: parseInt(idRating) }
    });

    // Update the average rating
    await updateAverageRating(rating.food_id);

    return rating;
};

const updateAverageRating = async (food_id) => {
    const ratings = await prisma.rating.findMany({
        where: { food_id: food_id },
        select: { rate: true }
    });

    const averageRating = ratings.reduce((sum, rating) => sum + rating.rate, 0) / ratings.length;

    await prisma.food.update({
        where: { id: food_id },
        data: { rating: averageRating }
    });
};

module.exports = {
    getAllRating,
    createNewRating,
    updateRating,
    deleteRating
};
