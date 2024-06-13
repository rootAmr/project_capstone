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

    return await prisma.rating.create({
        data: {
            user_id: userId,
            food_id: food_id,
            rate: rate
        }
    });
};

const updateRating = async (data, idRating) => {
    const { user_id, food_id, rate } = data;

    return await prisma.rating.update({
        where: { id: parseInt(idRating) },
        data: {
            user_id: user_id,
            food_id: food_id,
            rate: rate
        }
    });
};

const deleteRating = async (idRating) => {
    return await prisma.rating.delete({
        where: { id: parseInt(idRating) }
    });
};

module.exports = {
    getAllRating,
    createNewRating,
    updateRating,
    deleteRating
};
