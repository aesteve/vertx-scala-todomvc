package com.github.aesteve.vertx.scala.todomvc.domain

import com.github.aesteve.vertx.scala.todomvc.TodoMVCServer._

sealed case class TodoItem(
  var id: Option[String], // newly created todos won't have an id
  var title: Option[String],
  var order: Option[Int],
  var completed: Boolean = false
) {

  lazy val url = s"http://$HOST:$PORT/todos/${id.getOrElse("")}"

}
