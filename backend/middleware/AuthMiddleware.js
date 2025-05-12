const { StatusCodes } = require("http-status-codes");
const dotenv = require("dotenv");
dotenv.config();
const jwt = require("jsonwebtoken");

async function autMiddleware(req, res, next) {
  const authHeader = req.headers.authorization;

  // First check if header exists and has correct format
  if (!authHeader || !authHeader.startsWith("Bearer ")) {
    return res
      .status(StatusCodes.UNAUTHORIZED)
      .json({
        msg: "Authentication Invalid: Missing or malformed authorization header",
      });
  }

  // Now safely split the token
  const token = authHeader.split(" ")[1];
  console.log(token, "now");

  try {
    const { username, userid, role_id, role_name } = jwt.verify(token, process.env.JWT_SECRET);
    req.user = { username, userid, role_id, role_name };
    console.log(req.user);
    next();
  } catch (error) {
    console.error("Authentication error:", error);
    return res
      .status(StatusCodes.UNAUTHORIZED) // Changed from INTERNAL_SERVER_ERROR to UNAUTHORIZED
      .json({ msg: "Authentication Invalid: Invalid token" });
  }
}

module.exports = autMiddleware;
