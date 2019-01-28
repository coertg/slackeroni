package user

import javax.inject.Inject
import play.api.mvc._

class Controller @Inject()(
  cc: ControllerComponents
) extends AbstractController(cc) {

  def create: Action[AnyContent] = Action {
    // TODO: User creation
    NotImplemented
  }

}
