package helper.swagger_parser_validator

import java.io.{File, FileNotFoundException}

import common.FileUtils.readFileContentOptional
import core.ObjectsHandler

object SwaggerParsingHelper {

  def handleParse(file: File): Either[Exception, String] = {

    val fileContent = readFileContentOptional(file) match {
      case Some(f) => f
      case None => return Left(new FileNotFoundException("Error reading File"))
    }

    try {
      parse(fileContent)
      Right(fileContent)
    } catch {
      case e: Exception => Left(e)
    }
  }

  private def parse(fileContent: String): Unit = {
    val parser = ObjectsHandler.createSwaggerParserBuilder()
    parser.parse(fileContent)
  }

}
