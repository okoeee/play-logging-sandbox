package filters

import play.api.Logger
import play.api.mvc.{ActionFilter, AnyContent, Request, Result}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

/**
 * ActionFilterを使ってログにbodyを出力
 *
 * Play FrameworkデフォルトのFilterではbodyの解析ができない。
 * Filterの引数がRequestHeaderになっている。
 *
 * おそらくパフォーマンスとセキュリティの観点からそうしているのだと推察。
 *
 * stack overflow
 * https://stackoverflow.com/questions/25492592/how-to-log-the-body-from-every-request-in-play/25492776#25492776
 *
 * このクラスで使っているActionFilterではbodyを取得することができるので、ログにbodyを出力する際にはこれを使うのが良さそう。
 * 他にもEssentialFilterというものがあったが、Akkaの知識が必要そう。
 * https://www.playframework.com/documentation/2.7.x/ScalaHttpFilters#More-powerful-filters
 */
class LoggingActionFilter @Inject()(
 implicit ec: ExecutionContext
) extends ActionFilter[Request]:

  private val logger: Logger = Logger(this.getClass)
  def executionContext: ExecutionContext = ec

  def filter[A](input: Request[A]): Future[Option[Result]] = Future.successful {
    input.body match {
      case body: AnyContent =>
        body.asJson.foreach  { json =>
          logger.info(s"body: ${json.toString}")
        }
        // Noneを返すと処理が継続される
        None
      case _ => None
    }
  }

  def filterForVer[A](input: Request[A]): Future[Option[Result]] = Future.successful {
    for json <- input.body.asInstanceOf[AnyContent].asJson
    do logger.info(s"body: ${json.toString}")

    None
  }


