import java.io.{File, FileNotFoundException}

import amf.client.AMF
import amf.client.model.document.BaseUnit
import amf.core.benchmark.ExecutionLog
import core.APIS._
import helper.amf_helper.{AmfParsingHelper, AmfResolutionHelper, AmfValidationHelper}
import helper.java_parser_helper.RamlParsingValidationHelper
import helper.swagger_parser_validator.{SwaggerParsingHelper, SwaggerValidationHelper}
import helper.yaml_helper.YamlParsingHelper

import scala.collection.JavaConverters._

object App {

  val YAML: Boolean = false
  val AMF_PARSING: Boolean = false
  val AMF_VALIDATION: Boolean = true
  val AMF_DOUBLE_VALIDATION: Boolean = false
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
//  val apiPath: String = "/Users/lucas.block/mulesoft/current/postVideo.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/AMFScalaRunnerV2/run/testfile.json"
//  val apiPath: String = "/Users/lucas.block/AWSDownloads/parts-together/fat-ramls-sync/eb61ef69-8508-4715-b974-4e1b9d931e6b/17696991-7782-4608-8c96-ca186dde8763/api.raml"
  //Users/lucas.block/AWSDownloads/parts-together/fat-ramls-sync/dd3c1dec-f237-4ce6-9a6d-d7d507f86dd3/0bccbc70-c065-496c-9073-6473ddb2e3bc/api.raml
//  val apiPath: String = "/Users/lucas.block/AWSDownloads/Unzipped/All/3dab727d-b742-4b70-9893-9b8ab35a8ad1/5d4c2841-5109-4725-8c73-917e57aa7620/location-summary-api.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/current/wealth-wpc-admin-api-2.0.1-fat-raml/wpc-admin-api.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/current/wealth-wpc-admin-api-2.0.1-fat-raml/wpc-admin-api.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/current/field-nation-api-2.0.0-fat-raml/field-nation-v2-API.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/current/s-suez-delivery-collection-api-1.0.0-fat-raml/s-suez-delivery-collection-api.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/raml-tck/tests/spec-examples/complex-examples_Rv1-0.raml"
//  val apiPath: String = "/Users/lucas.block/AWSDownloads/Unzipped/All/135d8708-b954-4b00-96d7-70b22b16597f/4c41aa37-2161-4a6b-bebd-cab196cd18ca/api.raml"
//  val apiPath: String = "/Users/lucas.block/AWSDownloads/Unzipped/All/0a8d88d8-361a-4fe6-bda2-23e8577fcd04/a4bfa508-2eb2-440e-8329-3d2ecfae86d9/api.v1.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/AMFScalaRunnerV2/run/api_33974_ver_35336/api.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/AMFScalaRunnerV2/run/testfile.raml"
//  val apiPath : String = "/Users/lucas.block/mulesoft/AMFScalaRunnerV2/run/api_33974_ver_35336/api.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/PlatformSnapshotAll-plus/problems/part3-Errors/org_1dec3fc1-ce76-4655-83b4-df9d6b546bc9/api_46065_ver_47765/api.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/PlatformSnapshotAll-plus/small/part1/org_b1444ccd-aebb-4ad4-8fd2-8f373c433711/api_35420_ver_77911/api.raml"
//  val apiPath: String = "/Users/lucas.block/AWSDownloads/Unzipped/All/087f1a04-c980-4796-ae76-86f9ccd5f0b1/8c633d6b-4d5e-4dc3-9e45-b4b95543658f/e-bo.raml"
//  val apiPath: String = "/Users/lucas.block/AWSDownloads/Unzipped/All/087f1a04-c980-4796-ae76-86f9ccd5f0b1/a9a16827-02e7-44d6-959f-256621712528/q-rd.raml"
//  val apiPath: String = "/Users/lucas.block/AWSDownloads/Unzipped/All/149ab34a-eadd-4df6-b478-098a2e655e83/4e214511-5804-405c-88f7-aae4493647c5/lob-api.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/raml-java-parser/raml-parser-2/src/test/resources/org/raml/v2/parser/encodings/utf-16LE-bom/input.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/raml-java-parser/raml-parser-2/src/test/resources/org/raml/v2/parser/encodings/utf-32LE-bom/input.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/raml-java-parser/raml-parser-2/src/test/resources/org/raml/v2/parser/encodings/utf-16BE-bom/input.raml"
//  val apiPath: String = "/Users/lucas.block/mulesoft/raml-java-parser/raml-parser-2/src/test/resources/org/raml/v2/parser/encodings/utf-32BE-bom/input.raml"
//  val apiPath: String = "/Users/lucas.block/AWSDownloads/Unzipped/All/051db017-1d75-4870-b0ab-7de7a706fef5/89316a10-c6f7-45b7-8c0a-49126c29c35c/asset-mgmt-api.raml"
//  val apiPath: String = "/Users/lucas.block/AWSDownloads/Unzipped/All/13f02c97-c088-4831-8283-6f33da9aeb7e/9774361e-41c9-4e1a-b42c-0bc3f1ffbccf/experience.raml"
//  val apiPath: String = "/Users/lucas.block/AWSDownloads/Unzipped/All/02d4752f-9189-4410-89de-05c4b5623c5d/96b20a29-03ea-475d-a712-6cab8dfff168/api.raml"
//  val apiPath: String = "/Users/lucas.block/AWSDownloads/Unzipped/All/37a290c0-9ca7-4962-ac95-ec704cc6edb9/95dc1b27-e8bb-429e-960a-5851a3b8ce9c/main.raml"
//  val apiPath: String = "/Users/lucas.block/Downloads/fhir-api-specification-in-raml-1.0-1.0.0-fat-raml/healthcare-system-api.raml"
//  val apiPath: String = "/Users/lucas.block/AWSDownloads/Unzipped/All/b2d685e5-86c1-4452-88e7-ab38f2abf480/93b19882-ba9a-43a0-87a7-31e1c2dde989/quickstart-store.raml"
  val apiPath: String = "/Users/lucas.block/AWSDownloads/Unzipped/All/aa39dfd7-34c4-4714-9adb-198528694283/f8bb4306-ee34-4085-9806-c837f0b9ce2a/api.raml"

//  val apiKind: APIType = RAML10
  val apiKind: APIType = RAML08
//  val apiKind: APIType = OAS20

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
      RamlParsingValidationHelper.getValidationErrors(file, apiKind) match {
        case Right(r) =>
          if (r.isEmpty)
            println("Java Parser VALIDATION OK")
          else
            println(s"Java Parser Validations: $r}")
        case Left(e) => printAndThrow(s"Java Parser ERROR: ${e.getMessage}", e)
      }
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

    ExecutionLog.finish()
    ExecutionLog.buildReport
    println("\n\nFINISH OK")
  }

  private def printAndThrow(string: String, e: Throwable): Unit = {
    println(string)
    throw e
  }
}
