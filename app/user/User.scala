package user

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class User(
  id: Long,
  username: String,
  passhash: String,
  displayName: String
)

object User {

  implicit val writes: OWrites[User] = (
    (JsPath \ "id").write[Long] and
    (JsPath \ "username").write[String] and
    (JsPath \ "displayName").write[String]
  )((u: User) => (u.id, u.username, u.displayName))

  implicit val reads: Reads[User] = Json.reads[User]
}