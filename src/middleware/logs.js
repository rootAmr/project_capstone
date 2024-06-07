const logRequest = (req, res, next)=>{
    console.log('Terjadi request ke', req.path);
    next();
}

module.exports = logRequest;