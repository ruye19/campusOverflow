class Answer {
  constructor(answerid, questionid, userid, answer) {
    this.answerid = answerid;
    this.questionid = questionid;
    this.userid = userid;
    this.answer = answer;
  }

  static fromDB(dbAnswer) {
    return new Answer(
      dbAnswer.answerid,
      dbAnswer.questionid,
      dbAnswer.userid,
      dbAnswer.answer
    );
  }
}

module.exports = Answer;
