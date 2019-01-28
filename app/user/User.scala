package user

import play.api.libs.json.{Json, OFormat}

case class User(
  id: Long,
  username: String,
  passhash: String,
  displayName: String
)

object User {
  implicit val format: OFormat[User] = Json.format[User]
}