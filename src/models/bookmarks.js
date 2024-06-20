const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();

const getAllBookmark = async () => {
    return await prisma.bookmark.findMany();
}

const createNewBookmark = async (data) => {
    const { user_id, food_id } = data;

    return await prisma.$transaction(async (prisma) => {
        const existingBookmark = await prisma.bookmark.findFirst({
            where: {
                user_id: user_id,
                food_id: food_id
            }
        });

        if (existingBookmark) {
            throw new Error('Anda sudah menambahkan bookmark untuk makanan ini sebelumnya.');
        }

        const newBookmark = await prisma.bookmark.create({
            data: {
                user_id: user_id,
                food_id: food_id
            }
        });

        const bookmarkCount = await prisma.bookmark.count({
            where: { food_id: food_id }
        });

        await prisma.food.update({
            where: { id: food_id },
            data: {
                bookmark_counts: bookmarkCount
            }
        });

        return newBookmark;
    });
}

const deleteBookmark = async (userId, foodId) => {
    return await prisma.$transaction(async (prisma) => {
        const deletedBookmark = await prisma.bookmark.deleteMany({
            where: {
                user_id: userId,
                food_id: foodId
            }
        });

        if (deletedBookmark.count > 0) {
            const bookmarkCount = await prisma.bookmark.count({
                where: { food_id: foodId }
            });

            await prisma.food.update({
                where: { id: foodId },
                data: {
                    bookmark_counts: bookmarkCount
                }
            });
        } else {
            throw new Error('Bookmark not found');
        }

        return deletedBookmark;
    });
}

module.exports = {
    getAllBookmark,
    createNewBookmark,
    deleteBookmark
}
