package com.github.aesteve.vertx.scala.todomvc

import com.github.aesteve.vertx.scala.todomvc.routing.impl.TodoMVCRoutes
import io.vertx.core.Future
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.lang.scala.json.Json
import io.vertx.scala.core.http.HttpServerOptions
import io.vertx.scala.core.http.HttpServerOptions.HttpServerOptionsJava

import scala.util.{Failure, Success}


class TodoMVCServer extends ScalaVerticle {

  lazy val options = createOptions()
  lazy val server = vertx.createHttpServer(options)

  override def start(future: Future[Void]): Unit = {
    server
      .requestHandler(TodoMVCRoutes(vertx, executionContext))
      .listenFuture(options.getPort, options.getHost).onComplete {
        case Success(_) => future.complete()
        case Failure(cause) => future.fail(cause)
      }
  }

  override def stop(future: Future[Void]): Unit = {
    server.closeFuture().onComplete(_ => future.complete()) // can't use future.completer() here, obviously
  }

  private def createOptions(): HttpServerOptions = {
    HttpServerOptions(new HttpServerOptionsJava(Json.emptyObj()))
      .setHost(TodoMVCServer.HOST)
      .setPort(TodoMVCServer.PORT)
  }

}

object TodoMVCServer {
  val PORT = 8181
  val HOST = "localhost"
}
