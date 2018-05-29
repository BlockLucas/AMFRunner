package helper.java_parser_helper

import java.io.File

import core.APIS.{APIType, RAML08}
import org.raml.parser.rule.ValidationResult.Level
import scala.collection.JavaConverters._

object RamlParsingValidationHelper {

  def getValidationErrors(file : File, apiKind: APIType): Either[Throwable, List[String]] = {
    try {
      if (apiKind == RAML08) {
        // 0.8 use v1
        val validatorService = JavaParserHandler.getValidatorServiceV1
        validatorService.validate(file.getAbsolutePath).asScala.filter(_.getLevel == Level.ERROR) match {
          case result =>
            if (result.nonEmpty) {
              Right(result.map(_.getMessage).toList)
            } else {
              Right(List.empty)
            }
        }
      } else {
        // 1.0 use v2
        val parser = JavaParserHandler.getParserV2(file.getParentFile.getPath)
        parser.buildApi(file)
          match {
          case result => {
            if (result.hasErrors) {
              Right(result.getValidationResults.asScala.map(_.getMessage).toList)
            } else {
              Right(List.empty)
            }
          }
        }
      }
    } catch {
      case e: Throwable =>
        println(s"ERROR Java Parser/Validator. File: $file - ${e.getMessage}", e)
        Left(e)
    }
  }

}
