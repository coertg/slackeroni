package application

import javax.inject.Inject
import play.api.mvc._

class Controller @Inject()(
  cc: ControllerComponents,
  authAction: auth.Action
) extends AbstractController(cc) {

  def index = authAction {
    Ok(html.view())
  }

  // TODO: add websockets

}
