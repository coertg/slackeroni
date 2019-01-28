package user
import javax.inject.Inject

class Service @Inject()(
  repo: Repository
){

  def authenticate: (String, String) => Option[User] = repo.authenticate

  def findByEmail: String => Option[User] = repo.findByEmail

  def findBySessionKey: String => Option[User] = repo.findByEmail

  def findById: Long => Option[User] = repo.findById
}
