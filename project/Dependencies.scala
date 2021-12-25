import sbt._

object ScalaTest {
  val version = "3.2.10"
  val core    = "org.scalatest" %% "scalatest" % version
}

object UXID {
  val sulky       = "de.huxhorn.sulky"   % "de.huxhorn.sulky.ulid" % "8.3.0"
  val petitviolet = "net.petitviolet"    % "ulid4s_2.12"           % "0.5.0"
  val airframe    = "org.wvlet.airframe" % "airframe-ulid_2.13"    % "21.12.1"
  val chatwork    = "com.chatwork"       % "scala-ulid_2.13"       % "1.0.24"
}
