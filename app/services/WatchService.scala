package services

import akka.actor.ActorSystem
import com.beachape.filemanagement.MonitorActor
import com.beachape.filemanagement.RegistryTypes._
import com.beachape.filemanagement.Messages._
import play.api.Logger
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds._

import com.google.inject.Singleton
/**
  * Created by szq on 2017/5/2.
  */
@Singleton
class WatchService {
  implicit val system = ActorSystem("actorSystem")
  val fileMonitorActor = system.actorOf(MonitorActor(concurrency = 2))
  val modifyCallbackDirectory: Callback = { path =>
    Logger.info(s"Something was modified in path: $path")}

  val desktop = Paths get "C:/Users/szq/Desktop"

  fileMonitorActor ! RegisterCallback(
    event = ENTRY_MODIFY,
    path = desktop,
    callback = modifyCallbackDirectory)
}
