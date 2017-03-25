/**
  * Created by erikcom on 23/3/2017.
  */

package models

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}


sealed trait RequestReference

case class AvatarUrlOnly(avatarUrl: String) extends RequestReference

case class AvatarAndUrl(avatarUrl: String, url: String) extends RequestReference

case class AllReferences(avatarUrl: String, url: String, preview: String) extends RequestReference

case class Request(isGroup: Boolean,
                   senderUserID: String,
                   recipientUserID: String,
                   msgType: String,
                   message: String,
                   reference: AvatarUrlOnly)

object Request {
  implicit val requestReads: Reads[Request] = (
    (JsPath \ "is_group").read[Boolean] and
      (JsPath \ "sender_user_id").read[String] and
      (JsPath \ "recipient_user_id").read[String] and
      (JsPath \ "message_type").read[String] and
      (JsPath \ "message").read[String] and
      (JsPath \ "references").read[AvatarUrlOnly]
    ) (Request.apply _)
}

object RequestReference {
  def apply(x: RequestReference): RequestReference = x match {
    case ref: AvatarUrlOnly => AvatarUrlOnly(ref.avatarUrl)
    case ref: AvatarAndUrl => AvatarAndUrl(ref.avatarUrl, ref.url)
    case ref: AllReferences => AllReferences(ref.avatarUrl, ref.url, ref.preview)
  }
}

object AvatarUrlOnly {
  /*
  * Since applicative 'and' cannot work with single expression,
  * (JsPath \ "avatar_url").read[String](AvatarUrlOnly.apply _)
  * However, map or flatMap are work with monadic function
  * */
  implicit val avatarUrlOnlyReads: Reads[AvatarUrlOnly] =
    (JsPath \ "avatar_url").read[String].map(AvatarUrlOnly.apply)
}

object AvatarAndUrl {
  implicit val avatarAndUrlReads: Reads[AvatarAndUrl] = (
    (JsPath \ "avatar_url").read[String] and
      (JsPath \ "url").read[String]
    ) (AvatarAndUrl.apply _)
}

object AllReferences {
  implicit val allReferencesReads: Reads[AllReferences] = (
    (JsPath \ "avatar_url").read[String] and
      (JsPath \ "url").read[String] and
      (JsPath \ "preview").read[String]
    ) (AllReferences.apply _)
}