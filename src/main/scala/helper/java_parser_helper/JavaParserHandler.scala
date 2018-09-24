package helper.java_parser_helper

import org.raml.parser.visitor.RamlValidationService
import org.raml.v2.api.RamlModelBuilder

object JavaParserHandler {

  def getParserV2(rootDir: String): RamlModelBuilder = {
//    val custom = new CompositeResourceLoader(new DefaultResourceLoader, new RamlParserExchangeDependencyResourceLoader(rootDir))
//    new RamlModelBuilder(custom)
    new RamlModelBuilder()
  }

  def getValidatorServiceV1: RamlValidationService = {
    RamlValidationService.createDefault
  }
}
