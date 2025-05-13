const dbConnection = require("../db/dbConfig")
const { v4: uuidv4 } = require("uuid")
const { StatusCodes } = require("http-status-codes")
const jwt = require("jsonwebtoken")

async function createQuestion(req, res) {
  const questionid = uuidv4() // Generate a unique question ID using uuid
  const { title, description, tag } = req.body
  const { userid } = req.user
  // Validate required fields
  if (!title || !description) {
    return res.status(StatusCodes.BAD_REQUEST).json({
      message: "Please provide all required fields (title and description)",
    })
  }
  try {
    // Insert question into the database
    await dbConnection.query(
      "INSERT INTO questions (title, description, questionid, userid,tag) VALUES (?, ?, ?, ?,?)",
      [title, description, questionid, userid, tag || null]
    )

    return res
      .status(StatusCodes.CREATED)
      .json({ message: "Question Posted successfully", questionid })
  } catch (error) {
    console.error("Error creating question:", error.message)
    return res
      .status(StatusCodes.INTERNAL_SERVER_ERROR)
      .json({ message: "An unexpected error occurred", error: error.message })
  }
}

async function singleQuestion(req, res) {
  // res.send(`specific question for id=${req.params.question_id}`)

  const [singleQuestion] = await dbConnection.query(
    `SELECT q.id,q.questionid,q.userid ,q.title,q.description,q.tag,u.username FROM questions q join users u on q.userid = u.userid WHERE q.questionid = '${req.params.question_id}' order by q.id desc `
  )
  console.log(singleQuestion)
  return res.status(StatusCodes.OK).json({
    msg: "Question's retrieved successfully ",
    singleQuestion,
  })
}
async function getAllQuestion(req, res) {
  const [allQuestion] = await dbConnection.query(
    "SELECT q.id,q.questionid,q.userid ,q.title,q.description,q.tag,u.username FROM questions q join users u on q.userid = u.userid order by q.id desc "
  )
  console.log(allQuestion)
  return res.status(StatusCodes.OK).json({
    msg: "Question's retrieved successfully ",
    allQuestion,
  })
}
async function getSeachedQuestion(req, res) {
  console.log("am in get searched question function ", req.params.search)
  const [allQuestion] = await dbConnection.query(
    `SELECT q.id,q.questionid,q.userid ,q.title,q.description,q.tag,u.username FROM questions q join users u on q.userid = u.userid WHERE title like '%${req.params.search}%' or description like '%${req.params.search}%' order by q.id desc `
  )
  console.log(allQuestion)
  return res.status(StatusCodes.OK).json({
    msg: "Question/s retrieved successfully ",
    allQuestion,
  })
}

async function countQuestions(req, res) {
  try {
    const [[{ totalQuestions }]] = await dbConnection.query(
      "SELECT COUNT(*) AS totalQuestions FROM questions"
    );

    return res.status(StatusCodes.OK).json({
      message: "Total questions count retrieved successfully",
      totalQuestions,
    });
  } catch (error) {
    console.error("Error counting questions:", error.message);
    return res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({
      message: "An unexpected error occurred while counting questions",
      error: error.message,
    });
  }
}

async function deleteQuestion(req, res) {
  const { questionid } = req.params
  const { userid, role_id } = req.user
  console.log(role_id, "role id")

  // Check if user is admin (role_id === 1)
  if (role_id !== 1) {
    return res.status(StatusCodes.FORBIDDEN).json({
      message: "Access denied. Only admins can delete questions."
    })
  }

  try {
    // First check if question exists
    const [question] = await dbConnection.query(
      "SELECT * FROM questions WHERE questionid = ?",
      [questionid]
    )

    if (question.length === 0) {
      return res.status(StatusCodes.NOT_FOUND).json({
        message: "Question not found"
      })
    }

    // Delete the question
    await dbConnection.query(
      "DELETE FROM questions WHERE questionid = ?",
      [questionid]
    )

    return res.status(StatusCodes.OK).json({
      message: "Question deleted successfully"
    })
  } catch (error) {
    console.error("Error deleting question:", error.message)
    return res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({
      message: "An unexpected error occurred",
      error: error.message
    })
  }
}

module.exports = {
  createQuestion,
  getAllQuestion,
  singleQuestion,
  getSeachedQuestion,
  countQuestions,
  deleteQuestion
};
