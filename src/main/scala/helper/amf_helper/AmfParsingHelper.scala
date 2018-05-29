package helper.amf_helper

import java.io.File

import amf.client.model.document.BaseUnit
import core.APIS.APIType
import core.ObjectsHandler

object AmfParsingHelper {

  def handleParse(file: File, kind: APIType): Either[Exception,  BaseUnit] =  {
    println("about to parse")
    val start = System.nanoTime()
    try {
        val baseUnit = parse(file, kind)
        println("parsed")
        val elapsed = (System.nanoTime() - start) / 1000000
        println(s"AMF.parseFileAsync took $elapsed milliseconds")
        Right(baseUnit)
    } catch {
      case e: Exception =>
        val elapsed = (System.nanoTime() - start) / 1000000
        println(s"AMF.parseFileAsync took $elapsed milliseconds")
        Left(e)
    }
  }

  private def parse(file: File, kind: APIType): BaseUnit = {
    val parser = ObjectsHandler.createParser(kind)
    parser.parseFileAsync("file://" + file.getPath).get()
  }
}
