package helper.amf_helper

import java.io.File

import amf.model.document.BaseUnit
import core.APIS.APIType
import core.ObjectsHandler

object AmfGenerationHelper {

  def handleGen(targetFile: File, targetKind: APIType, baseUnit: BaseUnit): Either[Throwable, Unit] = {
    try {
        generate(targetFile, targetKind, baseUnit)
        Right()
    } catch {
      case s: StackOverflowError => Left(s)
      case e: Exception => Left(e)
    }

  }

  private def generate(target: File, kind: APIType, baseUnit: BaseUnit): Unit = {
    val generator = ObjectsHandler.createGenerator(kind)
    generator.generateFile(baseUnit, target).get()
  }

}
