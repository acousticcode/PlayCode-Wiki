package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.db.slick._
import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import models.Article
import java.util.Date

object Articles extends Controller {

  implicit object ArticleFormat extends Format[Article] {
    def reads(json: JsValue) = JsSuccess(Article(
      (json \ "id").as[Option[Long]],
      (json \ "title").as[String],
      (json \ "content").as[String],
      (json \ "date").as[Date]
    ))

    def writes(a: Article): JsValue = {
      Json.obj(
        "id" -> a.id,
        "title" -> a.title,
        "content" -> a.content,
        "date" -> a.date
      )
    }
  }

  def list = DBAction {
    implicit rs =>
      Ok(Json.toJson(
        models.Articles.list))
  }

  def show(title: String) = DBAction {
    implicit rs =>
      Ok(Json.toJson(models.Articles.findByTitle(title)))
  }

  val articleForm = Form(
    tuple("id" -> optional(longNumber), "title" -> text, "content" -> text)
  )

  def add = DBAction {
    implicit rs =>
      val data = articleForm.bindFromRequest.get
      models.Articles.insert(new Article(data._2, data._3))
      Ok("")
  }

  def updateForm(title: String) = TODO

  def update(title: String) = TODO

  def deleteForm(title: String) = TODO

  def delete(title: String) = DBAction {
    implicit rs =>

      models.Articles.delete(title)

      Ok("")
  }

  def history = TODO
}
