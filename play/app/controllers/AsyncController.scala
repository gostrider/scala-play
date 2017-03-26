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

class AsyncController @Inject() extends Controller {
  def index: Action[AnyContent] = Action.async { implicit request =>

    val payload: Request = request.body.asJson.get.as[Request]

    if (payload.isGroup) {
      UserService.fetchGroupID(payload.senderUserID).flatMap {
        case None => Future.successful(NotFound("Group ID not found"))
        case Some(sender) =>
          val senderID = UserService.getJID(sender)
          RequestService.fetchValidType(payload.msgType).flatMap {
            case None => Future.successful(NotFound("Invalid message type"))
            case Some(msgType) =>
              UserService.fetchUserID(payload.recipientUserID).flatMap {
                case None => Future.successful(NotFound("Recipient ID not found"))
                case Some(recipient) =>
                  val recipientID = UserService.getJID(recipient)
                  Future.successful(
                    Ok("sender:" + senderID +
                      "\n recipient:" + recipientID +
                      "\n type:" + msgType))
              }
          }
      }
    } else {
      UserService.fetchUserID(payload.senderUserID).flatMap {
        case None => Future.successful(NotFound("Sender ID not found"))
        case Some(sender) =>
          val senderID = UserService.getJID(sender)
          RequestService.fetchValidType(payload.msgType).flatMap {
            case None => Future.successful(NotFound("Invalid message type"))
            case Some(msgType) =>
              UserService.fetchUserID(payload.recipientUserID).flatMap {
                case None => Future.successful(NotFound("Recipient ID not found"))
                case Some(recipient) =>
                  val recipientID = UserService.getJID(recipient)
                  Future.successful(
                    Ok("sender:" + senderID +
                      "\n recipient:" + recipientID +
                      "\n type:" + msgType))
              }
          }
      }
    }
  }
}