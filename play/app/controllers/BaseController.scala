/**
  * Created by erikcom on 23/3/2017.
  */

package controllers

import javax.inject.Inject

import models.Request
import play.api.mvc._
import services.{RequestService, UserService}

class BaseController @Inject() extends Controller {
  /*
  * Application Flow:
  * 1. Check is group message
  * 2. Validate group id
  * 3. Get sender profile from local storage
  * 4. From user get JID
  * 5. Validate message type
  * 6. Get recipient profile from local storage
  * 7. (Skip) Parse fields to JSON
  * 8. (Skip) Send request to web service
  * */

  def index: Action[AnyContent] = Action { implicit request =>

    val payload: Request = request.body.asJson.get.as[Request]

    if (payload.isGroup) {
      if (UserService.isGroupID(payload.senderUserID))
        UserService.getUser(payload.senderUserID).map { user =>

          val senderID = UserService.getJID(user)
          RequestService.isValidType(payload.msgType) match {
            // Pattern match method
            case None => BadRequest("Invalid message type")
            case Some(msgType) => UserService.getUser(payload.recipientUserID).map {
              // Monadic method handling
              recipientID =>
                Ok("sender:" + senderID +
                  "\n recipient:" + recipientID +
                  "\n type:" + msgType)
            } getOrElse BadRequest("Recipient ID not found")
          }

        } getOrElse BadRequest("Group ID not found") else BadRequest("Invalid group id")
    } else {
      if (UserService.isUserID(payload.senderUserID))
        UserService.getUser(payload.senderUserID).map { user =>

          val senderID = UserService.getJID(user)
          RequestService.isValidType(payload.msgType) match {
            case None => BadRequest("Invalid message type")
            case Some(msgType) => UserService.getUser(payload.recipientUserID).map {
              recipientID =>
                Ok("sender:" + senderID +
                  "\n recipient:" + recipientID +
                  "\n type:" + msgType)
            } getOrElse BadRequest("Recipient ID not found")
          }
        } getOrElse BadRequest("Sender ID not found") else BadRequest("Invalid username")

    }
  }
}