package common

import java.io.{File, FileOutputStream, PrintWriter}
import java.nio.file.{Files, Paths, StandardOpenOption}

import scala.io.Source

object FileUtils {
  def readFileContent(filePath: String): String = readFileContent(new File(filePath))

  def readFileContent(file: File): String = Source.fromFile(file).getLines.mkString

  def readFileContentOptional(file: File): Option[String] = {
    try {
      Some(readFileContent(file))
    } catch {
      case _: Exception => None
    }
  }

  def writeFileContent(file: File, content: Object): Unit = {
    validatePath(file)
    var pw: PrintWriter = null
    try {
      pw = new PrintWriter(file)
      pw.write(content.toString)
    } catch {
      case e: Exception =>
    } finally {
      if (pw != null) { pw.close() }
    }
  }

  def appendDataToFile(file: File, data: String): Unit = {
    if (file.exists())
      Files.write(Paths.get(file.getAbsolutePath), data.getBytes(), StandardOpenOption.APPEND)
    else
      Files.write(Paths.get(file.getAbsolutePath), data.getBytes(), StandardOpenOption.CREATE)
  }

  def validatePath(file: File): Unit = {
    val filePath = file.getAbsoluteFile.getParentFile
    if (!filePath.exists()) filePath.mkdirs()
  }

}
