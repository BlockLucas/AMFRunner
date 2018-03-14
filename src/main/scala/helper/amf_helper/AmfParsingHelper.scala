package helper.amf_helper

import java.io.File

import amf.AMF
import amf.core.client.ParsingOptions
import amf.core.remote.JvmPlatform
import amf.model.document.BaseUnit
import core.APIS.APIType
import core.ObjectsHandler

object AmfParsingHelper {

  def handleParse(file: File, kind: APIType): Either[Exception,  BaseUnit] =  {
    println("about to parse")
    try {
        val baseUnit = parse(file, kind)
        println("parsed")
        Right(baseUnit)
    } catch {
      case e: Exception => Left(e)
    }
  }

  private def parse(file: File, kind: APIType): BaseUnit = {
    val parser = ObjectsHandler.createParser(kind)
    parser.parseFileAsync("file://" + file.getPath, JvmPlatform.instance(), new ParsingOptions()).get()
  }
}
