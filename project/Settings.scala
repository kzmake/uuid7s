import sbt.Keys._
import sbt._

object Settings {
  val coreSettings: Def.SettingsDefinition = Seq(
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-unchecked",
      "-encoding",
      "UTF-8",
      "-Xfatal-warnings",
      "-language:_",
      // Warn if an argument list is modified to match the receiver
      // "-Ywarn-adapted-args",
      // Warn when dead code is identified.
      "-Ywarn-dead-code",
      // Warn about inaccessible types in method signatures.
      // "-Ywarn-inaccessible",
      // Warn when a type argument is inferred to be `Any`.
      // "-Ywarn-infer-any",
      // Warn when non-nullary `def f()' overrides nullary `def f'
      // "-Ywarn-nullary-override",
      // Warn when nullary methods return Unit.
      // "-Ywarn-nullary-unit",
      // Warn when numerics are widened.
      "-Ywarn-numeric-widen"
      // Warn when imports are unused.
      // "-Ywarn-unused-import"
    ),
    libraryDependencies ++= Seq(
      ScalaTest.core % Test
    )
  )
}
