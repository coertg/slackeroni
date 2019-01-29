package user

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc._

class Controller @Inject()(
  cc: ControllerComponents,
  service: Service,
  authAction: auth.Action
) extends AbstractController(cc) {

  def getCurrent = authAction { request =>
    Ok(Json.toJson(request.user))
  }

  def create: Action[Create] = Action(parse.json[Create]) { request =>
    val input: Create = request.body
    service.create(input.username, input.password, input.displayName) match {
      case Right(user) => Created(Json.toJson(user))
      case Left(_) => Conflict
    }
  }

}
