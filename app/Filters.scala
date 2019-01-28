import javax.inject.Inject
import play.api.http.HttpFilters
import play.api.mvc._
import play.filters.gzip.GzipFilter
import play.mvc.Http.HeaderNames._

import scala.concurrent.ExecutionContext.Implicits.global

class Filters @Inject()(gzipFilter: GzipFilter) extends HttpFilters {
  def filters = Seq(gzipFilter, noCacheFilter)

  val noCacheFilter = new EssentialFilter {
    def apply(nextFilter: EssentialAction) = new EssentialAction {
      def apply(requestHeader: RequestHeader) = {
        nextFilter(requestHeader).map { result =>
          if (requestHeader.path.startsWith("/api/"))
            result.withHeaders(
              CACHE_CONTROL -> "no-cache, no-store, must-revalidate",
              PRAGMA -> "no-cache",
              EXPIRES -> "0")
          else result
        }
      }
    }
  }
}