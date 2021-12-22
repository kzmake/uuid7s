package kzmake.uuid7s

import java.nio.ByteBuffer
import java.security.SecureRandom
import scala.util.{Random, Try}

final case class UUIDv7(bytes: Array[Byte]) {
  def timestamp: Long       = uxts
  def timestampMillis: Long = uxts * 1000 + msec

  private def up: Long   = BigInt(bytes.slice(0, 8)).toLong
  private def down: Long = BigInt(bytes.slice(8, 16)).toLong

  lazy val uxts: Long     = (up >>> UUIDv7.uxtsO) & UUIDv7.uxtsM
  lazy val msec: Long     = (up >>> UUIDv7.msecO) & UUIDv7.msecM
  lazy val version: Long  = (up >>> UUIDv7.verO) & UUIDv7.verM
  lazy val sequence: Long = (up >>> UUIDv7.seqM) & UUIDv7.seqM
  lazy val variant: Long  = (down >>> UUIDv7.varO) & UUIDv7.varM
  lazy val random: Long   = (down >>> UUIDv7.randO) & UUIDv7.randM

  /** @return
    *   The UUID string ex: 061c30e5-5382-7000-b0ac-0fda4e2eb748
    */
  override def toString: String = Hex.valueOf(bytes.slice(0, 4)) +
    "-" + Hex.valueOf(bytes.slice(4, 6)) +
    "-" + Hex.valueOf(bytes.slice(6, 8)) +
    "-" + Hex.valueOf(bytes.slice(8, 10)) +
    "-" + Hex.valueOf(bytes.slice(10, 16))
}

object UUIDv7 {

  /** Generate UUID v7.
    * {{{
    * //  0                   1                   2                   3
    * //  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
    * //  ┌─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┐
    * //  │                             uxts                              │
    * //  ├─┴─┴─┴─┼─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┼─┴─┴─┴─┼─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┤
    * //  │ uxts  │         msec          │  ver  │          seq          │
    * //  ├─┴─┼─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┤
    * //  │var│                         rand                              │
    * //  ├─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┤
    * //  │                             rand                              │
    * //  └─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┘
    *
    * // 061c3289-2240-7000-9be2-ff62684af0f
    * val uuid = UUIDv7.generate().toString
    *
    * // 00000000-0000-7000-8000-000000000000
    * val min = UUIDv7.generate(() => 0L, () => 0L).toString
    * // ffffffff-f3e7-7000-bfff-ffffffffffff
    * val max = UUIDv7.generate(() => 68719476735999L, () => 4611686018427387903L).toString
    * }}}
    *
    * @return
    *   Generated UUID v7
    */
  def generate(
      tsFn: () => Long = UUIDv7.defaultTimestampFn,
      randFn: () => Long = UUIDv7.defaultRandomFn
  ): UUIDv7 = {
    val ts: Long = tsFn()

    val uxts: Long = (ts / 1000) & uxtsM // [0 68719476735] (1970-01-01 00:00:00 ~ 4147-08-20 07:32:15)
    val msec: Long = (ts % 1000) & msecM //[0 4095]
    val seq: Long  = Sequence.get(ts) & seqM // [0 4095]
    val rand: Long = randFn() & randM // [0 4611686018427387903]

    val up: Long   = (uxts << uxtsO) | (msec << msecO) | (version << verO) | seq
    val down: Long = (variant << varO) | rand

    UUIDv7(
      ByteBuffer.allocate(8).putLong(up).array()
        ++ ByteBuffer.allocate(8).putLong(down).array()
    )
  }

  def parse(uuidv7: String): Try[UUIDv7] = Try {
    val hex = uuidv7.replaceAll("-", "").toLowerCase
    require(hex.length == 32, s"invalid uuid: $uuidv7")

    UUIDv7(Hex.toBytes(hex))
  }

  val defaultTimestampFn: () => Long = () => System.currentTimeMillis()
  val defaultRandomFn: () => Long = {
    val r: Random = new Random(new SecureRandom())
    () => r.nextLong()
  }

  val uxtsM: Long = 0xfffffffffL
  val msecM: Long = 0xfffL
  val verM: Long  = 0xfL
  val seqM: Long  = 0xfffL
  val varM: Long  = 0x3L
  val randM: Long = 0x3fffffffffffffffL

  val uxtsO: Int = (msecM.toBinaryString + verM.toBinaryString + seqM.toBinaryString).length // 28 bits
  val msecO: Int = (verM.toBinaryString + seqM.toBinaryString).length                        // 16 bits
  val verO: Int  = seqM.toBinaryString.length                                                // 12 bits
  val seqO: Int  = 0
  val varO: Int  = randM.toBinaryString.length                                               // 62 bits
  val randO: Int = 0

  val version: Long = 7 & verM
  val variant: Long = 2 & varM // RFC4122
}
