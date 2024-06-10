require('dotenv').config()
const PORT = process.env.PORT || 5000;
const express = require('express');
const usersRoutes = require('./routes/users');
const foodsRoutes = require('./routes/foods');
const commentsRoutes = require('./routes/comment');
const middlewareLogRequest = require('./middleware/logs');


const app = express();

app.use(middlewareLogRequest);

app.use(express.json());

app.use('/users', usersRoutes);

app.use('/foods', foodsRoutes);

app.use('/comments', commentsRoutes);

app.listen(PORT, () => {
    console.log(`SERVER BERJALAN DI PORT ${PORT}`);
});
