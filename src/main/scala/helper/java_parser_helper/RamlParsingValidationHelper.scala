package helper.java_parser_helper

import java.io.File

import core.ObjectsHandler
import org.raml.v2.api.RamlModelResult

object RamlParsingValidationHelper {

  def handleParseValidation(file: File): Either[Exception, RamlModelResult] = {
    try {
      val result = parseVal(file)
      Right(result)
    } catch {
      case e: Exception => Left(e)
    }
  }

  private def parseVal(file: File): RamlModelResult = {
    val parser = ObjectsHandler.createJavaParserBuilder()
    val result = parser.buildApi(file)
    if (result.hasErrors) throw new Exception("RAML parser with errors = " + result.getValidationResults)
    result
  }

}
