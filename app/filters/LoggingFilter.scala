package filters

import org.apache.pekko.stream.Materializer
import org.apache.pekko.util.ByteString
import play.api.Logger
import play.api.libs.streams.Accumulator
import play.api.mvc.{EssentialAction, EssentialFilter, RequestHeader, Result}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class LoggingFilter @Inject()
(
  using Materializer,
  ExecutionContext
) extends EssentialFilter:

  private val accessLogger: Logger = Logger("access")

  def apply(nextFilter: EssentialAction): EssentialAction = new EssentialAction {
    def apply(requestHeader: RequestHeader): Accumulator[ByteString, Result] = {

      val startTime = System.currentTimeMillis

      nextFilter(requestHeader).map { result =>

        val endTime = System.currentTimeMillis
        val requestTime = endTime - startTime

        accessLogger.info(s"${requestHeader.method} ${requestHeader.uri}" +
          s" took ${requestTime}ms and returned ${result.header.status}")

        result.withHeaders("Request-Time" -> requestTime.toString)

      }
    }
  }
