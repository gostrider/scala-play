/**
  * Created by erikcom on 24/3/17.
  */

package services

import models.Request

import scalaz.\/

object RequestService {
  def getName(data: Request): String = data.name

  def getDetails(data: Request): String = data.detail

  def validateName(name: String): String \/ String = ???
}
