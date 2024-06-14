require('dotenv').config()
const PORT = process.env.PORT || 5000;
const express = require('express');
// const usersRoutes = require('./routes/users');
const foodsRoutes = require('./routes/foods');
const commentsRoutes = require('./routes/comment');
const ratingsRoutes = require('./routes/ratings');
const bookmarksRoutes = require('./routes/bookmarks');
const middlewareLogRequest = require('./middleware/logs');
const { authenticateToken } = require('./middleware/auth');



const app = express();

app.use(middlewareLogRequest);

app.use(express.json());

// app.use('/users', usersRoutes);

app.use('/foods', authenticateToken, foodsRoutes);

app.use('/comments', authenticateToken, commentsRoutes);

app.use('/ratings', authenticateToken, ratingsRoutes);

app.use('/bookmarks', authenticateToken, bookmarksRoutes);

app.listen(PORT, () => {
    console.log(`SERVER BERJALAN DI PORT ${PORT}`);
});
