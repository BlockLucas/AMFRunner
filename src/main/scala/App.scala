import java.io.{File, FileNotFoundException}

import scala.concurrent._
import ExecutionContext.Implicits.global

import amf.core.remote.{Platform, RamlYamlHint}
import amf.core.unsafe.PlatformBuilder
import amf.facades.{AMFCompiler, Validation}
import amf.{AMF, ProfileNames}
import amf.model.document.BaseUnit
import amf.plugins.document.webapi.RAML10Plugin
import core.APIS._
import helper.amf_helper.{AmfParsingHelper, AmfResolutionHelper, AmfValidationHelper}
import helper.java_parser_helper.RamlParsingValidationHelper
import helper.swagger_parser_validator.{SwaggerParsingHelper, SwaggerValidationHelper}
import helper.yaml_helper.YamlParsingHelper

import scala.collection.JavaConverters._
import scala.concurrent.Future
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


  val apiPath: String = "/Users/lucas.block/mulesoft/AMFScalaRunnerV2/run/testfile.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/AMFScalaRunnerV2/run/platformCP/api.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/AMFScalaRunnerV2/run/api_7898_ver_8051/pad_lab7.raml"
//  val apiPath2: String = "/Users/lucas.block/mulesoft/PlatformSnapshotAll-plus/small/part1/org_f795d042-f833-4f57-bec3-75d17d17f4d8/api_5077_ver_5072/recommender.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/TroubleFiles/StackOverFlow/api.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/NewYork/client-contracts-modified/client-contracts.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/NewYork/client-contracts/client-contracts.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/current/API/responses_Rv1-0.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/raml-tck/tests/semantic/root-section/default-media-type/basic-bad_Rv1-0.raml"

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
          val str = AMF.raml10Generator().generateString(baseUnit)
          println("AMF PARSING OK")
        case Left(e) => printAndThrow(s"AMF PARSING ERROR: ${e.getMessage}", e)
      }
    }

//    if (AMF_PARSING) {
//      AmfParsingHelper.handleParse(file2, apiKind) match {
//        case Right(b) =>
//          baseUnit = b
//          val str = AMF.raml10Generator().generateString(baseUnit)
//          println("AMF PARSING OK")
//        case Left(e) => printAndThrow(s"AMF PARSING ERROR: ${e.getMessage}", e)
//      }
//    }

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
//      RamlParsingValidationHelper.handleParseValidation(file) match {
//        case Right(result) => {
//          println("JAVA PARSING/VALIDATION OK")
//          println("JAVA PARSING/VALIDATION Validation Results" + result.getValidationResults.asScala.map("Message =" + _.getMessage).toList)
//
//        }
//        case Left(e) => printAndThrow(s"JAVA PARSING/VALIDATION ERROR: ${e.getMessage}", e)
//      }
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
