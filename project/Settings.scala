import sbt.Keys._
import sbt._

object Settings {
  lazy val scalaVersionsJVM    = Seq("2.13.6", "2.12.15", "2.11.12", "3.1.0")
  lazy val scalaVersionsNative = Seq("2.13.6", "2.12.15", "2.11.12")
  lazy val scalaVersionsJS     = Seq("2.13.6", "2.12.15", "2.11.12", "3.1.0")

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
    )
  )
  val libSettings: Def.SettingsDefinition = Seq(
    name           := "uuid7s",
    publish / skip := false,
    libraryDependencies ++= Seq(
      ScalaTest.core % Test
    )
  )
  val jvmSettings: Def.SettingsDefinition = Seq(
    crossScalaVersions := scalaVersionsJVM,
    scalaVersion       := scalaVersionsJVM.head
  )
  val nativeSettings: Def.SettingsDefinition = Seq(
    crossScalaVersions := scalaVersionsNative,
    scalaVersion       := scalaVersionsNative.head
  )
  val jsSettings: Def.SettingsDefinition = Seq(
    crossScalaVersions := scalaVersionsJS,
    scalaVersion       := scalaVersionsJS.head
  )
  val benchmarkSettings: Def.SettingsDefinition = Seq(
    name           := "uuid7s-benchmark",
    publish / skip := true,
    libraryDependencies ++= Seq(
      UXID.sulky,
      UXID.petitviolet,
      UXID.airframe,
      UXID.chatwork
    )
  )
  val cliSettings: Def.SettingsDefinition = Seq(
    name           := "uuid7s-cli",
    publish / skip := true,
    libraryDependencies ++= Seq(
      Scallop.core
    )
  )
}
