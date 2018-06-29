package core

import amf._
import APIS._
import amf.client.AMF
import amf.client.parse._
import amf.client.render._
import amf.client.resolve._
import io.swagger.parser.SwaggerParser
import org.raml.v2.api.RamlModelBuilder

object ObjectsHandler {

  def getProfileName(kind: APIType): ProfileName = {
    kind match {
      case RAML10 => ProfileNames.RAML
      case RAML08 => ProfileNames.RAML08
      case OAS20 => ProfileNames.OAS
    }
  }

  def createParser(apiType: APIType): Parser = {
    apiType match {
      case RAML10 => AMF.raml10Parser()
      case RAML08 => AMF.raml08Parser()
      case OAS20 => AMF.oas20Parser()
      case JSON_LD => AMF.amfGraphParser()
      case _ => throw new IllegalArgumentException()
    }
  }

  def createGenerator(apiType: APIType): Renderer = {
    apiType match {
      case RAML10 => new Raml10Renderer()
      case RAML08 => new Raml08Renderer()
      case OAS20 => new Oas20Renderer()
      case JSON_LD => new AmfGraphRenderer()
      case _ => throw new IllegalArgumentException()
    }
  }

  def createResolver(apiType: APIType): Resolver = {
    apiType match {
      case RAML10 => new Raml10Resolver()
      case RAML08 => new Raml08Resolver()
      case OAS20 => new Oas20Resolver()
      case JSON_LD => new AmfGraphResolver()
      case _ => throw new IllegalArgumentException()
    }
  }

  def createJavaParserBuilder(): RamlModelBuilder = {
    new RamlModelBuilder
  }

  def createSwaggerParserBuilder(): SwaggerParser = {
    new SwaggerParser()
  }
}
