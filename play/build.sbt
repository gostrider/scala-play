name := """play"""
organization := "erikcom"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies += filters
libraryDependencies ++= Seq(
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "org.scalaz" %% "scalaz-core" % "7.1.0"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "erikcom.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "erikcom.binders._"
