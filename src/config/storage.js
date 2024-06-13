const { Storage } = require('@google-cloud/storage');
const path = require('path');

const storage = new Storage({
    keyFilename: require('../../serviceAccount.json')
});

const bucket = storage.bucket('food-mood-capstone.appspot.com');

module.exports = bucket;
