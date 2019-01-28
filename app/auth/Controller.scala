package auth

import javax.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action => PlayAction, _}
import user.User

import scala.concurrent.ExecutionContext

class Controller @Inject()(
  cc: ControllerComponents,
  users: user.Service,
  auth: Service,
  playAction: DefaultActionBuilder
)(
  implicit ec: ExecutionContext
) extends AbstractController(cc) {

  private val loginForm = Form(
    mapping(
      "username" -> email,
      "password" -> nonEmptyText
    )(users.authenticate)(_.map(u => (u.username, "")))
      .verifying("Invalid username or password", result => result.isDefined)
  )

  def login: PlayAction[AnyContent] = playAction { implicit request =>
    Ok(html.view(loginForm))
  }

  def logout: PlayAction[AnyContent] = playAction { implicit request =>
    Ok(html.view(loginForm)).removingFromSession(auth.sessionKey)
  }

  private def onAuthFailure(formWithErrors: Form[Option[User]] = loginForm)(implicit f: Flash): Result = {
    BadRequest(html.view(formWithErrors))
  }

  private def onAuthSuccess(possibleAccount: Option[User])(implicit f: Flash): Result = possibleAccount match {
    case Some(account: User) =>
      Redirect(application.routes.Controller.index())
        .withSession(auth.getNewSession(account.username))
    case None => onAuthFailure()
  }

  def authenticate: PlayAction[AnyContent] = playAction { implicit request =>
    loginForm.bindFromRequest.fold(
      onAuthFailure,
      onAuthSuccess
    )
  }
}