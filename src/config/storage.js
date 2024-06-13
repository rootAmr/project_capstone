const { Storage } = require('@google-cloud/storage');
const path = require('path');

const storage = new Storage({
    keyFilename: path.join(__dirname, 'https://storage.googleapis.com/food-mood-bucket/food-mood-capstone-48c19433eb4f.json'),
});

const bucket = storage.bucket('food-mood-bucket');

module.exports = bucket;
