package core

object APIS {

  sealed abstract class APIType(
      val label: String,
      val extension: String
  )

  case object RAML08 extends APIType("RAML08", ".raml")
  case object RAML10 extends APIType("RAML", ".raml")
  case object OAS20 extends APIType("OpenAPI", ".json")
  case object OAS30 extends APIType("OpenAPI3", ".json")
  case object JSON_LD extends APIType("JSON_LD", ".json")

}
