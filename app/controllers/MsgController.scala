package controllers
import actors._
import play.api.mvc._
import javax.inject._
import services._
import akka.actor._
import akka.stream._
import play.api.libs.streams._

/**
  * Created by szq on 2017/5/15.
  */
class MsgController @Inject()
  (implicit system:ActorSystem, materializer: Materializer) extends Controller with RepoService {
  def socket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef(out => {
      for {
        repos <- getAllRepos
      } yield {
        new WatchService(out, system, repos)
      }
      Props(new WebSocketActor(out))
    })
  }
}
