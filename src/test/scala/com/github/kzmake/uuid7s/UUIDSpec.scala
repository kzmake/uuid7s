package com.github.kzmake.uuid7s

import org.scalatest.BeforeAndAfter
import org.scalatest.freespec._
import org.scalatest.matchers._
import org.scalatest.prop._

import java.security.SecureRandom
import scala.util.Random

class UUIDSpec extends AnyFreeSpec with BeforeAndAfter with should.Matchers with TableDrivenPropertyChecks {
  before {
    Sequence.reset()
  }

  "UUID" - {
    ".generate" - {
      "UUIDを生成できる" in {
        (1 to 100000).map(_ => {
          val t = UUID.defaultTimestampFn()
          val s = UUID.defaultSequenceFn(t)
          val r = UUID.defaultRandomFn()

          val uuid = UUID.generate(tsFn = () => t, seqFn = _ => s, randFn = () => r)

          uuid.version should be(7)
          uuid.variant should be(2)
          uuid.timestamp should be(t / 1000)
          uuid.timestampMillis should be(t)
          uuid.sequence should be(s & UUID.seqM)
          uuid.random should be(r & UUID.randM)
        })
      }

      "UUIDを重複なく生成できる" in {
        val n     = 100000
        val uuids = (1 to n).map(_ => UUID.generate())
        uuids.size should be(uuids.distinct.size)
        uuids.size should be(n)
      }

      "同じ時刻・乱数でもUUIDを生成できる" in {
        (0 to 4095).map(i => {
          val uuid = UUID.generate(tsFn = () => 10L, randFn = () => 0L)
          val sss  = BigInt(i).toString(16).reverse.padTo(3, "0").reverse.mkString
          uuid.toString should be(s"00000000-000a-7$sss-8000-000000000000")
        })
      }

      "特定の時刻やシーケンス番号、乱数でUUIDを生成できる" in {
        forAll(
          Table(
            ("timestamp_msec", "seq", "random", "expected"),
            (0L, 0L, 0L, "00000000-0000-7000-8000-000000000000"),
            (1L, 1L, 1L, "00000000-0001-7001-8000-000000000001"),
            (0L, 4095L, 4611686018427387903L, "00000000-0000-7fff-bfff-ffffffffffff"),
            (68719476735999L, 0L, 4611686018427387903L, "ffffffff-f3e7-7000-bfff-ffffffffffff"),
            (68719476735999L, 4095L, 0L, "ffffffff-f3e7-7fff-8000-000000000000"),
            (68719476735999L, 4095L, 4611686018427387903L, "ffffffff-f3e7-7fff-bfff-ffffffffffff"),
            (0L, -1L, -1L, "00000000-0000-7fff-bfff-ffffffffffff"),
            (68719476736000L, 4096L, 4611686018427387904L, "00000000-0000-7000-8000-000000000000")
          )
        ) { (tsmsec: Long, seq: Long, random: Long, expected: String) =>
          UUID.generate(() => tsmsec, _ => seq, () => random).toString should be(expected)
        }
      }
    }

    ".parse" - {
      "生成したUUIDをパースできる" in {
        (1 to 10000).map(_ => {
          val expected = UUID.generate().toString

          UUID.parse(expected).isSuccess should be(true)
          UUID.parse(expected).get.toString should be(expected)
        })
      }

      "UUIDをパースできる" in {
        forAll(
          Table(
            ("hex", "expected"),
            ("00000000-0000-7000-bfff-ffffffffffff", "00000000-0000-7000-bfff-ffffffffffff"),
            ("00000000-0000-7000-8000-000000000000", "00000000-0000-7000-8000-000000000000"),
            ("00000000-03e7-7000-8000-000000000000", "00000000-03e7-7000-8000-000000000000"),
            ("00000000-10ea-7000-8000-0000000004d2", "00000000-10ea-7000-8000-0000000004d2"),
            ("ffffffff-f3e7-7000-bfff-ffffffffffff", "ffffffff-f3e7-7000-bfff-ffffffffffff"),
            ("ffffffff-f3e7-7000-8000-000000000000", "ffffffff-f3e7-7000-8000-000000000000"),
            ("061c30e5-5382-7000-b0ac-0fda4e2eb748", "061c30e5-5382-7000-b0ac-0fda4e2eb748"),
            ("061C30E5-5382-7000-B0AC-0FDA4E2EB748", "061c30e5-5382-7000-b0ac-0fda4e2eb748")
          )
        ) { (hex: String, expected: String) =>
          UUID.parse(hex).isSuccess should be(true)
          UUID.parse(hex).get.toString should be(expected)
        }
      }

      "UUIDではない文字列をパースできない" in {
        forAll(
          Table(
            "invalid",
            "00000000-0000-7000-bfff-fffffffffffff",
            "00000000-0000-7000-bfff-gggggggggggg",
            "0000000000007000bfffffffffffffff",
            "0000000000007000BFFFFFFFFFFFFFFF"
          )
        ) { (invalid: String) => UUID.parse(invalid).isFailure should be(true) }
      }
    }

    "#toString" - {
      "tttttttt-tttt-7sss-brrr-rrrrrrrrrrrr な形式でUUIDを生成できる" in {
        forAll(
          Table(
            ("timestamp_msec", "sequence", "random", "expected"),
            (0L, 0L, 0L, "00000000-0000-7000-8000-000000000000"),
            (999L, 0L, 0L, "00000000-03e7-7000-8000-000000000000"),
            (1234L, 0L, 1234L, "00000000-10ea-7000-8000-0000000004d2"),
            (1640173141898L, 0L, 3507195640103745352L, "061c30e5-5382-7000-b0ac-0fda4e2eb748"),
            (68719476735999L, 0L, 4611686018427387903L, "ffffffff-f3e7-7000-bfff-ffffffffffff")
          )
        ) { (tsmsec: Long, sequence: Long, random: Long, expected: String) =>
          val uuid = UUID.generate(() => tsmsec, _ => sequence, () => random)
          uuid.toString should be(expected)
        }
      }
    }

    "#timestamp" - {
      "生成したUUIDから生成時のタイムスタンプ[sec]を取得できる" in {
        val now = System.currentTimeMillis()

        forAll(
          Table(
            ("timestamp_msec", "expected"),
            (0L, 0L),
            (999L, 0L),
            (1000L, 1L),
            (1999L, 1L),
            (now, now / 1000),
            (68719476735000L, 68719476735L),
            (68719476735999L, 68719476735L)
          )
        ) { (tsmsec: Long, expected: Long) => UUID.generate(tsFn = () => tsmsec).timestamp should be(expected) }
      }
    }

    "#timestampMillis" - {
      "生成したUUIDから生成時のタイムスタンプ[msec]を取得できる" in {
        val now = System.currentTimeMillis()

        forAll(
          Table(
            ("timestamp_msec", "expected"),
            (0L, 0L),
            (999L, 999L),
            (1000L, 1000L),
            (1999L, 1999L),
            (now, now),
            (68719476735000L, 68719476735000L),
            (68719476735999L, 68719476735999L)
          )
        ) { (tsmsec: Long, expected: Long) => UUID.generate(tsFn = () => tsmsec).timestampMillis should be(expected) }
      }
    }

    "#random" - {
      "生成したUUIDから乱数を取得できる" in {
        val r: Long = new Random(new SecureRandom()).nextLong()

        forAll(
          Table(
            ("rand", "expected"),
            (0L, 0L),
            (1L, 1L),
            (1000L, 1000L),
            (r, r & UUID.randM),
            (1000000000000000000L, 1000000000000000000L),
            (4611686018427387903L, 4611686018427387903L)
          )
        ) { (rand: Long, expected: Long) => UUID.generate(randFn = () => rand).random should be(expected) }
      }
    }

    "#version" - {
      "生成したUUIDからバージョン情報を取得できる" in {
        (1 to 10000).foreach { _ => UUID.generate().version should be(7) }
      }
    }

    "#variant" - {
      "生成したUUIDからRFC4122のバリアント情報を取得できる" in {
        (1 to 10000).foreach { _ => UUID.generate().variant should be(2) }
      }
    }
  }
}
