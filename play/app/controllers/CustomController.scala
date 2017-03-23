/**
  * Created by erikcom on 23/3/2017.
  */

package controllers

import javax.inject.Inject

import models.RequestPayload
import play.api.libs.json._
import play.api.libs.json.Reads
import play.api.mvc.{Action, BodyParser, BodyParsers, Controller}
import scala.concurrent.ExecutionContext.Implicits.global

//@Singleton
class CustomController @Inject() extends Controller {

  def validateJSON[A: Reads]: BodyParser[A] = BodyParsers.parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  def index: Action[RequestPayload] = Action(validateJSON[RequestPayload]) { implicit request =>
    // Transform json payload as type
//    val jsonPayload = request.body.asJson.get.as[RequestPayload]
    // Validate json payload implicitly
    val jsonPayload = request.body
    println(jsonPayload)
    Ok("runnable " + jsonPayload.name)
  }
}