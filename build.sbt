import Settings._

ThisBuild / version                    := "0.1.0"
ThisBuild / scalaVersion               := "2.13.6"
ThisBuild / semanticdbEnabled          := true
ThisBuild / scalafixScalaBinaryVersion := CrossVersion.binaryScalaVersion(scalaVersion.value)
ThisBuild / semanticdbVersion          := scalafixSemanticdb.revision // only required for Scala 2.x
ThisBuild / scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Wunused:imports" // Scala 2.x only, required by `RemoveUnused`
)

lazy val root = (project in file("."))
  .settings(
    name := "uuid7s"
  )
  .settings(coreSettings)

lazy val benchmark = (project in file("./benchmark"))
  .settings(
    name := "uuid7s-benchmark"
  )
  .settings(coreSettings, benchmarkSettings)
  .enablePlugins(JmhPlugin)
  .dependsOn(root)

addCommandAlias("benchmark", ";project benchmark ;Jmh / compile ;Jmh / run -i 3 -wi 3 -f1 -t1")
