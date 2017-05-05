package services

import akka.actor.ActorSystem
import com.beachape.filemanagement.MonitorActor
import com.beachape.filemanagement.RegistryTypes._
import com.beachape.filemanagement.Messages._
import play.api.Logger
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds._
import parser.AppParser
import com.google.inject.Singleton

/**
  * Created by szq on 2017/5/2.
  */
@Singleton
class WatchService extends AppParser with ApiService {
  implicit val system = ActorSystem("actorSystem")
  val fileMonitorActor = system.actorOf(MonitorActor(concurrency = 2))
  val modifyCallbackDirectory: Callback = { path =>
    Logger.info(path.toString)
    for {
      apis <- parseApis
      q <- batchUpdateApis(apis)
    } {
      println(apis, q.toString)
    }
  }

  val desktop = Paths get "app"

  fileMonitorActor ! RegisterCallback(
    event = ENTRY_MODIFY,
    path = desktop,
    callback = modifyCallbackDirectory)
}
