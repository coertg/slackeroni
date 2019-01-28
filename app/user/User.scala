package user

case class User(
  id: Long,
  username: String,
  passhash: String,
  displayName: String
)
