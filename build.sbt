import Settings._

ThisBuild / organization         := "io.github.kzmake"
ThisBuild / organizationHomepage := Some(url("https://github.com/kzmake"))
ThisBuild / versionScheme        := Some("early-semver")
ThisBuild / scmInfo := Some(ScmInfo(url("https://github.com/kzmake/uuid7s"), "scm:git@github.com:kzmake/uuid7s.git"))
ThisBuild / developers := List(
  Developer(
    id = "kzmake",
    name = "Kazuki Iwata",
    email = "kzmake.i3a@gmail.com",
    url = url("https://github.com/kzmake")
  )
)
ThisBuild / description                := "UUID version 7 in Scala."
ThisBuild / licenses                   := List("MIT License" -> new URL("https://opensource.org/licenses/MIT"))
ThisBuild / homepage                   := Some(url("https://github.com/kzmake/uuid7s"))
ThisBuild / pomIncludeRepository       := { _ => false }
ThisBuild / scalaVersion               := "2.13.6"
ThisBuild / semanticdbEnabled          := true
ThisBuild / scalafixScalaBinaryVersion := CrossVersion.binaryScalaVersion(scalaVersion.value)
ThisBuild / semanticdbVersion          := scalafixSemanticdb.revision // only required for Scala 2.x
ThisBuild / scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Wunused:imports" // Scala 2.x only, required by `RemoveUnused`
)

sonatypeCredentialHost := "s01.oss.sonatype.org"
sonatypeRepository     := "https://s01.oss.sonatype.org/service/local"

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
