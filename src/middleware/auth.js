const { auth } = require("../config/firebase");
const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();

const authenticateToken = async (req, res, next) => {
  try {
    const bearerHeader = req.headers["authorization"];
    if (typeof bearerHeader !== "undefined") {
      const token = bearerHeader.split(" ")[1];
      if (!token) return res.status(401).json({ message: "Token required" });

      // Verify ID token
      const decodedToken = await auth.verifyIdToken(token);
      req.user = decodedToken;

      // Check or create user in the database
      await checkOrCreateUser(decodedToken);

      next(); // Proceed to the next middleware
    } else {
      res.status(401).json({ message: "Unauthorized" });
    }
  } catch (error) {
    console.error("Error authenticating token:", error);
    res.status(403).json({ message: "Forbidden" });
  }
};

const checkOrCreateUser = async (decodedToken) => {
  const userId = decodedToken.uid; // Updated to use Firebase uid
  const user = await prisma.user.findUnique({
    where: { user_id: userId }
  });

  if (!user) {
    await prisma.user.create({
      data: { user_id: userId }
    });
  }
};

module.exports = {
  authenticateToken
};
