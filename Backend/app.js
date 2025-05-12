//require("dotenv").config();
const express = require("express")
const app = express()
const cors = require("cors")

// Configure CORS to allow all origins
app.use(cors({
  origin: '*',
  methods: ['GET', 'POST', 'PUT', 'DELETE'],
  allowedHeaders: ['Content-Type', 'Authorization']
}))

const usersRoutes = require("./routes/userRoutes")
const questionRoutes = require("./routes/questionRoute")
const answerRoutes = require("./routes/answerRoutes")
const authMiddleWare = require("./middleware/AuthMiddleware")
PORT = 5500
const dbcon = require("./db/dbConfig")
//login route

app.use(express.json())

app.get("/", (req, res) => {
  res.send("done!")
})

app.use("/api/users", usersRoutes)

//question router
app.use("/api/question", authMiddleWare, questionRoutes)

//answer router
app.use("/api/answer", authMiddleWare, answerRoutes)

async function start() {
  try {
    console.log("start database connection")
    const result = await dbcon.execute("select 'test'")
    console.log(result)
  } catch (error) {
    console.log(error.message)
  }
}

start()

app.listen(PORT, (err) => {
  if (err) {
    console.log(err.message)
  } else {
    console.log("Litsenning on http://localhost:5500")
  }
})



// deployed in https://forum-of-evangadi-bootcamp-8a0129ciq-ruths-projects-0d21d5e6.vercel.app/landing
// deployedd of backend in render.com 