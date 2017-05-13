package services
import models._

/**
  * Created by szq on 2017/5/13.
  */
trait RepoService extends QuillSupport {
  import QDB._

  def getAllRepos = {
    def selectRepo = quote {
      query[Repository]
    }
    run(selectRepo)
  }

  def addRepo(repo:Repository) = {
    def insertRepo(repo:Repository) = quote {
      query[Repository].insert(lift(repo))
    }
    run(insertRepo(repo))
  }

  def updateRepoById(repo:Repository) = {
    def updateRepo(repo:Repository) = quote {
      query[Repository].filter(_.id == lift(repo.id)).update(lift(repo))
    }
    run(updateRepo(repo))
  }

  def removeRepoById(id:Long) = {
    def deleteRepo(id:Option[Long]) = quote {
      query[Repository].filter(_.id == lift(id)).delete
    }
    run(deleteRepo(Some(id)))
  }
}
