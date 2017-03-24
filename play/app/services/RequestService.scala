/**
  * Created by erikcom on 24/3/17.
  */

package services

import models.RequestPayload

object RequestService {
  def getName(data: RequestPayload): String = data.name

  def getDetails(data: RequestPayload): String = data.details
}
