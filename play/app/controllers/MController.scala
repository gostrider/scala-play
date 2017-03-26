/**
  * Created by erikcom on 26/3/2017.
  */

package controllers

import javax.inject.Inject

import models.Request
import play.api.mvc.{Action, AnyContent, Controller}
import services.{RequestService, UserService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalaz.Scalaz._
import scalaz._

class MController @Inject() extends Controller {
  def index: Action[AnyContent] = Action.async { implicit request =>
    val payload = request.body.asJson.get.as[Request]

    def getID(xID: String) = if (payload.isGroup) UserService.fetchGroupID(xID) else UserService.fetchUserID(xID)

    val result = for {

      senderID <- getID(payload.senderUserID).map {
        _ \/> BadRequest("Group ID not found")
      } |> EitherT.apply

      msgType <- RequestService.isValidType(payload.msgType) \/>
        BadRequest("Invalid message type") |> Future.successful |> EitherT.apply

      recipientID <- UserService.fetchUserID(payload.recipientUserID).map {
        _ \/> BadRequest("Recipient ID not found")
      } |> EitherT.apply

    } yield (senderID, msgType, recipientID)

    Future.successful(Ok(result.run))
  }
}