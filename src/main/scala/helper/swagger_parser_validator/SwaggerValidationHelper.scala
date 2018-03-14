package helper.swagger_parser_validator

import java.io.File

import play.api.libs.json.Json

import scala.language.{implicitConversions, postfixOps}
import scala.sys.process._

object SwaggerValidationHelper {

  def handleValidation(file: File): Either[String, Unit] = {
    //Left boolean indicates if is an error or an exception
    try {
      val response = validate(file)
      if (response.nonEmpty && !response.equals("{}\n")) {
        val validationResult = try { Json.prettyPrint(Json.parse(response)) } catch { case e: Exception => e}
        Left(s"Error in Validation:\n$validationResult")
      } else Right()
    } catch {
      case e: Exception => Left(s"Error Calling Remote Swagger Validation: ${e.getMessage}")
    }
  }

  private def validate(file: File): String = {
    val call = Seq("curl", "-X", "POST", "http://online.swagger.io/validator/debug", "-H", "Content-Type: application/json", "-d", s"@${file.getPath}")
    call!!
  }

}
