package helper.java_parser_helper

import helper.resource.loader.RamlParserExchangeDependencyResourceLoader
import org.raml.parser.visitor.RamlValidationService
import org.raml.v2.api.RamlModelBuilder
import org.raml.v2.api.loader.{CompositeResourceLoader, DefaultResourceLoader}

object JavaParserHandler {

  def getParserV2(rootDir: String): RamlModelBuilder = {
    val custom = new CompositeResourceLoader(new DefaultResourceLoader, new RamlParserExchangeDependencyResourceLoader(rootDir))
    new RamlModelBuilder(custom)
  }

  def getValidatorServiceV1: RamlValidationService = {
    RamlValidationService.createDefault
  }
}
