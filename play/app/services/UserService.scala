/**
  * Created by erikcom on 25/3/2017.
  */

package services

import models.User

import scala.concurrent.Future

object UserService {
  private val users = Seq(
    User("user10", "some_uuid"),
    User("user11", "some_uuid_2"),
    User("group1", "some_uuid_3")
  )

  def getUser(username: String): Option[User] =
    users.find(_.jid == username)

  def getJID(user: User): String = user.jid

  def getUserID(user: User): String = user.userid

  def isUserID(username: String): Boolean =
    !(username contains "group") && (username contains "user")

  def isGroupID(username: String): Boolean =
    (username contains "group") && !(username contains "user")
}