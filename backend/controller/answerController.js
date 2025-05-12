const dbConnection = require("../db/dbConfig")
const { StatusCodes } = require("http-status-codes")

// Get answers for a specific question
const getAnswer = async (req, res) => {
    try {
        const { questionid } = req.params
        const [answers] = await dbConnection.query(
            `SELECT a.answerid, a.userid, a.questionid, a.answer_text as answer, u.username 
             FROM answers a 
             JOIN users u ON a.userid = u.userid 
             WHERE a.questionid = ?`,
            [questionid]
        )
        
        console.log("Found answers:", answers)
        
        if (!answers || answers.length === 0) {
            console.log("No answers found for question:", questionid)
            return res.status(StatusCodes.OK).json({ 
                message: "No answers found", 
                answers: [] 
            })
        }

        return res.status(StatusCodes.OK).json({ 
            message: "Answers retrieved successfully", 
            answers 
        })
    } catch (error) {
        console.error("Error getting answers:", error)
        return res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({ 
            message: "An unexpected error occurred",
            error: error.message 
        })
    }
}

// Post a new answer
const postAnswer = async (req, res) => {
    const { questionid, answer } = req.body
    const userid = req.user.userid // Get userid from auth middleware
    console.log("Received post answer request:", { questionid, answer, userid })
    
    if (!answer) {
        return res
            .status(StatusCodes.BAD_REQUEST)
            .json({ message: "Please provide answer" })
    }

    try {
        const [result] = await dbConnection.query(
            "INSERT INTO answers (userid, questionid, answer_text) VALUES (?, ?, ?)",
            [userid, questionid, answer]
        )

        console.log("Answer posted successfully:", result)

        return res
            .status(StatusCodes.CREATED)
            .json({ message: "Answer posted successfully" })
    } catch (error) {
        console.error("Error posting answer:", error)
        return res
            .status(StatusCodes.INTERNAL_SERVER_ERROR)
            .json({ 
                message: "An unexpected error occurred",
                error: error.message 
            })
    }
}

// Get answer statistics
const getAnswerStats = async (req, res) => {
    try {
        const [[{ totalAnswers }]] = await dbConnection.query(
            "SELECT COUNT(*) AS totalAnswers FROM answers"
        )

        res.status(StatusCodes.OK).json({
            totalAnswers,
        })
    } catch (error) {
        console.error("Get answer count error:", error)
        res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({
            msg: "Error fetching answer count",
            error: error.message,
        })
    }
}

module.exports = {
    getAnswer,
    postAnswer,
    getAnswerStats
}
