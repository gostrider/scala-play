/**
  * Created by erikcom on 23/3/2017.
  */

package controllers

import javax.inject.Inject

import models.Request
import play.api.libs.ws._
import play.api.mvc._
import services.{RequestService, UserService}

//import scala.concurrent.ExecutionContext.Implicits.global

class CustomController @Inject()(ws: WSClient) extends Controller {
  def index: Action[AnyContent] = Action { implicit request =>
    val requestPayload: Request = request.body.asJson.get.as[Request]

    if (requestPayload.isGroup) {
      if (UserService.isGroupID(requestPayload.senderUserID)) UserService.getUser(requestPayload.senderUserID).map { user =>
        val userID = UserService.getJID(user)
        RequestService.isValidType(requestPayload.msgType) match {
          // Pattern match method
          case None => BadRequest("Invalid message type")
          case Some(msgType) =>
            UserService.getUser(requestPayload.recipientUserID).map {
              // Monadic method handling
              rUser => Ok("")
            } getOrElse BadRequest("recipient not found")
        }
      } getOrElse BadRequest("sender not found") else BadRequest("Invalid group id")
    } else {
      if (UserService.isUserID(requestPayload.senderUserID)) UserService.getUser(requestPayload.senderUserID).map { user =>
        val userID = UserService.getJID(user)
        RequestService.isValidType(requestPayload.msgType) match {
          case None => BadRequest("Invalid message type")
          case Some(msgType) =>
            UserService.getUser(requestPayload.recipientUserID).map {
              rUser => Ok("")
            } getOrElse BadRequest("recipient not found")
        }
      } getOrElse BadRequest("sender not found") else BadRequest("Invalid username")
    }
  }
}