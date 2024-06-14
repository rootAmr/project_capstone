const { Storage } = require('@google-cloud/storage');
const path = require('path');

const storage = new Storage({
  projectId: 'food-mood-capstone',
  keyFilename: path.join(__dirname, '../../serviceAccount.json')
});

const bucketName = 'food-mood-capstone.appspot.com';
const bucket = storage.bucket(bucketName);

const uploadFileToGCS = (file) => {
  return new Promise((resolve, reject) => {
    const { originalname, buffer } = file;
    const blob = bucket.file(Date.now() + path.extname(originalname));
    const blobStream = blob.createWriteStream({
      resumable: false,
      gzip: true
    });

    blobStream.on('finish', () => {
      const publicUrl = `https://storage.googleapis.com/${bucket.name}/${blob.name}`;
      resolve(publicUrl);
    }).on('error', (error) => {
      reject(error);
    }).end(buffer);
  });
};

module.exports = uploadFileToGCS;
