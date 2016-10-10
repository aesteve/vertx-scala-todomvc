package com.github.aesteve.vertx.scala.todomvc.routing

import io.vertx.scala.core.Vertx
import io.vertx.scala.core.http.HttpServerRequest
import io.vertx.scala.ext.web.Router

abstract class Routes(val vertx: Vertx) {

  lazy val router = Router.router(vertx)

  def attachRoutes(): Routes

  def apply(): (HttpServerRequest => Unit) = {
    attachRoutes()
    req => router.accept(req)
  }

}
