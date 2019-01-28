package auth

import java.time.Instant
import play.api.libs.json.{JsSuccess, Json, OFormat}
import scala.concurrent.duration.Duration
import scala.util.Try

private[auth] case class SessionToken(username: String, validUntil: Instant){
  def hasNotExpired: Boolean = Instant.now().isBefore(validUntil)
}

private[auth] object SessionToken {
  implicit val format: OFormat[SessionToken] = Json.format[SessionToken]

  def apply(username: String, maxSessionAge: Duration): SessionToken = {
    val validUntil = Instant.now().plusMillis(maxSessionAge.toMillis)
    SessionToken(username, validUntil)
  }

  def getNewStringToken(username: String, maxSessionAge: Duration): String = {
    Json.toJson(SessionToken(username, maxSessionAge)).toString()
  }

  def fromStringToken(s: String): Option[SessionToken] = {
    Try(Json.parse(s)).toOption.flatMap { js =>
      Json.fromJson[SessionToken](js) match {
        case j: JsSuccess[SessionToken] => Option(j.get)
        case _ => None
      }
    }
  }

}
