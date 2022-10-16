name := """PAMIW-2"""
organization := "lthv"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.9"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "42.5.0",
  "org.scalikejdbc" %% "scalikejdbc" % "3.5.0",
  "org.scalikejdbc" %% "scalikejdbc-config" % "3.5.0",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.8.0-scalikejdbc-3.5",
  "com.typesafe.slick" %% "slick-codegen" % "3.4.1",
  "com.typesafe.slick" %% "slick" % "3.4.0",
  "io.github.nafg.slick-migration-api" %% "slick-migration-api" % "0.9.0"
)

enablePlugins(ScalikejdbcPlugin)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "lthv.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "lthv.binders._"
