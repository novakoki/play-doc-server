package actors
import akka.actor._
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds._
import com.beachape.filemanagement.Messages._
import com.beachape.filemanagement.MonitorActor
import com.beachape.filemanagement.RegistryTypes._
import parser.AppParser
import models.Repository

/**
  * Created by szq on 2017/5/23.
  */
class WatchActor (out:ActorRef, system:ActorSystem, repos:List[Repository]) extends Actor with AppParser {
  implicit val fileMonitorActor = system.actorOf(MonitorActor(concurrency = repos.size))

  override def receive = {
    case repo:Repository =>
      watch(repo.rootPath)
  }

  def watch(path:String) = {
    val repoPath = Paths get path

    val modifyCallback:Callback = { path =>
      println(path)
    }

    fileMonitorActor ! RegisterCallback (
      event = ENTRY_MODIFY,
      path = repoPath,
      callback =  modifyCallback
    )
  }
}
