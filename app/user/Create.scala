package user

import play.api.libs.json.{Json, OFormat}

case class Create (username: String, password: String, displayName: String)

object Create {
  implicit val format: OFormat[Create] = Json.format[Create]
}
