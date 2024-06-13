const multer = require('multer');
const path = require('path');

// Konfigurasi penyimpanan untuk multer
const storage = multer.diskStorage({
    destination: function (req, file, cb) {
        // Tentukan folder penyimpanan untuk file yang diunggah
        cb(null, path.join(__dirname, '../uploads/')); // Ganti dengan path yang sesuai
    },
    filename: function (req, file, cb) {
        // Generate nama file yang unik
        cb(null, `${Date.now()}_${file.originalname}`);
    }
});

// Filter untuk mengizinkan hanya file-file gambar
const fileFilter = (req, file, cb) => {
    if (file.mimetype.startsWith('image/')) {
        cb(null, true);
    } else {
        cb(new Error('File bukan gambar!'), false);
    }
};

// Buat middleware multer
const upload = multer({
    storage: storage,
    fileFilter: fileFilter,
    limits: {
        fileSize: 1024 * 1024 * 5 // Batasan ukuran file (contoh: 5 MB)
    }
});

module.exports = upload;
