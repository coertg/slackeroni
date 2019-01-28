package auth

import javax.inject.Inject
import play.api.mvc.Results.{Redirect, Unauthorized}
import play.api.mvc._
import user.User

import scala.concurrent.{ExecutionContext, Future}

class Action @Inject()(
  bp: BodyParsers.Default,
  users: user.Service,
  auth: Service
)(
  implicit ec: ExecutionContext
) extends ActionBuilder[UserRequest, AnyContent] with ActionRefiner[Request, UserRequest]{

  override def parser: BodyParser[AnyContent] = bp

  override protected def refine[A](request: Request[A]): Future[Either[Result, UserRequest[A]]] = Future.successful{
    val possibleSessionValue: Option[String] = request.session.get(auth.sessionKey)
    val possibleUnexpiredSession: Option[SessionToken] = possibleSessionValue.flatMap(auth.parseUnexpiredSession)
    val possibleUser: Option[User] = possibleUnexpiredSession.flatMap(s => users.findBySessionKey(s.username))
    val possibleUserRequest: Option[UserRequest[A]] = possibleUser.map(new UserRequest(_, request))

    possibleUserRequest match {
      case Some(ur) => Right(ur)
      case _ => Left(onAuthFail(request))
    }
  }

  override protected def executionContext: ExecutionContext = ec

  private def onAuthFail[A](request: Request[A]) = {
    if (request.path.startsWith("/api"))
      Unauthorized("Unauthorized Request")
    else
      Redirect(routes.Controller.login())
  }
}
