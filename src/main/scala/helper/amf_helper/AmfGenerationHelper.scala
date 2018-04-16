package helper.amf_helper

import java.io.File

import amf.client.model.document.BaseUnit
import core.APIS.APIType
import core.ObjectsHandler

object AmfGenerationHelper {

  def handleGen(targetFile: File, targetKind: APIType, baseUnit: BaseUnit): Either[Throwable, Unit] = {
    try {
        val start = System.nanoTime()
        generate(targetFile, targetKind, baseUnit)
        val elapsed = (System.nanoTime() - start) / 1000000
        println(s"AMF.generateFile took $elapsed milliseconds")
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
