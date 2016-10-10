package com.github.aesteve.vertx.scala.todomvc.routing

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.github.aesteve.vertx.scala.extensions._
import io.vertx.core.http.HttpHeaders.CONTENT_TYPE
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.RoutingContext
import io.vertx.scala.ext.web.handler.BodyHandler

abstract class JacksonRoutes (override val vertx: Vertx) extends Routes(vertx =vertx) {

  private val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)


  override def attachRoutes(): Routes = {
    router.route.handler(setJsonContentType)
    router.route.handler(BodyHandler.create.handle)
    router.route.last.handler(sendPayloadAsJson)
    this
  }

  private val setJsonContentType: (RoutingContext => Unit) = ctx => {
    ctx.response.headers + (CONTENT_TYPE, "application/json")
    ctx.next
  }

  protected def getBody[T](ctx: RoutingContext, clazz: Class[T]): Option[T] = {
    ctx.getBodyAsString.map(body => mapper.readValue(body, clazz))
  }

  private val sendPayloadAsJson: (RoutingContext => Unit) = ctx => {
    ctx.payload match {
      case None => ctx.response.setStatusCode(204).end
      case Some(value) => ctx.response.end(mapper.writeValueAsString(value))
    }
  }

}
