package com.github.aesteve.vertx.scala

import io.vertx.core.http.HttpMethod
import io.vertx.scala.core.MultiMap
import io.vertx.scala.ext.web.handler.CorsHandler
import io.vertx.scala.ext.web.{Route, Router, RoutingContext}

package object extensions {

  implicit class CorsHandlerExtension(cors: CorsHandler) {

    def allowedMethods(methods: Set[HttpMethod]): CorsHandler = {
      methods.foreach(cors.allowedMethod)
      cors
    }

    def allowedHeader(header: CharSequence): CorsHandler = {
      cors.allowedHeader(header.toString)
      cors
    }

  }


  implicit class RouterExtension(router: Router) {

    def all: (RoutingContext => Unit) => Route = router.route.handler

  }


  implicit class MultiMapExtension(map: MultiMap) {

    // Question : is there a way to avoid that, the toString() invokation on CharSequences is a mess to deal with
    // This should be fixed in vertx-core though
    // here we add some sugar to deal with HttpServerResponse as if it was a Map
    def +(header: (CharSequence, String)) = {
      map.add(header._1.toString, header._2.toString)
    }

  }


  implicit class RoutingContextExtension(routingContext: RoutingContext) {

    val payload = Option(routingContext.get("payload").asInstanceOf[Object]) // FIXME : else throws "cannot be cast to Nothing" at runtime, which I don't understand at all

    def setPayload[T <: AnyRef](payload: T): Unit = {
      routingContext.put("payload", payload)
      routingContext.next
    }
  }
  
}
