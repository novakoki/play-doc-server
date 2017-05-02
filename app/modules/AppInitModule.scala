package modules

import com.google.inject.AbstractModule
import services.WatchService

/**
  * Created by szq on 2017/5/2.
  */
class AppInitModule extends AbstractModule {
  override def configure() = {
    bind(classOf[WatchService]).asEagerSingleton
  }
}
