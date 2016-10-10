package com.github.aesteve.vertx.scala.todomvc.routing.impl

import com.github.aesteve.vertx.scala.todomvc.routing.{JacksonRoutes, Routes}
import io.vertx.core.http.HttpHeaders._
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.handler.{BodyHandler, CorsHandler}
import com.github.aesteve.vertx.scala.extensions._
import com.github.aesteve.vertx.scala.todomvc.domain.TodoItem
import com.github.aesteve.vertx.scala.todomvc.services.impl.TodoMapService
import io.vertx.core.http.HttpMethod.{DELETE, GET, OPTIONS, PATCH, POST, PUT}
import io.vertx.scala.core.http.HttpServerRequest

import scala.concurrent.ExecutionContext

sealed class TodoMVCRoutes (override val vertx: Vertx, implicit val context: ExecutionContext) extends JacksonRoutes(vertx = vertx) {

  lazy val mapService = TodoMapService()
  lazy val ALLOWED_METHODS = Set(GET, POST, PUT, PATCH, DELETE, OPTIONS)
  lazy val cors = CorsHandler
    .create("*")
    .allowedMethods(ALLOWED_METHODS)
    .allowedHeader(CONTENT_TYPE)

  override def attachRoutes(): Routes = {
    super.attachRoutes()
    // router.route.handler(cors) // FIXME : This doesn't work..., can't take an instance of Handler as parameter, necessarily a function / closure
    router.all(cors.handle)
    router.all(BodyHandler.create.handle)
    router.get("/todos").handler(ctx => {
      mapService.list() onSuccess { case list => ctx.setPayload(list) }
    })
    router.post("/todos").handler(ctx => {
      getBody(ctx, classOf[TodoItem]) match {
        case None => ctx.fail(400)
        case Some(todo) =>
          mapService.create(todo) onSuccess { case newTodo => ctx.setPayload(newTodo) }
      }
    })
    router.delete("/todos").handler(ctx => {
      mapService.clear() onSuccess { case list => ctx.setPayload(list) }
    })
    router.get("/todos/:id").handler(ctx => {
      val id = ctx.request.getParam("id").getOrElse("") // ":id" is written one line above. Maybe we could find another pattern in this case
      mapService.retrieve(id) onSuccess {
        case None => ctx.fail(404)
        case Some(todo) => ctx.setPayload(todo)
      }
    })
    router.patch("/todos/:id").handler(ctx => {
      val id = ctx.request.getParam("id").getOrElse("") // ":id" is written one line above. Maybe we could find another pattern in this case
      getBody(ctx, classOf[TodoItem]) match {
        case None => ctx.fail(400)
        case Some(todo) =>
          mapService.update(id, todo) onSuccess {
            case None => ctx.fail(404)
            case Some(newTodo) => ctx.setPayload(newTodo)
          }
      }
    })
    router.delete("/todos/:id").handler(ctx => {
      val id = ctx.request.getParam("id").getOrElse("") // ":id" is written one line above. Maybe we could find another pattern in this case
      mapService.delete(id) onSuccess {
        case None => ctx.fail(404)
        case Some(old) => ctx.setPayload(old)
      }
    })
    this
  }

}

object TodoMVCRoutes {

  def apply(vertx: Vertx, context: ExecutionContext): (HttpServerRequest => Unit) = {
    new TodoMVCRoutes(vertx, context).apply()
  }

}
