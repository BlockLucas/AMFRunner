package helper.amf_helper

import amf.client.model.document.BaseUnit
import amf.client.resolve.Resolver
import core.APIS.APIType
import core.ObjectsHandler

object AmfResolutionHelper {

  def handleResolution(kind: APIType, baseUnit: BaseUnit): Either[Throwable, BaseUnit] = {
    println("about to resolve")
    try {
        val start = System.nanoTime()
        val b = resolve(baseUnit, kind)
        val elapsed = (System.nanoTime() - start) / 1000000
        println(s"AMF.resolve took $elapsed milliseconds")
        println("resolved")
        Right(b)
    } catch {
      case s: StackOverflowError => Left(s)
      case e: Exception => Left(e)
    }
  }

  private def resolve(baseUnit: BaseUnit, kind: APIType): BaseUnit = {
    val resolver: Resolver = ObjectsHandler.createResolver(kind)
    resolver.resolve(baseUnit)
  }

}
