const DBConnection = require("../db/dbConfig");
const bcrypt = require("bcrypt");
const { StatusCodes } = require("http-status-codes");
const jwt = require("jsonwebtoken");

// ========== Register (Updated to handle role_id and profession) ==========
const register = async (req, res) => {
  const {
    username,
    email,
    password,
    firstname,
    lastname,
    profession,
    role_id = 2,
  } = req.body;

  if (
    !username ||
    !email ||
    !password ||
    !firstname ||
    !lastname ||
    !profession
  ) {
    return res.status(StatusCodes.BAD_REQUEST).json({
      msg: "All fields are required",
    });
  }

  if (password.length < 8) {
    return res.status(StatusCodes.BAD_REQUEST).json({
      msg: "Password must be at least 8 characters",
    });
  }

  try {
    // Check if user exists
    const [user] = await DBConnection.query(
      "SELECT username, userid FROM users WHERE email = ? OR username = ?",
      [email, username]
    );

    if (user.length > 0) {
      return res.status(StatusCodes.BAD_REQUEST).json({
        msg: "User already exists",
      });
    }

    // Hash password
    const salt = await bcrypt.genSalt(10);
    const hashedPassword = await bcrypt.hash(password, salt);

    // Insert user with role_id and profession
    await DBConnection.query(
      "INSERT INTO users (username, email, password, firstname, lastname, profession, role_id) VALUES (?, ?, ?, ?, ?, ?, ?)",
      [
        username,
        email,
        hashedPassword,
        firstname,
        lastname,
        profession,
        role_id,
      ]
    );

    return res.status(StatusCodes.CREATED).json({
      msg: "User created successfully",
      role_id,
    });
  } catch (error) {
    console.error("Registration error:", error);
    return res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({
      msg: "Registration failed",
      error: error.message,
    });
  }
};

// ========== Login (Updated with role information) ==========
const login = async (req, res) => {
  const { email, password } = req.body;

  if (!email || !password) {
    return res.status(StatusCodes.BAD_REQUEST).json({
      msg: "Email and password are required",
    });
  }

  try {
    // Get user with role information
    const [user] = await DBConnection.query(
      `SELECT u.userid, u.username, u.password, r.role_name, r.role_id, u.firstname 
       FROM users u
       JOIN roles r ON u.role_id = r.role_id
       WHERE u.email = ?`,
      [email]
    );

    if (user.length === 0) {
      return res.status(StatusCodes.UNAUTHORIZED).json({
        msg: "Invalid email or password",
      });
    }

    // Verify password
    const isMatch = await bcrypt.compare(password, user[0].password);
    if (!isMatch) {
      return res.status(StatusCodes.UNAUTHORIZED).json({
        msg: "Invalid email or password",
      });
    }

    // Create token with role information
    const { userid, username, role_id, role_name, firstname } = user[0];
    const token = jwt.sign(
      { userid, username, role_id, role_name },
      process.env.JWT_SECRET,
      { expiresIn: "1d" }
    );

    return res.status(StatusCodes.OK).json({
      msg: "Login successful",
      token,
      user: {
        userid,
        username,
        role_id,
        role: role_name,
        firstname
      },
    });
  } catch (error) {
    console.error("Login error:", error);
    return res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({
      msg: "Login failed",
      error: error.message,
    });
  }
};

// ========== Check User ==========
async function checkUser(req, res) {
  try {
    const { username, userid, role_id, role } = req.user;
    res.status(StatusCodes.OK).json({
      msg: "Valid user",
      username,
      userid,
      role_id,
      role,
    });
  } catch (error) {
    console.error("Check user error:", error);
    res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({
      msg: "Error verifying user",
    });
  }
}

// ========== Get Full Name ==========
async function getFullName(req, res) {
  try {
    const { userid } = req.user;
    const [userData] = await DBConnection.query(
      "SELECT firstname, lastname FROM users WHERE userid = ?",
      [userid]
    );

    if (userData.length === 0) {
      return res.status(StatusCodes.NOT_FOUND).json({
        msg: "User not found",
      });
    }

    const fullName = `${userData[0].firstname} ${userData[0].lastname}`;
    res.status(StatusCodes.OK).json({
      fullname: fullName,
    });
  } catch (error) {
    console.error("Get full name error:", error);
    res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({
      msg: "Error fetching user data",
    });
  }
}

// ========== Get Total Users Only ==========
async function getUserStats(req, res) {
  try {
    const [[{ totalUsers }]] = await DBConnection.query(
      "SELECT COUNT(*) AS totalUsers FROM users"
    );

    res.status(StatusCodes.OK).json({
      totalUsers,
    });
  } catch (error) {
    console.error("Get user count error:", error);
    res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({
      msg: "Error fetching user count",
      error: error.message,
    });
  }
}

// ========== Get All Names and Professions ==========
async function getAllUserNamesAndProfessions(req, res) {
  try {
    const [users] = await DBConnection.query(
      "SELECT userid, firstname, lastname, profession FROM users"
    );

    const result = users.map((user) => ({
      userid: user.userid,
      firstname: user.firstname,
      lastname: user.lastname,
      profession: user.profession,
    }));

    res.status(StatusCodes.OK).json({ users: result });
  } catch (error) {
    console.error("Error fetching user names and professions:", error);
    res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({
      msg: "Error fetching user data",
      error: error.message,
    });
  }
}

// ========== Delete User ==========
async function deleteUser(req, res) {
  try {
    const { userid } = req.params;
    await DBConnection.query("DELETE FROM users WHERE userid = ?", [userid]);
    res.status(StatusCodes.OK).json({ msg: "User deleted successfully" });
  } catch (error) {
    console.error("Delete user error:", error);
    res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({
      msg: "Failed to delete user",
      error: error.message,
    });
  }
}

// ========== Exports ==========
module.exports = {
  login,
  register,
  checkUser,
  getFullName,
  getUserStats,
  getAllUserNamesAndProfessions,
  deleteUser,
};
