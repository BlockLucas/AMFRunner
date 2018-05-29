package helper.yaml_helper

import java.io.File

import common.FileUtils
import org.yaml.parser.YamlParser

object YamlParsingHelper {

  def handleParse(file: File): Either[Exception,  Unit] =  {
    try {
      YamlParser(FileUtils.readFileContent(file)).parse()
      Right()
    } catch {
      case e: Exception => Left(e)
    }
  }

}
