import java.io.{File, FileNotFoundException}

import amf.client.AMF
import amf.client.model.document.BaseUnit
import core.APIS._
import helper.amf_helper.{AmfParsingHelper, AmfResolutionHelper, AmfValidationHelper}
import helper.swagger_parser_validator.{SwaggerParsingHelper, SwaggerValidationHelper}
import helper.yaml_helper.YamlParsingHelper

import scala.collection.JavaConverters._
import org.raml.parser.visitor.RamlValidationService

object App {

  val YAML: Boolean = false
  val AMF_PARSING: Boolean = false
  val AMF_VALIDATION: Boolean = false
  val AMF_DOUBLE_VALIDATION: Boolean = true
  val AMF_RESOLUTION: Boolean = false
  val RAML_PARSER: Boolean = false
  val SWAGGER_PARSER: Boolean = false
  val SWAGGER_VALIDATION: Boolean = false


//  val apiPath: String = "/Users/lucas.block/mulesoft/PlatformSnapshotAll-plus/small/part2/org_a8a0badd-95dc-4259-ad41-2ac50189b1ce/api_6109_ver_10147/api.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/PlatformSnapshotAll-plus/small/part2/org_a8a0badd-95dc-4259-ad41-2ac50189b1ce/api_6109_ver_7609/api.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/PlatformSnapshotAll-plus/part3-Errors/org_1dec3fc1-ce76-4655-83b4-df9d6b546bc9/api_46065_ver_47765/api.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/current/financial-api/infor-financial-api.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/AMFScalaRunnerV2/run/api.raml"
//  val apiPath: String = "/Users/lucas.block/Downloads/api_6109_ver_10147/api.raml"
  val apiPath: String = "/Users/lucas.block/mulesoft/PlatformSnapshotAll-plus/problems/part3-Errors/org_1dec3fc1-ce76-4655-83b4-df9d6b546bc9/api_46065_ver_47765/api.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/PlatformSnapshotAll-plus/small/part1/org_be8f07f1-b04f-4b51-be0e-75c65f54b5ae/api_75818_ver_78924/americanflightapi.raml"

//  val apiKind: APIType = RAML08
  val apiKind: APIType = RAML10

  def main(args: Array[String]): Unit = {

    var baseUnit: BaseUnit = null

    println("STARTING")
    AMF.init().get()

    val file = new File(apiPath)
//    val file2 = new File(apiPath2)
    if (!file.isFile) printAndThrow("ERROR WITH FILE", new FileNotFoundException())

    if (YAML) {
      YamlParsingHelper.handleParse(file) match {
        case Right(y) =>
          println(y)
          println("YAML PARSER OK")
        case Left(e) => printAndThrow(s"YAML PARSER ERROR: ${e.getMessage}", e)
      }
    }

    if (AMF_PARSING) {
      AmfParsingHelper.handleParse(file, apiKind) match {
        case Right(b) =>
          baseUnit = b
          println("AMF PARSING OK")
        case Left(e) => printAndThrow(s"AMF PARSING ERROR: ${e.getMessage}", e)
      }
    }

    if (AMF_VALIDATION) {

      AmfParsingHelper.handleParse(file, apiKind) match {
        case Right(b) =>
          baseUnit = b
          println("AMF PARSING OK")
        case Left(e) => printAndThrow(s"AMF PARSING ERROR: ${e.getMessage}", e)
      }

      AmfValidationHelper.handleValidation(apiKind, baseUnit) match {
        case Right(r) =>
          if (r.conforms)
            println("AMF VALIDATION OK")
          else
            println(s"AMF VALIDATION ERROR: ${AmfValidationHelper.handleValidationResults(r.results.asScala.toList)}")
        case Left(e) => printAndThrow(s"AMF VALIDATION ERROR: ${e.getMessage}", e)
      }
    }

    if (AMF_DOUBLE_VALIDATION) {

      AmfParsingHelper.handleParse(file, apiKind) match {
        case Right(b) =>
          baseUnit = b
          println("AMF PARSING OK")
        case Left(e) => printAndThrow(s"AMF PARSING ERROR: ${e.getMessage}", e)
      }

      AmfValidationHelper.handleValidation(apiKind, baseUnit) match {
        case Right(r) =>
          if (r.conforms)
            println("AMF FIRST VALIDATION OK")
          else
            println(s"AMF FIRST VALIDATION ERROR: ${AmfValidationHelper.handleValidationResults(r.results.asScala.toList)}")
        case Left(e) => printAndThrow(s"AMF FIRST VALIDATION ERROR: ${e.getMessage}", e)
      }

      AmfValidationHelper.handleValidation(apiKind, baseUnit) match {
        case Right(r) =>
          if (r.conforms)
            println("AMF SECOND VALIDATION OK")
          else
            println(s"AMF SECOND VALIDATION ERROR: ${AmfValidationHelper.handleValidationResults(r.results.asScala.toList)}")
        case Left(e) => printAndThrow(s"AMF SECOND VALIDATION ERROR: ${e.getMessage}", e)
      }
    }

    if (AMF_RESOLUTION) {

      AmfParsingHelper.handleParse(file, apiKind) match {
        case Right(b) =>
          baseUnit = b
          println("AMF PARSING OK")
        case Left(e) => printAndThrow(s"AMF PARSING ERROR: ${e.getMessage}", e)
      }

      AmfResolutionHelper.handleResolution(apiKind, baseUnit) match {
        case Right(b) =>
          println("AMF RESOLUTION-RESOLUTION OK")
          AmfValidationHelper.handleValidation(apiKind, baseUnit) match {
            case Right(r) =>
              if (r.conforms)
                println("AMF RESOLUTION-VALIDATION OK")
              else
                println(s"AMF RESOLUTION-VALIDATION ERROR: ${AmfValidationHelper.handleValidationResults(r.results.asScala.toList)}")
            case Left(e) => printAndThrow(s"AMF RESOLUTION-VALIDATION ERROR: ${e.getMessage}", e)
          }
        case Left(e) => printAndThrow(s"AMF RESOLUTION-RESOLUTION ERROR: ${e.getMessage}", e)
      }

    }

    if (RAML_PARSER) {
      val results = RamlValidationService.createDefault.validate(file.getAbsolutePath)
      print("Java Parser" +  results)
    }

    if (SWAGGER_PARSER) {
      SwaggerParsingHelper.handleParse(file) match {
        case Right(_) => println("SWAGGER PARSING OK")
        case Left(e) => printAndThrow(s"SWAGGER PARSING ERROR: ${e.getMessage}", e)
      }
    }

    if (SWAGGER_VALIDATION) {
      SwaggerValidationHelper.handleValidation(file) match {
        case Right(_) => println("SWAGGER VALIDATION OK")
        case Left(e) => println(s"SWAGGER VALIDATION ERROR: $e")
      }
    }

  }

  private def printAndThrow(string: String, e: Throwable): Unit = {
    println(string)
    throw e
  }
}
