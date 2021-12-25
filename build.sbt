import Settings._

ThisBuild / version              := "0.1.0"
ThisBuild / organization         := "io.github.kzmake"
ThisBuild / organizationName     := "uuid7s"
ThisBuild / organizationHomepage := Some(url("https://github.com/kzmake"))
ThisBuild / versionScheme        := Some("semver-spec")
ThisBuild / scmInfo := Some(ScmInfo(url("https://github.com/kzmake/uuid7s"), "scm:git@github.com:kzmake/uuid7s.git"))
ThisBuild / developers := List(
  Developer(
    id = "kzmake",
    name = "Kazuki Iwata",
    email = "kzmake.i3a@gmail.com",
    url = url("https://github.com/kzmake")
  )
)
ThisBuild / description          := "UUID version 7 in Scala."
ThisBuild / licenses             := List("MIT License" -> new URL("https://opensource.org/licenses/MIT"))
ThisBuild / homepage             := Some(url("https://github.com/kzmake/uuid7s"))
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle          := true
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
