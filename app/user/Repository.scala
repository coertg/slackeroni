package user

import java.security.MessageDigest
import java.sql.SQLException

import anorm.Macro.namedParser
import anorm.{SqlParser, SqlStringInterpolation}
import javax.inject.Inject
import play.api.db.DBApi

class Repository @Inject()(
  dBApi: DBApi
){

  private val db = dBApi.database("default")
  private val parser = namedParser[User]

  private[user] def findByEmail(email: String): Option[User] = {
    db.withConnection { implicit c =>
      SQL"""
        SELECT
          id,
          username,
          passhash,
          display_name AS displayName
        FROM slackeroni.users
        WHERE username=$email
      """.as(parser.singleOpt)
    }
  }

  private[user] def findById(id: Long): Option[User] = {
    db.withConnection { implicit c =>
      SQL"""
        SELECT
          id,
          username,
          passhash,
          display_name AS displayName
        FROM slackeroni.users
        WHERE id=$id
      """.as(parser.singleOpt)
    }
  }

  private[user] def create(username: String, password: String, displayName: String): Either[Error, User] = {
    try {
      db.withConnection { implicit c =>
        val passhash = md5(password)
        val newId =
          SQL"""
          INSERT INTO
            slackeroni.users (
              username,
              passhash,
              display_name
            )
            VALUES (
              $username,
              $passhash,
              $displayName
            );
        """.executeInsert(SqlParser.scalar[Long].single)

        Right(
          User(
            newId,
            username,
            passhash,
            displayName
          )
        )
      }
    } catch {
      handleException
    }
  }

  private[user] def authenticate(email: String, password: String): Option[User] = {
    findByEmail(email).filter { account =>  md5(password) == account.passhash }
  }

  private def md5(s: String): String = {
    MessageDigest.getInstance("MD5").digest(s.getBytes).map("%02x".format(_)).mkString
  }

  private def handleException[A]: PartialFunction[Throwable, Either[Error, A]] = {
    case e: SQLException if e.getSQLState == "23505" => Left(Error.CONFLICT)
  }
}
