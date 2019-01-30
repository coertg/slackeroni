package application

import akka.actor._

object MessagingActor {
  def props(out: ActorRef) = Props(new MessagingActor(out))
}

class MessagingActor(out: ActorRef) extends Actor {
  def receive = {
    case "marco!" => out ! "polo!"
    case msg: String =>
      out ! ("I received your message: " + msg)
  }
}