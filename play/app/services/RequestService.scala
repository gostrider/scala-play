/**
  * Created by erikcom on 24/3/17.
  */

package services

object RequestService {
  private val messageType = Seq("text", "image", "voice")

  def isValidType(msgType: String): Option[String] =
    messageType.find(_ == msgType)
}
