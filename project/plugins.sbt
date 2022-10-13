addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.17")
addSbtPlugin("org.foundweekends.giter8" % "sbt-giter8-scaffold" % "0.13.1")

libraryDependencies += "org.postgresql" % "postgresql" % "42.5.0"
addSbtPlugin("org.scalikejdbc" % "scalikejdbc-mapper-generator" % "3.5.0")