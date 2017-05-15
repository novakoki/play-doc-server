package controllers
import play.api.mvc._
import javax.inject._
import akka.actor._
import akka.stream._
import play.api.libs.streams._
import services._
import parser.AppParser
import com.beachape.filemanagement.MonitorActor
import com.beachape.filemanagement.RegistryTypes._
import com.beachape.filemanagement.Messages._
import java.nio.file.StandardWatchEventKinds._
import java.nio.file.Paths

/**
  * Created by szq on 2017/5/15.
  */
class MsgController @Inject() (implicit system:ActorSystem, materializer: Materializer) extends Controller {
  implicit val fileMonitorActor = system.actorOf(MonitorActor(concurrency = 2))

  def socket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef(out => {
      println(out)
      WatchActor.props(out)
      MyWebSocketActor.props(out)
    })
  }
}

object MyWebSocketActor {
  def props(out: ActorRef) = Props(new MyWebSocketActor(out))
}

class MyWebSocketActor(out: ActorRef) extends Actor {
  def receive = {
    case msg: String =>
      out ! ("I received your message: " + msg)
  }

}

object WatchActor {
  def props(out: ActorRef) = Props(new WatchActor(out))
}

class WatchActor(out:ActorRef) extends Actor {
  def receive = {
    case msg:String =>
      out ! "asdad"
  }
}
