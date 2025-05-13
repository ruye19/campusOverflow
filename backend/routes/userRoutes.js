const express = require("express")
const router = express.Router()

// usercontroller
const authMiddleWare = require("../middleware/AuthMiddleware")
const {
  register,
  login,
  checkUser,
  getFullName,
  getUserStats,
  getAllUserNamesAndProfessions,
  deleteUser,
} = require("../controller/userController");

router.post("/register", register)
router.post("/login", login)
router.get("/getAllUserNamesAndProfessions", getAllUserNamesAndProfessions);
router.get("/check", authMiddleWare,checkUser)
router.get("/getFullName", authMiddleWare, getFullName)
router.get("/getUserStats", getUserStats);
router.delete("/:userid", deleteUser);

module.exports = router
