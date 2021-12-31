package io.github.kzmake.uuid7s

import org.rogach.scallop._

import java.time._

case class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  // generate
  val n = opt[Int](default = Some(1), validate = _ > 0)

  // parse
  val local  = opt[Boolean](default = Some(false))
  val uuidv7 = trailArg[String](required = false)

  verify()
}

object Main extends App {
  val conf = Conf(args.toSeq)
  conf.uuidv7.toOption match {
    case None => (1 to conf.n.apply()).foreach(_ => println(UUID.generate().toString))
    case Some(v) =>
      val uuid    = UUID.parse(v).get
      val instant = Instant.ofEpochSecond(uuid.timestamp, uuid.msec * 1_000_000)
      if (conf.local.apply()) println(OffsetDateTime.ofInstant(instant, ZoneId.systemDefault()))
      else println(OffsetDateTime.ofInstant(instant, ZoneOffset.UTC))
  }
}
