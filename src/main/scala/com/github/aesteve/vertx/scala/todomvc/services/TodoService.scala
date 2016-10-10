package com.github.aesteve.vertx.scala.todomvc.services

import com.github.aesteve.vertx.scala.todomvc.domain.TodoItem

import scala.concurrent.Future

/**
  * Basic CRUD operations
  */
trait TodoService {

  def list(): Future[Iterable[TodoItem]]
  def create(todo: TodoItem): Future[TodoItem]
  def update(id: String, todo: TodoItem): Future[Option[TodoItem]]
  def retrieve(id: String): Future[Option[TodoItem]]
  def delete(id: String): Future[Option[TodoItem]]
  def clear(): Future[Iterable[TodoItem]]

}
