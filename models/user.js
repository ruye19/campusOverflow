class User {
  constructor(
    userid,
    username,
    email,
    firstname,
    lastname,
    profession,
    role_id
  ) {
    this.userid = userid;
    this.username = username;
    this.email = email;
    this.firstname = firstname;
    this.lastname = lastname;
    this.profession = profession;
    this.role_id = role_id;
  }

  static fromDB(dbUser) {
    return new User(
      dbUser.userid,
      dbUser.username,
      dbUser.email,
      dbUser.firstname,
      dbUser.lastname,
      dbUser.profession,
      dbUser.role_id
    );
  }
}

module.exports = User;
