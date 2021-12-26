package io.github.kzmake.uuid7s.benchmark

import org.openjdk.jmh.annotations._
import java.util.concurrent.TimeUnit

@BenchmarkMode(Array(Mode.SampleTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class UXIDBenchmark {
  @Benchmark
  def kzmake_UUID(): Unit = {
    io.github.kzmake.uuid7s.UUID.generate().toString
  }

  @Benchmark
  def java_UUID() = {
    java.util.UUID.randomUUID().toString
  }

  @Benchmark
  def airframe_ULID(): Unit = {
    wvlet.airframe.ulid.ULID.newULID.toString
  }
}
