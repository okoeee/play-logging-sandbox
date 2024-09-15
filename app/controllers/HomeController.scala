package controllers

import javax.inject.*
import play.api.*
import play.api.libs.json.Json
import play.api.mvc.*

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val responseBody = Json.obj(
      "status" -> "success",
      "message" -> "Hello, Play Framework!"
    )
    Ok(responseBody)
  }

}
