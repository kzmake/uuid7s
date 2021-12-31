import sbt.Keys._
import sbt._

object Settings {
  val scalaVersion2_11 = "2.11.12"
  val scalaVersion2_12 = "2.12.15"
  val scalaVersion2_13 = "2.13.6"
  val scalaVersion3    = "3.1.0"

  val coreSettings: Def.SettingsDefinition = Seq(
    scalacOptions ++= Seq(
      "-feature",
      // "-deprecation",
      // "-unchecked",
      "-encoding",
      "UTF-8",
      "-Xfatal-warnings",
      "-language:_"
      // Warn if an argument list is modified to match the receiver
      // "-Ywarn-adapted-args",
      // Warn when dead code is identified.
      // "-Ywarn-dead-code",
      // Warn about inaccessible types in method signatures.
      // "-Ywarn-inaccessible",
      // Warn when a type argument is inferred to be `Any`.
      // "-Ywarn-infer-any",
      // Warn when non-nullary `def f()' overrides nullary `def f'
      // "-Ywarn-nullary-override",
      // Warn when nullary methods return Unit.
      // "-Ywarn-nullary-unit",
      // Warn when numerics are widened.
      // "-Ywarn-numeric-widen"
      // Warn when imports are unused.
      // "-Ywarn-unused-import"
    ),
    libraryDependencies ++= Seq(
      ScalaTest.core % Test
    )
  )
  val libSettings: Def.SettingsDefinition = Seq(
    publish / skip     := false,
    crossScalaVersions := Seq(scalaVersion2_11, scalaVersion2_12, scalaVersion2_13, scalaVersion3)
  )
  val benchmarkSettings: Def.SettingsDefinition = Seq(
    publish / skip := true,
    libraryDependencies ++= Seq(
      UXID.sulky,
      UXID.petitviolet,
      UXID.airframe,
      UXID.chatwork
    )
  )
  val cliSettings: Def.SettingsDefinition = Seq(
    publish / skip := true,
    libraryDependencies ++= Seq(
      Scallop.core
    )
  )
}
