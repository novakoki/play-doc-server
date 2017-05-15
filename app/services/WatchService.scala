package services

import com.beachape.filemanagement.MonitorActor
import javax.inject._
import akka.actor._
import parser.AppParser

/**
  * Created by szq on 2017/5/2.
  */

class WatchService @Inject() (system:ActorSystem) extends AppParser with ApiService {
  val fileMonitorActor = system.actorOf(MonitorActor(concurrency = 2))
}
