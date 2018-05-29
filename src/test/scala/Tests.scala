import java.io.File

import amf.client.AMF
import amf.client.parse.{Raml08Parser, Raml10Parser}
import amf.core.unsafe.PlatformSecrets
import org.scalatest._

class Tests extends FunSuite
  with Matchers
  with PlatformSecrets
  with BeforeAndAfterAll {

  val testDir = new File("vocabularies/src/test/resources/")

  test("test exclusuive") {
    AMF.init().get()
    val parser = new Raml10Parser()
    val testFile = new File("/Users/lucas.block/mulesoft/AMFScalaRunnerV2/run/testfile.raml")
    val baseUnit = parser.parseFileAsync("file://" + testFile.getAbsolutePath).get()
    println(baseUnit)
  }

  test("test stack") {
    AMF.init().get()
    val parser = new Raml08Parser()
    val baseUnit = parser.parseFileAsync("file:///Users/lucas.block/mulesoft/PlatformSnapshotAll-plus/problems/part3-Errors/org_1dec3fc1-ce76-4655-83b4-df9d6b546bc9/api_46065_ver_47765/api.raml").get()
    val result = AMF.validate(baseUnit, "RAML08", "RAML").get()
    println(result.conforms)
    assert(true)
  }

}
