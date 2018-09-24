package helper.amf_helper

import amf.AMFStyle
import amf.client.AMF
import amf.client.model.document.BaseUnit
import amf.client.validate.{ValidationReport, ValidationResult}
import core.APIS.APIType
import core.ObjectsHandler


object AmfValidationHelper {

  def handleValidation(kind: APIType, baseUnit: BaseUnit): Either[Throwable, ValidationReport] = {
    try {
      val start = System.nanoTime()
      val report = validate(kind, baseUnit)
      val elapsed = (System.nanoTime() - start) / 1000000
      println(s"AMF.validate took $elapsed milliseconds")
      Right(report)
    } catch {
      case s: StackOverflowError => Left(s)
      case e: Exception => Left(e)
    }
  }

  private def validate(kind: APIType, baseUnit: BaseUnit): ValidationReport = {
    println("about to validate")
    val profile = ObjectsHandler.getProfileName(kind)
    AMF.validate(baseUnit, profile, profile.messageStyle).get()
  }

  def handleValidationResults(amfResults: List[ValidationResult]): String = {
    val resultsGroups = amfResults.groupBy(_.level)
    s"""|Violations:
        |${resultsGroups.filter(_._1 == "Violation").flatMap(_._2).map(e => "Message: " + e.message + "; Position: " + e.position + "; location: " + e.location).mkString("/")}
        |Warnings:
        |${resultsGroups.filter(_._1 == "Warning").flatMap(_._2).map(e => "Message: " + e.message + "; Position: " + e.position).mkString("/")}"""
  }

}
