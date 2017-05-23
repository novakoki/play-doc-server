package actors
import akka.actor._

/**
  * Created by szq on 2017/5/23.
  */
class WebSocketActor(out:ActorRef) extends Actor {
  override def receive = {
    case msg:String => {
      out ! "I receive your message!"
    }
  }
}
