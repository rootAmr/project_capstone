const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();

const getAllBookmark = async () => {
    return await prisma.bookmark.findMany();
}

const createNewBookmark = async (data) => {
    const { user_id, food_id } = data;

    return await prisma.bookmark.create({
        data: {
            user_id: user_id,
            food_id: food_id
        }
    });
}

const updateBookmark = async (data, idBookmark) => {
    const { user_id, food_id } = data;

    return await prisma.bookmark.update({
        where: { id: parseInt(idBookmark) },
        data: {
            user_id: user_id,
            food_id: food_id
        }
    });
}

const deleteBookmark = async (idBookmark) => {
    return await prisma.bookmark.delete({
        where: { id: parseInt(idBookmark) }
    });
}

module.exports = {
    getAllBookmark,
    createNewBookmark,
    updateBookmark,
    deleteBookmark
}
