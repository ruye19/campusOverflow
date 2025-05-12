const express = require("express")
const cors = require("cors")
const userRoutes = require("./routes/userRoutes")
const questionRoutes = require("./routes/questionRoutes")
const answerRoutes = require("./routes/answerRoutes")
const app = express()

// Enable CORS for all routes
app.use(cors())

app.use(express.json())

// Routes
app.use("/api/users", userRoutes)
app.use("/api/questions", questionRoutes)
app.use("/api/answers", answerRoutes)

const PORT = process.env.PORT || 5500

// Listen on all network interfaces
app.listen(PORT, "0.0.0.0", () => {
  console.log(`Server is running on port ${PORT}`)
}) 