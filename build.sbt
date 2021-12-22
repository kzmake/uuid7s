import Settings._

ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / semanticdbEnabled := true
ThisBuild / scalafixScalaBinaryVersion := CrossVersion.binaryScalaVersion(scalaVersion.value)
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision // only required for Scala 2.x
ThisBuild / scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Wunused:imports" // Scala 2.x only, required by `RemoveUnused`
)

lazy val root = (project in file("."))
  .settings(
    name := "uuid7s",
  )
  .settings(coreSettings)

