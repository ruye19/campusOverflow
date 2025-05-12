class Question {
  constructor(questionid, title, description, userid, tag) {
    this.questionid = questionid;
    this.title = title;
    this.description = description;
    this.userid = userid;
    this.tag = tag;
  }

  static fromDB(dbQuestion) {
    return new Question(
      dbQuestion.questionid,
      dbQuestion.title,
      dbQuestion.description,
      dbQuestion.userid,
      dbQuestion.tag
    );
  }
}

module.exports = Question;
