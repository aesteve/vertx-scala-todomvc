# TodoMVC (server) implementation written in Scala using Vert.x

## Temporary setup

Build `vertx-lang-scala-stack` locally so that you can add `vertx-web-scala` as a dependency

Read the `TODO` / `FIXME` comments to have an idea of what I'm missing, or simply what I am not experimented enough to code properly

Test the app using [The todo backend spec checker](http://www.todobackend.com/specs/index.html?http://localhost:8181/todos) (unit tests to come)


------------- 
Original README

#Getting vertx-lang-scala

The current version in master is deployed as SNAPSHOTS in a sonatype repo (which is the default one configured fir this build)

#Get the vertx-modules

There is a separate project for generating other vertx-modules (vertx-web, vertx-auth, ...). It's currently untested but most modules should work.
If you want to try them out clone and build: [Vert.x Scala Lang Stack](https://github.com/codepitbull/vertx-lang-scala-stack)

#Work with this project

Create a runnable fat-jar
```
sbt assembly
```

play around in sbt
```
sbt
> console
scala> vertx.deployVerticle(classOf[DemoVerticle].getName)
scala> vertx.deploymentIDs
```

From here you can freely interact with the Vertx-API inside the sbt-scala-shell.
