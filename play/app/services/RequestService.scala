/**
  * Created by erikcom on 24/3/17.
  */

package services

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object RequestService {
  private val messageType = Seq("text", "image", "voice")

  // For base example use case, blocking version
  def isValidType(msgType: String): Option[String] =
    messageType.find(_ == msgType)

  // For refactor one use case, async version
  def fetchValidType(msgType: String): Future[Option[String]] =
    Future(messageType.find(_ == msgType))
}
