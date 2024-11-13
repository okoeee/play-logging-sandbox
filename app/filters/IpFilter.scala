package filters

import org.apache.pekko.stream.Materializer
import play.api.mvc.{Filter, RequestHeader, Result, Results}

import javax.inject.Inject
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class IpFilter @Inject() (
    implicit val mat: Materializer
                         ) extends Filter:

  private def fetchIpFromDB: Future[Seq[String]] = Future {
   Seq("1.1.1.1", "0:0:0:0:0:0:0:1")
  }

  private def isAllowedIP(clientIp: String, allowedIpSeq: Seq[String]) = allowedIpSeq.contains(clientIp)

  override def apply(next: RequestHeader => Future[Result])(rh: RequestHeader): Future[Result] =  {
    println("Filter is executed")
    println(s"Your IP Address Is: ${rh.remoteAddress}")
    fetchIpFromDB.flatMap { IpSeq =>
      if isAllowedIP(rh.remoteAddress, IpSeq) then next(rh)
      else Future.successful(Results.Forbidden)
    }
  }
