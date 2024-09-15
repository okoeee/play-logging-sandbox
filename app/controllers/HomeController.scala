package controllers

import filters.LoggingActionFilter

import javax.inject.*
import play.api.*
import play.api.libs.json.Json
import play.api.mvc.*

@Singleton
class HomeController @Inject()(
  val controllerComponents: ControllerComponents,
  loggingActionFilter: LoggingActionFilter
) extends BaseController {

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val responseBody = Json.obj(
      "status" -> "success",
      "message" -> "Hello, Play Framework!"
    )
    Ok(responseBody)
  }

  def post(): Action[AnyContent] = (Action andThen loggingActionFilter) { implicit request: Request[AnyContent] =>
    val responseBody = Json.obj(
      "status" -> "success",
      "message" -> "Hello, Play Framework!"
    )
    Ok(responseBody)
  }

}
