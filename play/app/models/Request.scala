/**
  * Created by erikcom on 23/3/2017.
  */

package models

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

case class Request(name: String, detail: String)

object Request {
  implicit val requestPayloadReads: Reads[Request] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "detail").read[String]
    ) (Request.apply _)
}
