package com.github.kzmake.uuid7s

import org.scalatest.freespec._
import org.scalatest.matchers._
import org.scalatest.prop._

import java.security.SecureRandom
import scala.util.Random

class UUIDv7Spec extends AnyFreeSpec with should.Matchers with TableDrivenPropertyChecks {
  "UUIDv7" - {
    ".generate" - {
      "UUIDv7を生成できる" in {
        (1 to 100000).map(_ => {
          val t = UUIDv7.defaultTimestampFn()
          val r = UUIDv7.defaultRandomFn()

          val uuidv7 = UUIDv7.generate(() => t, () => r)

          uuidv7.version should be(7)
          uuidv7.variant should be(2)
          uuidv7.timestamp should be(t / 1000)
          uuidv7.timestampMillis should be(t)
          uuidv7.random should be(r & UUIDv7.randM)
        })
      }

      "UUIDv7を重複なく生成できる" in {
        val n       = 100000
        val uuidv7s = (1 to n).map(_ => UUIDv7.generate())
        uuidv7s.size should be(uuidv7s.distinct.size)
        uuidv7s.size should be(n)
      }

      "同一時刻でもUUIDv7を生成できる" in {
        forAll(
          Table(
            ("timestamp_msec", "random", "expected"),
            (10L, 0L, "00000000-000a-7000-8000-000000000000"),
            (10L, 0L, "00000000-000a-7001-8000-000000000000"),
            (10L, 0L, "00000000-000a-7002-8000-000000000000"),
            (10L, 0L, "00000000-000a-7003-8000-000000000000"),
            (10L, 0L, "00000000-000a-7004-8000-000000000000"),
            (10L, 0L, "00000000-000a-7005-8000-000000000000")
          )
        ) { (tsmsec: Long, random: Long, expected: String) =>
          UUIDv7.generate(() => tsmsec, () => random).toString should be(expected)
        }
      }

      "特定の時刻や乱数でUUIDv7を生成できる" in {
        forAll(
          Table(
            ("timestamp_msec", "random", "expected"),
            (0L, -1L, "00000000-0000-7000-bfff-ffffffffffff"),
            (1L, 0L, "00000000-0001-7000-8000-000000000000"),
            (999L, 0L, "00000000-03e7-7000-8000-000000000000"),
            (1234L, 1234L, "00000000-10ea-7000-8000-0000000004d2"),
            (1640173141898L, 3507195640103745352L, "061c30e5-5382-7000-b0ac-0fda4e2eb748"),
            (68719476735998L, 4611686018427387903L, "ffffffff-f3e6-7000-bfff-ffffffffffff"),
            (68719476735999L, 4611686018427387904L, "ffffffff-f3e7-7000-8000-000000000000")
          )
        ) { (tsmsec: Long, random: Long, expected: String) =>
          UUIDv7.generate(() => tsmsec, () => random).toString should be(expected)
        }
      }
    }

    ".parse" - {
      "生成したUUIDv7をパースできる" in {
        (1 to 10000).map(_ => {
          val expected = UUIDv7.generate().toString

          UUIDv7.parse(expected).isSuccess should be(true)
          UUIDv7.parse(expected).get.toString should be(expected)
        })
      }

      "UUIDv7をパースできる" in {
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
            ("061C30E5-5382-7000-B0AC-0FDA4E2EB748", "061c30e5-5382-7000-b0ac-0fda4e2eb748"),
            ("061c30e553827000b0ac0fda4e2eb748", "061c30e5-5382-7000-b0ac-0fda4e2eb748"),
            ("061C30E553827000B0AC0FDA4E2EB748", "061c30e5-5382-7000-b0ac-0fda4e2eb748")
          )
        ) { (hex: String, expected: String) =>
          UUIDv7.parse(hex).isSuccess should be(true)
          UUIDv7.parse(hex).get.toString should be(expected)
        }
      }

      "UUIDv7ではない文字列をパースできない" in {
        forAll(
          Table(
            "invalid",
            "00000000-0000-7000-bfff-fffffffffff",
            "00000000-0000-7000-bfff-fffffffffffff",
            "00000000-0000-7000-bfff-ggggggggggg"
          )
        ) { (invalid: String) => UUIDv7.parse(invalid).isFailure should be(true) }
      }
    }

    "#toString" - {
      "tttttttt-tttt-7sss-brrr-rrrrrrrrrrrr な形式でUUIDv7を生成できる" in {
        forAll(
          Table(
            ("timestamp_msec", "random", "expected"),
            (0L, 0L, "00000000-0000-7000-8000-000000000000"),
            (999L, 0L, "00000000-03e7-7000-8000-000000000000"),
            (1234L, 1234L, "00000000-10ea-7000-8000-0000000004d2"),
            (1640173141898L, 3507195640103745352L, "061c30e5-5382-7000-b0ac-0fda4e2eb748"),
            (68719476735999L, 4611686018427387903L, "ffffffff-f3e7-7000-bfff-ffffffffffff")
          )
        ) { (tsmsec: Long, random: Long, expected: String) =>
          UUIDv7.generate(() => tsmsec, () => random).toString should be(expected)
        }
      }
    }

    "#timestamp" - {
      "生成したUUIDv7から生成時のタイムスタンプ[sec]を取得できる" in {
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
        ) { (tsmsec: Long, expected: Long) => UUIDv7.generate(tsFn = () => tsmsec).timestamp should be(expected) }
      }
    }

    "#timestampMillis" - {
      "生成したUUIDv7から生成時のタイムスタンプ[msec]を取得できる" in {
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
        ) { (tsmsec: Long, expected: Long) => UUIDv7.generate(tsFn = () => tsmsec).timestampMillis should be(expected) }
      }
    }

    "#random" - {
      "生成したUUIDv7から乱数を取得できる" in {
        val r: Long = new Random(new SecureRandom()).nextLong()

        forAll(
          Table(
            ("rand", "expected"),
            (0L, 0L),
            (1L, 1L),
            (1000L, 1000L),
            (r, r & UUIDv7.randM),
            (1000000000000000000L, 1000000000000000000L),
            (4611686018427387903L, 4611686018427387903L)
          )
        ) { (rand: Long, expected: Long) => UUIDv7.generate(randFn = () => rand).random should be(expected) }
      }
    }

    "#version" - {
      "生成したUUIDv7からバージョン情報を取得できる" in {
        (1 to 10000).foreach { _ => UUIDv7.generate().version should be(7) }
      }
    }

    "#variant" - {
      "生成したUUIDv7からRFC4122のバリアント情報を取得できる" in {
        (1 to 10000).foreach { _ => UUIDv7.generate().variant should be(2) }
      }
    }
  }
}
