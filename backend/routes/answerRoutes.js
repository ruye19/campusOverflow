const express = require("express")
// const { post } = require("./questionRoute");
const router = express.Router()
const {
  postAnswer,
  getAnswer,
  getAnswerStats,
  //   getAnswerCount,
} = require("../controller/answerController");

// Get answers for a specific question
router.get("/:questionid", getAnswer)

// Post a new answer
router.post("/", postAnswer)

// Get answer statistics
router.get("/stats", getAnswerStats)

module.exports = router
