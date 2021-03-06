package services

import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds._

import akka.actor._
import com.beachape.filemanagement.Messages._
import com.beachape.filemanagement.MonitorActor
import com.beachape.filemanagement.RegistryTypes._
import parser.AppParser
import models.Repository

/**
  * Created by szq on 2017/5/2.
  */

class WatchService (out:ActorRef, system:ActorSystem, repos:List[Repository]) extends AppParser with ApiService {
  implicit val fileMonitorActor = system.actorOf(MonitorActor(concurrency = repos.size))
  repos.foreach(watch)

  def watch(repo:Repository) = {
    val repoPath = Paths get repo.rootPath

    val modifyCallback:Callback = { path =>
      for {
        apis <- parseApis(repo.id.get, repo.rootPath)
        r <- batchUpdateApis(repo.id.get, apis)
      } {
        out ! apis.toString
      }
    }

    fileMonitorActor ! RegisterCallback (
      event = ENTRY_MODIFY,
      path = repoPath,
      callback =  modifyCallback
    )
  }
}
