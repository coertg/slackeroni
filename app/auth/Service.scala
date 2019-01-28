package auth

import javax.inject.Inject
import play.api.Configuration
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

class Service @Inject()(conf: Configuration){
  val sessionKey: String = conf.getOptional[String]("auth.sessionKey").getOrElse("USER")
  private val maxSessionAge: Duration = conf.getOptional[Duration]("auth.maxSessionAge").getOrElse(5 minutes)

  def parseUnexpiredSession(s: String): Option[SessionToken] = {
    SessionToken.fromStringToken(s).filter(_.hasNotExpired)
  }

  def getNewSession(username: String): (String, String) = {
    sessionKey -> SessionToken.getNewStringToken(username, maxSessionAge)
  }
}
