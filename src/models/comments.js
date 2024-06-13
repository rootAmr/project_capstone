const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();

const getAllComment = async () => {
    return await prisma.comment.findMany();
}

const createNewComment = async (data, userId) => {
    const { food_id, commentField } = data;

    // Check if the user has already commented on the specific food item
    const existingComment = await prisma.comment.findFirst({
        where: {
            user_id: userId,
            food_id: food_id
        }
    });

    if (existingComment) {
        throw new Error('Anda sudah memberikan komentar untuk makanan ini sebelumnya.');
    }

    return await prisma.comment.create({
        data: {
            user_id: userId,
            food_id: food_id,
            commentField: commentField
        }
    });
};

const updateComment = async (data, idComment) => {
    const { user_id, food_id, commentField } = data;

    return await prisma.comment.update({
        where: { id: parseInt(idComment) },
        data: {
            user_id: user_id,
            food_id: food_id,
            commentField: commentField
        }
    });
};

const deleteComment = async (idComment) => {
    return await prisma.comment.delete({
        where: { id: parseInt(idComment) }
    });
};

module.exports = {
    getAllComment,
    createNewComment,
    updateComment,
    deleteComment
};
