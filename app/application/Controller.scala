package application

import akka.actor.ActorSystem
import akka.stream.Materializer
import javax.inject.Inject
import play.api.libs.streams.ActorFlow
import play.api.mvc._

class Controller @Inject()(
  cc: ControllerComponents,
  authAction: auth.Action
)(
  implicit system: ActorSystem,
  mat: Materializer
) extends AbstractController(cc) {

  def index: Action[AnyContent] = authAction { request =>
    Ok(s"howdy \uD83E\uDD20, ${request.user.displayName}")
  }

  def socket: WebSocket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      MessagingActor.props(out)
    }
  }

}
