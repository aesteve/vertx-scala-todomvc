package com.github.aesteve.vertx.scala.todomvc

import io.vertx.scala.core.Vertx

object DemoLauncher extends App {

  Vertx.vertx().deployVerticle(classOf[TodoMVCServer].getName)

}
