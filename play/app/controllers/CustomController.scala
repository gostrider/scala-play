/**
  * Created by erikcom on 23/3/2017.
  */

package controllers

import javax.inject.Inject

import models.Request
import play.api.libs.json.{Reads, _}
import play.api.libs.ws._
import play.api.mvc._
import services.RequestService

import scala.concurrent.ExecutionContext.Implicits.global

class CustomController @Inject()(ws: WSClient) extends Controller {

  def validateJSON[A: Reads]: BodyParser[A] = BodyParsers.parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  def index: Action[AnyContent] = Action { implicit request =>
    val jsonPayload: Request = request.body.asJson.get.as[Request]

    //    ws.url("http://staging-chat.chaatz.com:4000").get().map { response => Ok(response.body) }
    Ok("runnable")
  }
}