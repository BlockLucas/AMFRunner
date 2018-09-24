import java.io.{File, FileNotFoundException}

import amf.client.AMF
import amf.client.model.document.BaseUnit
import amf.core.benchmark.ExecutionLog
import core.APIS._
import helper.amf_helper.{AmfGenerationHelper, AmfParsingHelper, AmfResolutionHelper, AmfValidationHelper}
import helper.java_parser_helper.RamlParsingValidationHelper
import helper.swagger_parser_validator.{SwaggerParsingHelper, SwaggerValidationHelper}
import helper.yaml_helper.YamlParsingHelper

import scala.collection.JavaConverters._

object App {

  val YAML: Boolean = false
  val AMF_PARSING: Boolean = true
  val AMF_VALIDATION: Boolean = false
  val AMF_VALIDATION_ITERATE: Boolean = false
  val AMF_RESOLUTION: Boolean = false

  val AMF_DOUBLE_VALIDATION: Boolean = false

  val RAML_PARSER: Boolean = false
  val SWAGGER_PARSER: Boolean = false
  val SWAGGER_VALIDATION: Boolean = false

  val RAML_DOUBLE_PARSER: Boolean = false
  val GENERATE: Boolean = false

  // Full Path of the master API
  val apiPath: String = ""

  val apiKind: APIType = RAML10 //RAML10, RAML08, OAS20, JSON_LD

  def main(args: Array[String]): Unit = {

//    ExecutionLog.start()

    var baseUnit: BaseUnit = null

    println("STARTING")
    AMF.init().get()

    val file = new File(apiPath)
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

    if (AMF_VALIDATION_ITERATE) {
      var i: Double = 0
      var success: Double = 0
      var unsuccess: Double = 0
      var baseUnitIterate: BaseUnit = null

      while(true){
        i = i + 1
        AmfParsingHelper.handleParse(file, apiKind) match {
          case Right(b) =>
            baseUnitIterate = b
            println("AMF PARSING OK")
          case Left(e) => {
            println(s"AMF PARSING ERROR: ${e.getMessage}")
            unsuccess = unsuccess + 1
          }
        }

        if (baseUnitIterate != null) {
          AmfValidationHelper.handleValidation(apiKind, baseUnitIterate) match {
            case Right(r) =>
              if (r.conforms){
                println("AMF VALIDATION OK")
                success = success + 1
              }
              else {
                println(s"AMF VALIDATION ERROR: ${AmfValidationHelper.handleValidationResults(r.results.asScala.toList)}")
                unsuccess = unsuccess + 1
              }
            case Left(e) => printAndThrow(s"AMF VALIDATION ERROR: ${e.getMessage}", e)
          }
        }
        println(s"results: \n\tsuccess: $success\n\tunsuccess: $unsuccess\n\ttotal: $i\n\tsuccessrate: ${(success/i)*100}%")
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
      val start = System.nanoTime()
      println("Java Parser Starting Validation")
      RamlParsingValidationHelper.getValidationErrors(file, apiKind) match {
        case Right(r) =>
          if (r.isEmpty)
            println("Java Parser VALIDATION OK")
          else
            println(s"Java Parser Validations: $r}")
        case Left(e) => printAndThrow(s"Java Parser ERROR: ${e.getMessage}", e)
      }
      val elapsed = (System.nanoTime() - start) / 1000000
      println(s"Java Parser took $elapsed milliseconds")
    }

    if (RAML_DOUBLE_PARSER) {
      val start = System.nanoTime()
      RamlParsingValidationHelper.getValidationErrors(file, apiKind) match {
        case Right(r) =>
          if (r.isEmpty)
            println("Java Parser FIRST VALIDATION OK")
          else
            println(s"Java Parser FIRST Validations: $r}")
        case Left(e) => printAndThrow(s"Java Parser ERROR: ${e.getMessage}", e)
      }
      val elapsed = (System.nanoTime() - start) / 1000000
      println(s"Java Parser FIRST took $elapsed milliseconds")

      val start2 = System.nanoTime()
      RamlParsingValidationHelper.getValidationErrors(file, apiKind) match {
        case Right(r) =>
          if (r.isEmpty)
            println("Java Parser SECOND VALIDATION OK")
          else
            println(s"Java Parser SECOND Validations: $r}")
        case Left(e) => printAndThrow(s"Java Parser ERROR: ${e.getMessage}", e)
      }
      val elapsed2 = (System.nanoTime() - start2) / 1000000
      println(s"Java Parser SECOND took $elapsed2 milliseconds")
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

    if (GENERATE) {
      var baseUnit: BaseUnit = null

      AmfParsingHelper.handleParse(file, apiKind) match {
        case Right(b) =>
          baseUnit = b
          println("AMF PARSING OK")
        case Left(e) => printAndThrow(s"AMF PARSING ERROR: ${e.getMessage}", e)
      }

      AmfResolutionHelper.handleResolution(apiKind, baseUnit) match {
        case Right(b) =>
          baseUnit = b
          println("AMF RESOLUTION-RESOLUTION OK")
        case Left(e) => printAndThrow(s"AMF RESOLUTION ERROR: ${e.getMessage}", e)
      }

      val targetFile = new File("/Users/lucas.block/mulesoft/AMFScalaRunnerV2/run/target.raml")
      AmfGenerationHelper.handleGen(targetFile, apiKind, baseUnit) match {
        case Right(_) => println("AMF GENERATION OK")
        case Left(e) => printAndThrow(s"AMF GENERATION ERROR: ${e.getMessage}", e)
      }
    }

//    ExecutionLog.finish()
//    ExecutionLog.buildReport
    println("\n\nFINISH OK")
  }

  private def printAndThrow(string: String, e: Throwable): Unit = {
    println(string)
    throw e
  }
}
