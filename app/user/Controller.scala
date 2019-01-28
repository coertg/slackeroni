package user

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc._

class Controller @Inject()(
  cc: ControllerComponents,
  service: Service
) extends AbstractController(cc) {

  def create: Action[Create] = Action(parse.json[Create]) { request =>
    val input: Create = request.body
    service.create(input.username, input.password, input.displayName) match {
      case Right(user) => Created(Json.toJson(user))
      case Left(_) => Conflict
    }
  }

}
