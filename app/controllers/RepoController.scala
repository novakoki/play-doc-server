package controllers
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import services._
import io.circe.syntax._
import io.circe.generic.auto._
import models.Repository

/**
  * Created by szq on 2017/5/13.
  */
class RepoController extends Controller with RepoService {
  def getRepos = Action.async {
    for {
      repos <- getAllRepos
    } yield Ok(repos.asJson.noSpaces)
  }

  def createRepo = Action.async(parse.form(repoForm)) { implicit request =>
    for {
      r <- addRepo(request.body)
    } yield Ok("")
  }

  def updateRepo = Action.async(parse.form(repoForm)) {implicit request =>
    for {
      r <- updateRepoById(request.body)
    } yield Ok("")
  }

  def removeRepo(id:Long) = Action.async {
    for {
      r <- removeRepoById(id)
    } yield Ok("")
  }

  val repoForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "name" -> nonEmptyText,
      "baseUrl" -> nonEmptyText,
      "description" -> text
    )(Repository.apply)(Repository.unapply)
  )

}
