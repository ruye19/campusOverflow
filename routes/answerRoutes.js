const express = require("express")
// const { post } = require("./questionRoute");
const router = express.Router()
const {
  postAnswer,
  getAnswer,
  getAnswerStats,
  //   getAnswerCount,
} = require("../controller/answerController");
router.post("/", postAnswer)
router.get("/getAnswerStats", getAnswerStats);
router.get("/:questionid", getAnswer)

module.exports = router
