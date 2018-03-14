import java.io.File

import amf.Raml10Parser
import amf.core.unsafe.PlatformSecrets
import amf.model.document.Document
import org.scalatest._

class Tests extends FunSuite
  with Matchers
  with PlatformSecrets
  with BeforeAndAfterAll {

  val testDir = new File("vocabularies/src/test/resources/")

  test("test exclusuive") {
    amf.AMF.init().get()
    val parser = new Raml10Parser()
    val testFile = new File("/Users/lucas.block/mulesoft/AMFScalaRunnerV2/run/testfile.raml")
    val baseUnit = parser.parseFileAsync("file://" + testFile.getAbsolutePath).get()
    println(baseUnit)

  }

}
