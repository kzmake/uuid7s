import sbt._

object ScalaTest {
  val version = "3.2.10"
  val core    = "org.scalatest" %% "scalatest" % version
}

object UXID {
  val sulky       = "de.huxhorn.sulky"   % "de.huxhorn.sulky.ulid" % "8.3.0"
  val petitviolet = "net.petitviolet"    %% "ulid4s"           % "0.5.0"
  val airframe    = "org.wvlet.airframe" %% "airframe-ulid"    % "21.12.1"
  val chatwork    = "com.chatwork"       %% "scala-ulid"       % "1.0.24"
}

object Scallop {
  val version = "4.1.0"
  val core    = "org.rogach" %% "scallop" % version
}
