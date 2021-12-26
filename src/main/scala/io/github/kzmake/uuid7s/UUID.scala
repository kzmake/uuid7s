package io.github.kzmake.uuid7s

import java.security.SecureRandom
import scala.util.{Random, Try}

final case class UUID(self: java.util.UUID) {
  def timestamp: Long       = uxts
  def timestampMillis: Long = uxts * 1000 + msec

  private def up: Long   = self.getMostSignificantBits
  private def down: Long = self.getLeastSignificantBits

  def uxts: Long     = (up >>> UUID.uxtsO) & UUID.uxtsM
  def msec: Long     = (up >>> UUID.msecO) & UUID.msecM
  def version: Int   = self.version()
  def sequence: Long = (up >>> UUID.seqO) & UUID.seqM
  def variant: Int   = self.variant()
  def random: Long   = (down >>> UUID.randO) & UUID.randM

  def compare(that: UUID): Int  = self.compareTo(that.self)
  override def hashCode: Int    = self.hashCode
  override def toString: String = self.toString
}

object UUID {

  /** Generate UUID(version 7).
    *
    * {{{
    * // 061c3289-2240-7000-9be2-ff62684af0f
    * val uuid = UUID.generate().toString
    *
    * // 00000000-0000-7000-8000-000000000000
    * val min = UUID.generate(() => 0L, _ => 0L, () => 0L).toString
    * // ffffffff-f3e7-7000-bfff-ffffffffffff
    * val max = UUID.generate(() => 68719476735999L, _ => 4095L, () => 4611686018427387903L).toString
    * }}}
    *
    * @return
    *   Generated UUID(version 7)
    */
  def generate(
      tsFn: () => Long = UUID.defaultTimestampFn,
      seqFn: Long => Long = defaultSequenceFn,
      randFn: () => Long = UUID.defaultRandomFn
  ): UUID = {
    val ts: Long = tsFn()

    val uxts: Long = (ts / 1000) & uxtsM // [0 68719476735] (1970-01-01 00:00:00 ~ 4147-08-20 07:32:15)
    val msec: Long = (ts % 1000) & msecM //[0 4095]
    val seq: Long  = seqFn(ts) & seqM // [0 4095]
    val rand: Long = randFn() & randM // [0 4611686018427387903]

    val up: Long   = (uxts << uxtsO) | (msec << msecO) | (version << verO) | seq
    val down: Long = (variant << varO) | rand

    UUID(new java.util.UUID(up, down))
  }

  def parse(uuid: String): Try[UUID] = Try {
    UUID(java.util.UUID.fromString(uuid))
  }

  val defaultTimestampFn: () => Long  = () => System.currentTimeMillis()
  val defaultSequenceFn: Long => Long = ts => Sequence.get(ts)
  val defaultRandomFn: () => Long = {
    val r: Random = new Random(new SecureRandom())
    () => r.nextLong()
  }

  private[uuid7s] val uxtsM: Long = 0xfffffffffL
  private[uuid7s] val msecM: Long = 0xfffL
  private[uuid7s] val verM: Long  = 0xfL
  private[uuid7s] val seqM: Long  = 0xfffL
  private[uuid7s] val varM: Long  = 0x3L
  private[uuid7s] val randM: Long = 0x3fffffffffffffffL

  private[uuid7s] val uxtsO: Int = (msecM.toBinaryString + verM.toBinaryString + seqM.toBinaryString).length // 28 bits
  private[uuid7s] val msecO: Int = (verM.toBinaryString + seqM.toBinaryString).length                        // 16 bits
  private[uuid7s] val verO: Int  = seqM.toBinaryString.length                                                // 12 bits
  private[uuid7s] val seqO: Int  = 0
  private[uuid7s] val varO: Int  = randM.toBinaryString.length                                               // 62 bits
  private[uuid7s] val randO: Int = 0

  val version: Long = 7 & verM
  val variant: Long = 2 & varM // RFC4122
}
