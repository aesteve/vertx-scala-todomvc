package com.github.aesteve.vertx.scala.todomvc.services.impl

import java.util.UUID

import com.github.aesteve.vertx.scala.todomvc.domain.TodoItem
import com.github.aesteve.vertx.scala.todomvc.services.TodoService

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global // FIXME : use the verticle instead, how could we use define it within the scope ?

class TodoMapService() extends TodoService {

  val todos: mutable.Map[String, TodoItem] = mutable.Map()

  override def list(): Future[Iterable[TodoItem]] = {
    Future(todos.values)
  }

  override def create(todo: TodoItem): Future[TodoItem] = {
    val newId = UUID.randomUUID().toString.replaceAll("-", "")
    todo.id = Some(newId)
    todos(newId) = todo
    Future(todo)
  }

  override def update(id: String, todo: TodoItem): Future[Option[TodoItem]] = {
    todo.id = Some(id)
    todos.get(id) match {
      case None => Future(None)
      case Some(old) => Future({
        todos.put(id, todo)
        Some(todo)
      })
    }
  }

  override def retrieve(id: String): Future[Option[TodoItem]] = {
    Future(todos.get(id))
  }

  override def delete(id: String): Future[Option[TodoItem]] = {
    Future(todos.remove(id))
  }

  override def clear(): Future[Iterable[TodoItem]] = {
    Future({
      todos.clear()
      todos.values
    })
  }
}

object TodoMapService {
  def apply() = new TodoMapService()
}
