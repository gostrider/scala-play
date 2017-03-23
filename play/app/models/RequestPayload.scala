/**
  * Created by erikcom on 23/3/2017.
  */

package models

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

case class RequestPayload(name: String, details: String)

object RequestPayload {
  implicit val requestPayloadReads: Reads[RequestPayload] = (
    // For model encode to JSON, use (JsPath \ "field").write[String]
    // Equivalent to JSON decode to model
    (JsPath \ "name").read[String] and
      (JsPath \ "details").read[String]
    ) (RequestPayload.apply _)
}
